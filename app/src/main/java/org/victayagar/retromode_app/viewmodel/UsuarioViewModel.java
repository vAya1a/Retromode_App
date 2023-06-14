package org.victayagar.retromode_app.viewmodel;

/*
El UsuarioViewModel es una clase de ViewModel que extiende AndroidViewModel y se
utiliza para proporcionar datos y funciones relacionadas con usuarios a la interfaz de usuario.
El UsuarioViewModel utiliza AndroidViewModel como clase base y recibe una instancia de Application
en su constructor. Esto permite que el ViewModel tenga una referencia a la aplicación y se
pueda acceder a ella si es necesario.
*/

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

    public LiveData<GenericResponse<Usuario>> save(Usuario u) {
        return this.repositorio.save(u);
    }
}
