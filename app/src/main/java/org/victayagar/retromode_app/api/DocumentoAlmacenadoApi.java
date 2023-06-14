package org.victayagar.retromode_app.api;

import org.victayagar.retromode_app.entidad.GenericResponse;
import org.victayagar.retromode_app.entidad.servicio.DocumentoAlmacenado;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/*
La interfaz DocumentoAlmacenadoApi tiene un campo base que representa la URL base
para las solicitudes relacionadas con los documentos almacenados. El método save()
utiliza las anotaciones @Multipart y @POST para indicar que se trata de una solicitud
POST multipart/form-data y se concatena con base para formar la ruta completa de la solicitud.
Este método acepta dos parámetros: file de tipo MultipartBody.Part, que representa el archivo
a subir, y requestBody de tipo RequestBody, que representa el nombre del archivo en la solicitud.
El método devuelve un objeto Call<GenericResponse<DocumentoAlmacenado>>, que representa una llamada
a la API para guardar un documento almacenado y obtener una respuesta genérica.
*/

public interface DocumentoAlmacenadoApi {
    String base = "api/documento-almacenado";

    // Método para guardar un documento almacenado en la API
    @Multipart
    @POST(base)
    Call<GenericResponse<DocumentoAlmacenado>> save(@Part MultipartBody.Part file, @Part("nombre") RequestBody requestBody);
}
