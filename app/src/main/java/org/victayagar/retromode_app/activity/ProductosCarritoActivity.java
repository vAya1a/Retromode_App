package org.victayagar.retromode_app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.victayagar.retromode_app.R;
import org.victayagar.retromode_app.adapter.ProductoCarritoAdapter;
import org.victayagar.retromode_app.communication.CarritoCommunication;
import org.victayagar.retromode_app.entidad.servicio.DetallePedido;
import org.victayagar.retromode_app.entidad.servicio.Usuario;
import org.victayagar.retromode_app.entidad.servicio.dto.GenerarPedidoDTO;
import org.victayagar.retromode_app.utils.Carrito;
import org.victayagar.retromode_app.utils.DateSerializer;
import org.victayagar.retromode_app.utils.TimeSerializer;
import org.victayagar.retromode_app.viewmodel.PedidoViewModel;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;


/*
Esta clase se encarga de gestionar la visualización y manipulación
de los productos en el carrito de compras de la aplicación. Aquí se
realizan tareas como mostrar la lista de productos en el carrito, permitir
al usuario finalizar la compra, registrar el pedido en la base de datos y
mostrar mensajes de notificación al usuario. También se implementa la interfaz
CarritoCommunication para manejar la eliminación de detalles de pedido individuales del carrito.
*/
public class ProductosCarritoActivity extends AppCompatActivity implements CarritoCommunication {

    private PedidoViewModel pedidoViewModel;
    private ProductoCarritoAdapter adapter;
    private RecyclerView rcvCarritoCompras;
    private Button btnFinalizarCompra;
    final Gson g = new GsonBuilder()
            .registerTypeAdapter(Date.class, new DateSerializer())
            .registerTypeAdapter(Time.class, new TimeSerializer())
            .create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos_carrito);
        init();
        initViewModel();
        initAdapter();
    }

    // Inicializa las vistas y configura el botón de retroceso
    private void init() {
        Toolbar toolbar = this.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_volveratras);
        toolbar.setNavigationOnClickListener(v -> {//Reemplazo con lamba
            this.finish();
            this.overridePendingTransition(R.anim.rigth_in, R.anim.rigth_out);
        });
        rcvCarritoCompras = findViewById(R.id.rcvCarritoCompras);
        btnFinalizarCompra = findViewById(R.id.btnFinalizarCompra);
        btnFinalizarCompra.setOnClickListener(v -> {
            // Verifica si el usuario ha iniciado sesión
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            String pref = preferences.getString("UsuarioJson", "");
            Usuario u = g.fromJson(pref, Usuario.class);
            int idC = u.getCliente().getId();
            if (idC != 0) {
                if (Carrito.getDetallePedidos().isEmpty()) {
                    toastIncorrecto("¡Ups!, el carrito de compras está vacío");
                } else {
                    toastCorrecto("Procesando pedido...");
                    registrarPedido(idC);
                }
            } else {
                toastIncorrecto("No ha iniciado sesión, se le redirigirá al login");
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }
        });
    }

    // Inicializa el ViewModel necesario para obtener los datos relacionados con el pedido
    private void initViewModel() {
        final ViewModelProvider vmp = new ViewModelProvider(this);
        this.pedidoViewModel = vmp.get(PedidoViewModel.class);
    }

    // Inicializa el adaptador del RecyclerView y lo configura con los datos del carrito
    private void initAdapter() {
        adapter = new ProductoCarritoAdapter(Carrito.getDetallePedidos(), this);
        rcvCarritoCompras.setLayoutManager(new LinearLayoutManager(this));
        rcvCarritoCompras.setAdapter(adapter);
    }

    // Registra el pedido en la base de datos
    private void registrarPedido(int idC) {
        ArrayList<DetallePedido> detallePedidos = Carrito.getDetallePedidos();
        GenerarPedidoDTO dto = new GenerarPedidoDTO();
        java.util.Date date = new java.util.Date();
        dto.getPedido().setFechaCompra(new Date(date.getTime()));
        dto.getPedido().setAnularPedido(false);
        dto.getPedido().setMonto(getTotalV(detallePedidos));
        dto.getCliente().setId(idC);
        dto.setDetallePedidos(detallePedidos);
        this.pedidoViewModel.guardarPedido(dto).observe(this, response -> {
            if (response.getRpta() == 1) {
                toastCorrecto("¡El pedido se ha registrado en nuestra base con exito!");
                Carrito.limpiar();
                finish();
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
            } else {
                toastIncorrecto("¡Aviso!, ocurrió un error y no se pudo registrar el pedido");
            }
        });

    }

    // Calcula el total del pedido sumando el total de cada detalle
    private double getTotalV(List<DetallePedido> detalles) {
        float total = 0;
        for (DetallePedido dp : detalles) {
            total += dp.getTotal();
        }
        return total;
    }

    // Muestra un Toast personalizado de error
    public void toastIncorrecto(String texto) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View layouView = layoutInflater.inflate(R.layout.custom_toast_error, (ViewGroup) findViewById(R.id.ll_custom_toast_error));
        TextView textView = layouView.findViewById(R.id.txtMensajeToast2);
        textView.setText(texto);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layouView);
        toast.show();

    }

    // Muestra un Toast personalizado de éxito
    public void toastCorrecto(String texto) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View layouView = layoutInflater.inflate(R.layout.custom_toast_ok, (ViewGroup) findViewById(R.id.ll_custom_toast_ok));
        TextView textView = layouView.findViewById(R.id.txtMensajeToast1);
        textView.setText(texto);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layouView);
        toast.show();
    }

    // Implementación del método para eliminar un detalle del carrito
    @Override
    public void eliminarDetalle(int idP) {
        Carrito.eliminar(idP);
        this.adapter.notifyDataSetChanged();
    }
}