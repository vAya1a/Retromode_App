package org.victayagar.retromode_app.api;

import org.victayagar.retromode_app.entidad.GenericResponse;
import org.victayagar.retromode_app.entidad.servicio.Usuario;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/*
El método login() realiza una solicitud POST para autenticar al usuario. Toma dos parámetros
email y contra que se envían en el cuerpo de la solicitud codificados en el formato de formulario URL-encoding.
El método save() realiza una solicitud POST para guardar un nuevo usuario. Toma un parámetro
u que representa el objeto Usuario a guardar y se envía en el cuerpo de la solicitud como JSON.
*/

public interface UsuarioApi {
    // Ruta del controlador Usuario
    String base = "api/usuario";

    // Ruta del controlador Usuario + la ruta del método
    @FormUrlEncoded
    @POST(base + "/login")
    Call<GenericResponse<Usuario>> login(@Field("email") String email, @Field("pass") String contra);

    @POST(base)
    Call<GenericResponse<Usuario>> save(@Body Usuario u);
}
