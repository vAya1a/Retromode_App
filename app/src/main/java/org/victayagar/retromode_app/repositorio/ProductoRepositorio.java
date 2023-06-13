package org.victayagar.retromode_app.repositorio;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.victayagar.retromode_app.api.ConfigApi;
import org.victayagar.retromode_app.api.ProductoApi;
import org.victayagar.retromode_app.entidad.GenericResponse;
import org.victayagar.retromode_app.entidad.servicio.Producto;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductoRepositorio {
    private final ProductoApi api;
    private static ProductoRepositorio repositorio;

    public ProductoRepositorio() {
        this.api = ConfigApi.getProductoApi();
    }

    public static ProductoRepositorio getInstance() {
        if (repositorio == null) {
            repositorio = new ProductoRepositorio();
        }
        return repositorio;
    }

    public LiveData<GenericResponse<List<Producto>>> listarProductosRecomendados() {
        final MutableLiveData<GenericResponse<List<Producto>>> mld = new MutableLiveData<>();
        this.api.listarProductosRecomendados().enqueue(new Callback<GenericResponse<List<Producto>>>() {
            @Override
            public void onResponse(Call<GenericResponse<List<Producto>>> call, Response<GenericResponse<List<Producto>>> response) {
                mld.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<List<Producto>>> call, Throwable t) {
                mld.setValue(new GenericResponse<>());
                t.printStackTrace();
            }
        });
        return mld;
    }

    public LiveData<GenericResponse<List<Producto>>> listarProductosPorCategoria(int idC) {
        final MutableLiveData<GenericResponse<List<Producto>>> mld = new MutableLiveData<>();
        this.api.listarProductosPorCategoria(idC).enqueue(new Callback<GenericResponse<List<Producto>>>() {
            @Override
            public void onResponse(Call<GenericResponse<List<Producto>>> call, Response<GenericResponse<List<Producto>>> response) {
                mld.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<List<Producto>>> call, Throwable t) {
                mld.setValue(new GenericResponse<>());
                t.printStackTrace();
            }
        });
        return mld;
    }
}
