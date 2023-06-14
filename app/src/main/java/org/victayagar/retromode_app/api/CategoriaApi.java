package org.victayagar.retromode_app.api;

import org.victayagar.retromode_app.entidad.GenericResponse;
import org.victayagar.retromode_app.entidad.servicio.Categoria;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/*
La interfaz CategoriaApi tiene un campo base que representa la URL base para las solicitudes
relacionadas con las categorías. El método listarCategoriasActivas() utiliza la anotación
@GET para indicar que se trata de una solicitud GET y se concatena con base para formar la
ruta completa de la solicitud. Este método devuelve un objeto Call<GenericResponse<List<Categoria>>>,
que representa una llamada a la API para obtener una lista de categorías activas.
*/

public interface CategoriaApi {
    String base = "api/categoria";

    // Método para obtener una lista de categorías activas
    @GET(base)
    Call<GenericResponse<List<Categoria>>> listarCategoriasActivas();
}
