package org.victayagar.retromode_app.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.victayagar.retromode_app.entidad.GenericResponse;
import org.victayagar.retromode_app.entidad.servicio.Usuario;
import org.victayagar.retromode_app.repositorio.UsuarioRepositorio;

public class UsuarioViewModel extends AndroidViewModel {
    private final UsuarioRepositorio repositorio;

    public UsuarioViewModel(@NonNull Application application) {
        super(application);
        this.repositorio = UsuarioRepositorio.getInstance();
    }

    public LiveData<GenericResponse<Usuario>> login(String email, String pass) {
        return this.repositorio.login(email, pass);
    }
}
