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

/*
El repositorio ClienteRepositorio se encarga de interactuar con la API de
clientes para guardar un cliente
*/

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
        // Creamos un MutableLiveData para almacenar la respuesta de la API
        final MutableLiveData<GenericResponse<Cliente>> mld = new MutableLiveData<>();
        // Realizamos la llamada a la API para guardar el cliente
        this.api.guardarCliente(c).enqueue(new Callback<GenericResponse<Cliente>>() {
            @Override
            public void onResponse(Call<GenericResponse<Cliente>> call, Response<GenericResponse<Cliente>> response) {
                // En caso de respuesta exitosa, establecemos el valor del MutableLiveData con la respuesta de la API
                mld.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<Cliente>> call, Throwable t) {
                // En caso de error, mostramos un mensaje por consola y establecemos un GenericResponse vacío en el MutableLiveData
                mld.setValue(new GenericResponse<>());
                System.out.println("Se ha producido un error : " + t.getMessage());
                t.printStackTrace();
            }
        });
        // Devolvemos el MutableLiveData como LiveData para que los observadores puedan recibir actualizaciones
        return mld;
    }
}