package org.victayagar.retromode_app.api;

import org.victayagar.retromode_app.entidad.GenericResponse;
import org.victayagar.retromode_app.entidad.servicio.Pedido;
import org.victayagar.retromode_app.entidad.servicio.dto.GenerarPedidoDTO;
import org.victayagar.retromode_app.entidad.servicio.dto.PedidoConDetallesDTO;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

/*
El método listarPedidosPorCliente() realiza una solicitud GET para obtener una lista
de pedidos con detalles para un cliente específico. Toma un parámetro idCli que se
utiliza para especificar el ID del cliente en la URL.
El método guardarPedido() realiza una solicitud POST para guardar un pedido.
Toma un objeto GenerarPedidoDTO en el cuerpo de la solicitud para enviar los detalles del pedido.
El método anularPedido() realiza una solicitud DELETE para anular un pedido existente.
Toma un parámetro id que se utiliza para especificar el ID del pedido en la URL.
El método exportInvoicePDF() realiza una solicitud GET para exportar una factura en
formato PDF. Toma dos parámetros idCli e idOrden que se utilizan para especificar el ID
del cliente y el ID del pedido en los parámetros de consulta de la URL. La anotación
@Streaming se utiliza para indicar que la respuesta será transmitida de forma continua.
Estos métodos definen las operaciones disponibles en la API relacionadas con los pedidos
*/

public interface PedidoApi {
    String base = "api/pedido";

    // Método para listar los pedidos de un cliente
    @GET(base + "/misPedidos/{idCli}")
    Call<GenericResponse<List<PedidoConDetallesDTO>>> listarPedidosPorCliente(@Path("idCli") int idCli);

    // Método para guardar un pedido
    @POST(base)
    Call<GenericResponse<GenerarPedidoDTO>> guardarPedido(@Body GenerarPedidoDTO dto);

    // Método para anular un pedido
    @DELETE(base + "/{id}")
    Call<GenericResponse<Pedido>> anularPedido(@Path("id") int id);

    // Método para exportar una factura en formato PDF
    @Streaming
    @GET(base + "/exportInvoice")
    Call<ResponseBody> exportInvoicePDF(@Query("idCli") int idCli, @Query("idOrden") int idOrden);

}
