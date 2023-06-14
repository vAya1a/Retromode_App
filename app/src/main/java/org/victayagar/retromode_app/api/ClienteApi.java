package org.victayagar.retromode_app.api;

import org.victayagar.retromode_app.entidad.GenericResponse;
import org.victayagar.retromode_app.entidad.servicio.Cliente;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/*
La interfaz ClienteApi tiene un campo base que representa la URL base
para las solicitudes relacionadas con los clientes. El método guardarCliente()
utiliza la anotación @POST para indicar que se trata de una solicitud POST y se
concatena con base para formar la ruta completa de la solicitud. Este método acepta
un objeto Cliente en el cuerpo de la solicitud, representado por la anotación @Body.
El método devuelve un objeto Call<GenericResponse<Cliente>>, que representa una llamada a la API
ara guardar un cliente y obtener una respuesta genérica.
*/

public interface ClienteApi {

    String base = "api/cliente";

    // Método para guardar un cliente en la API
    @POST(base)
    Call<GenericResponse<Cliente>> guardarCliente(@Body Cliente c);
}
