package org.victayagar.retromode_app.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.victayagar.retromode_app.entidad.GenericResponse;
import org.victayagar.retromode_app.entidad.servicio.Pedido;
import org.victayagar.retromode_app.entidad.servicio.dto.GenerarPedidoDTO;
import org.victayagar.retromode_app.entidad.servicio.dto.PedidoConDetallesDTO;
import org.victayagar.retromode_app.repositorio.PedidoRepositorio;

import java.util.List;

import okhttp3.ResponseBody;

/*
El PedidoViewModel es una clase de ViewModel que extiende AndroidViewModel y
se utiliza para proporcionar datos y funciones relacionadas con pedidos a la
interfaz de usuario.
El PedidoViewModel utiliza AndroidViewModel como clase base y recibe una instancia de
Application en su constructor. Esto permite que el ViewModel tenga una referencia a la
aplicación y se pueda acceder a ella si es necesario.
*/

public class PedidoViewModel extends AndroidViewModel {
    private final PedidoRepositorio repositorio;

    public PedidoViewModel(@NonNull Application application) {
        super(application);
        this.repositorio = PedidoRepositorio.getInstance();
    }

    public LiveData<GenericResponse<List<PedidoConDetallesDTO>>> listarPedidosPorCliente(int idCli) {
        return this.repositorio.listarPedidosPorCliente(idCli);
    }

    public LiveData<GenericResponse<GenerarPedidoDTO>> guardarPedido(GenerarPedidoDTO dto) {
        return repositorio.save(dto);
    }

    public LiveData<GenericResponse<Pedido>> anularPedido(int id) {
        return repositorio.anularPedido(id);
    }

    /**
     * Export invoice
     *
     * @param idCli
     * @param idOrden
     * @return
     */
    public LiveData<GenericResponse<ResponseBody>> exportInvoice(int idCli, int idOrden) {
        return repositorio.exportInvoice(idCli, idOrden);
    }
}
