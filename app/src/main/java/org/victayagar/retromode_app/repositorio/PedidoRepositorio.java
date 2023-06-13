package org.victayagar.retromode_app.repositorio;

import static org.victayagar.retromode_app.utils.Global.OPERACION_CORRECTA;
import static org.victayagar.retromode_app.utils.Global.RPTA_OK;
import static org.victayagar.retromode_app.utils.Global.TIPO_DATA;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.victayagar.retromode_app.api.ConfigApi;
import org.victayagar.retromode_app.api.PedidoApi;
import org.victayagar.retromode_app.entidad.GenericResponse;
import org.victayagar.retromode_app.entidad.servicio.Pedido;
import org.victayagar.retromode_app.entidad.servicio.dto.GenerarPedidoDTO;
import org.victayagar.retromode_app.entidad.servicio.dto.PedidoConDetallesDTO;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PedidoRepositorio {
    private final PedidoApi api;
    private static PedidoRepositorio repositorio;

    public PedidoRepositorio() {
        this.api = ConfigApi.getPedidoApi();
    }

    public static PedidoRepositorio getInstance() {
        if (repositorio == null) {
            repositorio = new PedidoRepositorio();
        }
        return repositorio;
    }

    public LiveData<GenericResponse<List<PedidoConDetallesDTO>>> listarPedidosPorCliente(int idCli) {
        final MutableLiveData<GenericResponse<List<PedidoConDetallesDTO>>> mld = new MutableLiveData<>();
        this.api.listarPedidosPorCliente(idCli).enqueue(new Callback<GenericResponse<List<PedidoConDetallesDTO>>>() {
            @Override
            public void onResponse(Call<GenericResponse<List<PedidoConDetallesDTO>>> call, Response<GenericResponse<List<PedidoConDetallesDTO>>> response) {
                mld.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<List<PedidoConDetallesDTO>>> call, Throwable t) {
                mld.setValue(new GenericResponse());
                System.out.println("Error al obtener los pedidos: " + t.getMessage());
                t.printStackTrace();
            }
        });
        return mld;
    }

    //GUARDAR PEDIDO CON DETALLES
    public LiveData<GenericResponse<GenerarPedidoDTO>> save(GenerarPedidoDTO dto) {
        MutableLiveData<GenericResponse<GenerarPedidoDTO>> data = new MutableLiveData<>();
        api.guardarPedido(dto).enqueue(new Callback<GenericResponse<GenerarPedidoDTO>>() {
            @Override
            public void onResponse(Call<GenericResponse<GenerarPedidoDTO>> call, Response<GenericResponse<GenerarPedidoDTO>> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<GenericResponse<GenerarPedidoDTO>> call, Throwable t) {
                data.setValue(new GenericResponse<>());
                t.printStackTrace();
            }
        });
        return data;
    }

    //ANULAR PEDIDO
    public LiveData<GenericResponse<Pedido>> anularPedido(int id) {
        MutableLiveData<GenericResponse<Pedido>> mld = new MutableLiveData<>();
        this.api.anularPedido(id).enqueue(new Callback<GenericResponse<Pedido>>() {
            @Override
            public void onResponse(Call<GenericResponse<Pedido>> call, Response<GenericResponse<Pedido>> response) {
                if (response.isSuccessful()) {
                    mld.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<GenericResponse<Pedido>> call, Throwable t) {
                mld.setValue(new GenericResponse<>());
                t.printStackTrace();
            }
        });
        return mld;
    }

    /**
     * Este método develve el reporte PDF de la compra realizada
     *
     * @param idCli
     * @param idOrden
     */
    public LiveData<GenericResponse<ResponseBody>> exportInvoice(int idCli, int idOrden) {
        MutableLiveData<GenericResponse<ResponseBody>> mld = new MutableLiveData<>();
        this.api.exportInvoicePDF(idCli, idOrden).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    mld.setValue(new GenericResponse<>(TIPO_DATA, RPTA_OK, OPERACION_CORRECTA, response.body()));
                    Log.e("exportInvoice", "file recived");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("exportInvoice", t.getMessage());
                t.printStackTrace();
            }
        });
        return mld;
    }
}