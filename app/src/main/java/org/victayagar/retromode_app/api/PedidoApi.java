package org.victayagar.retromode_app.api;

import org.victayagar.retromode_app.entidad.GenericResponse;
import org.victayagar.retromode_app.entidad.servicio.dto.PedidoConDetallesDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PedidoApi {
    String base = "api/pedido";
    @GET(base + "/misPedidos/{idCli}")
    Call<GenericResponse<List<PedidoConDetallesDTO>>> listarPedidosPorCliente(@Path("idCli") int idCli);
}
