package org.victayagar.retromode_app.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.victayagar.retromode_app.entidad.GenericResponse;
import org.victayagar.retromode_app.entidad.servicio.Cliente;
import org.victayagar.retromode_app.repositorio.ClienteRepositorio;

/*
El ClienteViewModel es una clase de ViewModel que extiende AndroidViewModel y se utiliza
para proporcionar datos y funciones relacionadas con los clientes a la interfaz de usuario
*/

public class ClienteViewModel extends AndroidViewModel {
    private final ClienteRepositorio repositorio;

    public ClienteViewModel(@NonNull Application application) {
        super(application);
        this.repositorio = ClienteRepositorio.getInstance();
    }

    public LiveData<GenericResponse<Cliente>> guardarCliente(Cliente c) {
        return repositorio.guardarCliente(c);
    }
}
