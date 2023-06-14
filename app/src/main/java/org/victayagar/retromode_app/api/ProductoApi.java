package org.victayagar.retromode_app.api;

import org.victayagar.retromode_app.entidad.GenericResponse;
import org.victayagar.retromode_app.entidad.servicio.Producto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/*
El método listarProductosRecomendados() realiza una solicitud GET para
obtener una lista de productos recomendados.

El método listarProductosPorCategoria() realiza una solicitud GET para
obtener una lista de productos filtrados por categoría. Toma un parámetro
idC que se utiliza para especificar el ID de la categoría en la URL.
*/

public interface ProductoApi {
    String base = "api/producto";

    // Método para listar productos recomendados
    @GET(base)
    Call<GenericResponse<List<Producto>>> listarProductosRecomendados();

    // Método para listar productos por categoría
    @GET(base + "/{idC}")
    Call<GenericResponse<List<Producto>>> listarProductosPorCategoria(@Path("idC") int idC);
}
