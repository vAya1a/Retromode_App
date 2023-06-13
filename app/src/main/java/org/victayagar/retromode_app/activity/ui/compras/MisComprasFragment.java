package org.victayagar.retromode_app.activity.ui.compras;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.victayagar.retromode_app.R;
import org.victayagar.retromode_app.activity.VerInvoiceActivity;
import org.victayagar.retromode_app.adapter.MisComprasAdapter;
import org.victayagar.retromode_app.communication.AnularPedidoCommunication;
import org.victayagar.retromode_app.communication.Communication;
import org.victayagar.retromode_app.entidad.servicio.Usuario;
import org.victayagar.retromode_app.utils.DateSerializer;
import org.victayagar.retromode_app.utils.TimeSerializer;
import org.victayagar.retromode_app.viewmodel.PedidoViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class MisComprasFragment extends Fragment implements Communication, AnularPedidoCommunication {
    private ActivityResultLauncher<String> perReqLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
        if(result){
            successMessage("Gracias por concedernos el permiso, pulsa el botón nuevamente para descargar la factura");
        }else{
            errorMessage("Permiso denegado, no podemos continuar");
        }
    });
    private PedidoViewModel pedidoViewModel;
    private RecyclerView rcvPedidos;
    private MisComprasAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_miscompras, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        initViewModel();
        initAdapter();
        loadData();
    }

    private void init(View v) {
        rcvPedidos = v.findViewById(R.id.rcvMisCompras);
    }

    private void initViewModel() {
        pedidoViewModel = new ViewModelProvider(this).get(PedidoViewModel.class);
    }

    private void initAdapter() {
        adapter = new MisComprasAdapter(new ArrayList<>(), this, this);
        rcvPedidos.setLayoutManager(new GridLayoutManager(getContext(), 1));
        rcvPedidos.setAdapter(adapter);
    }

    private void loadData() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        final Gson g = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .registerTypeAdapter(Time.class, new TimeSerializer())
                .create();
        String usuarioJson = sp.getString("UsuarioJson", null);
        if (usuarioJson != null) {
            final Usuario u = g.fromJson(usuarioJson, Usuario.class);
            this.pedidoViewModel.listarPedidosPorCliente(u.getCliente().getId()).observe(getViewLifecycleOwner(), response -> {
                adapter.updateItems(response.getBody());
            });
        }
    }

    @Override
    public void showDetails(Intent i) {
        getActivity().startActivity(i);
        getActivity().overridePendingTransition(R.anim.above_in, R.anim.above_out);
    }

    @Override
    public void exportInvoice(int idCli, int idOrden, String fileName) {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            pedidoViewModel.exportInvoice(idCli, idOrden).observe(getViewLifecycleOwner(), response -> {
                if (response.getRpta() == 1) {
                    try {
                        boolean folderCreated = true;
                        File path = requireContext().getExternalFilesDir("/pedidos");
                        if (!path.exists()) {
                            folderCreated = false;
                            Toast.makeText(requireContext(), "Lo siento, no se pudo crear la carpeta para guardar los archivos", Toast.LENGTH_LONG);
                        }
                        if (folderCreated) {
                            byte[] bytes = response.getBody().bytes();
                            File file = new File(path, fileName);
                            FileOutputStream fileOutputStream = new FileOutputStream(file);
                            fileOutputStream.write(bytes);
                            fileOutputStream.close();
                            //PREVISUALIZAR EL PDF EN EL APP
                            new MaterialAlertDialogBuilder(requireContext()).setTitle("EXPORTAR FACTURA")
                                    .setMessage("Factura guardada correctamente en la siguiente ubicación: "
                                            + file.getAbsolutePath() + " ¿Deseas visualizarla ahora?")
                                    .setCancelable(false)
                                    .setPositiveButton("SÍ", (dialog, i) -> {
                                        dialog.dismiss();
                                        Intent intent = new Intent(requireContext(), VerInvoiceActivity.class);
                                        intent.putExtra("pdf", bytes);
                                        startActivity(intent);
                                    }).setNegativeButton("QUIZÁS MÁS TARDE", (dialogInterface, i) -> {
                                        dialogInterface.dismiss();
                                    }).show();
                        }
                    } catch (Exception e) {
                        errorMessage("No se pudo guardar el archivo en el dispositivo");
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(requireContext(), response.getMessage(), Toast.LENGTH_LONG);
                }
            });
        } else {
            new SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("DANOS PERMISO")
                    .setContentText("Debido a que vamos a descargar un archivo, es necesario tener acceso al almacenamiento interno...")
                    .setConfirmButton("VALE", sweetAlertDialog -> {
                        sweetAlertDialog.dismissWithAnimation();
                        perReqLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    }).setCancelButton("QUIZÁS MÁS TARDE", SweetAlertDialog::dismissWithAnimation).show();
        }
    }

    @Override
    public String anularPedido(int id) {
        this.pedidoViewModel.anularPedido(id).observe(getViewLifecycleOwner(), response -> {
            if (response.getRpta() == 1) {
                loadData();
            }
        });
        return "El pedido ha sido cancelado";
    }
    public void successMessage(String message) {
        new SweetAlertDialog(requireContext(),
                SweetAlertDialog.SUCCESS_TYPE).setTitleText("¡Hecho!")
                .setContentText(message).show();
    }
    public void errorMessage(String message) {
        new SweetAlertDialog(requireContext(),
                SweetAlertDialog.ERROR_TYPE).setTitleText("¡Ups!... ocurrió un error")
                .setContentText(message).show();
    }
}