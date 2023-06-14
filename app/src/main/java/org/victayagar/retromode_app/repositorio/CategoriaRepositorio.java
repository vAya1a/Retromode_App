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

/*
El repositorio CategoriaRepositorio se encarga de interactuar con la API de
categorías para obtener la lista de categorías activas
*/

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
        // Creamos un MutableLiveData para almacenar la respuesta de la API
        final MutableLiveData<GenericResponse<List<Categoria>>> mld = new MutableLiveData<>();
        // Realizamos la llamada a la API para obtener la lista de categorías activas
        this.api.listarCategoriasActivas().enqueue(new Callback<GenericResponse<List<Categoria>>>() {
            @Override
            public void onResponse(Call<GenericResponse<List<Categoria>>> call, Response<GenericResponse<List<Categoria>>> response) {
                // En caso de respuesta exitosa, establecemos el valor del MutableLiveData con la respuesta de la API
                mld.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<List<Categoria>>> call, Throwable t) {
                // En caso de error, mostramos un mensaje por consola
                System.out.println("Error al obtenet las categorias: " + t.getMessage());
            }
        });
        // Devolvemos el MutableLiveData como LiveData para que los observadores puedan recibir actualizaciones
        return mld;
    }
}
