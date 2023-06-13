package org.victayagar.retromode_app.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.victayagar.retromode_app.entidad.GenericResponse;
import org.victayagar.retromode_app.entidad.servicio.Categoria;
import org.victayagar.retromode_app.repositorio.CategoriaRepositorio;

import java.util.List;

public class CategoriaViewModel extends AndroidViewModel {
    private final CategoriaRepositorio repositorio;

    public CategoriaViewModel(@NonNull Application application) {
        super(application);
        this.repositorio = CategoriaRepositorio.getInstance();
    }

    public LiveData<GenericResponse<List<Categoria>>> listarCategoriasActivas() {
        return this.repositorio.listarCategoriasActivas();
    }
}
