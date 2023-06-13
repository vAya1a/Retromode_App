package org.victayagar.retromode_app.repositorio;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.victayagar.retromode_app.api.CategoriaApi;
import org.victayagar.retromode_app.api.ConfigApi;
import org.victayagar.retromode_app.entidad.GenericResponse;
import org.victayagar.retromode_app.entidad.servicio.Categoria;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoriaRepositorio {
    private final CategoriaApi api;
    private static CategoriaRepositorio repositorio;

    public CategoriaRepositorio() {
        this.api = ConfigApi.getCategoriaApi();
    }

    public static CategoriaRepositorio getInstance() {
        if (repositorio == null) {
            repositorio = new CategoriaRepositorio();
        }
        return repositorio;
    }

    public LiveData<GenericResponse<List<Categoria>>> listarCategoriasActivas() {
        final MutableLiveData<GenericResponse<List<Categoria>>> mld = new MutableLiveData<>();
        this.api.listarCategoriasActivas().enqueue(new Callback<GenericResponse<List<Categoria>>>() {
            @Override
            public void onResponse(Call<GenericResponse<List<Categoria>>> call, Response<GenericResponse<List<Categoria>>> response) {
                mld.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<List<Categoria>>> call, Throwable t) {
                System.out.println("Error al obtenet las categorias: " + t.getMessage());
            }
        });
        return mld;
    }
}
