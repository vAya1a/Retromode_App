package org.victayagar.retromode_app.repositorio;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.victayagar.retromode_app.api.ConfigApi;
import org.victayagar.retromode_app.api.UsuarioApi;
import org.victayagar.retromode_app.entidad.GenericResponse;
import org.victayagar.retromode_app.entidad.servicio.Usuario;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuarioRepositorio {
    private static UsuarioRepositorio repositorio;
    private final UsuarioApi api;

    public UsuarioRepositorio() {
        this.api = ConfigApi.getUsuarioApi();
    }

    public static UsuarioRepositorio getInstance() {
        if (repositorio == null) {
            repositorio = new UsuarioRepositorio();
        }
        return repositorio;
    }

    public LiveData<GenericResponse<Usuario>> login(String email, String contra) {
        final MutableLiveData<GenericResponse<Usuario>> mld = new MutableLiveData<>();
        this.api.login(email, contra).enqueue(new Callback<GenericResponse<Usuario>>() {
            @Override
            public void onResponse(Call<GenericResponse<Usuario>> call, Response<GenericResponse<Usuario>> response) {
                mld.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<Usuario>> call, Throwable t) {
                mld.setValue(new GenericResponse());
                System.out.println("Se ha producido un error al iniciar sesión: " + t.getMessage());
                t.printStackTrace();
            }
        });
        return mld;
    }

    public LiveData<GenericResponse<Usuario>> save(Usuario u) {
        final MutableLiveData<GenericResponse<Usuario>> mld = new MutableLiveData<>();
        this.api.save(u).enqueue(new Callback<GenericResponse<Usuario>>() {
            @Override
            public void onResponse(Call<GenericResponse<Usuario>> call, Response<GenericResponse<Usuario>> response) {
                mld.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<Usuario>> call, Throwable t) {
                mld.setValue(new GenericResponse<>());
                System.out.println("Se ha producido un error : " + t.getMessage());
                t.printStackTrace();
            }
        });
        return mld;
    }
}
