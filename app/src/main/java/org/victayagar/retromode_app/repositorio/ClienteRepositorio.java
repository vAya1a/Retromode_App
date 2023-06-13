package org.victayagar.retromode_app.repositorio;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.victayagar.retromode_app.api.ClienteApi;
import org.victayagar.retromode_app.api.ConfigApi;
import org.victayagar.retromode_app.entidad.GenericResponse;
import org.victayagar.retromode_app.entidad.servicio.Cliente;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClienteRepositorio {
    private static ClienteRepositorio repositorio;
    private final ClienteApi api;

    public static ClienteRepositorio getInstance() {
        if (repositorio == null) {
            repositorio = new ClienteRepositorio();
        }
        return repositorio;
    }

    private ClienteRepositorio() {
        api = ConfigApi.getClienteApi();
    }

    public LiveData<GenericResponse<Cliente>> guardarCliente(Cliente c) {
        final MutableLiveData<GenericResponse<Cliente>> mld = new MutableLiveData<>();
        this.api.guardarCliente(c).enqueue(new Callback<GenericResponse<Cliente>>() {
            @Override
            public void onResponse(Call<GenericResponse<Cliente>> call, Response<GenericResponse<Cliente>> response) {
                mld.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<Cliente>> call, Throwable t) {
                mld.setValue(new GenericResponse<>());
                System.out.println("Se ha producido un error : " + t.getMessage());
                t.printStackTrace();
            }
        });
        return mld;
    }
}