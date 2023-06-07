package org.victayagar.retromode_app.api;

import org.victayagar.retromode_app.entidad.GenericResponse;
import org.victayagar.retromode_app.entidad.servicio.Producto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ProductoApi {
    String base = "api/producto";

    @GET(base)
    Call<GenericResponse<List<Producto>>> listarProductosRecomendados();

    @GET(base + "/{idC}")
    Call<GenericResponse<List<Producto>>> listarProductosPorCategoria(@Path("idC") int idC);
}
