package org.victayagar.retromode_app.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.victayagar.retromode_app.entidad.GenericResponse;
import org.victayagar.retromode_app.entidad.servicio.Producto;
import org.victayagar.retromode_app.repositorio.ProductoRepositorio;

import java.util.List;

/*
El ProductoViewModel es una clase de ViewModel que extiende AndroidViewModel
y se utiliza para proporcionar datos y funciones relacionadas con productos a la interfaz de usuario.
El ProductoViewModel utiliza AndroidViewModel como clase base y recibe una instancia de Application en
su constructor. Esto permite que el ViewModel tenga una referencia a la aplicación y se pueda acceder
a ella si es necesario.
*/

public class ProductoViewModel extends AndroidViewModel {
    private final ProductoRepositorio repositorio;

    public ProductoViewModel(@NonNull Application application) {
        super(application);
        repositorio = ProductoRepositorio.getInstance();
    }

    public LiveData<GenericResponse<List<Producto>>> listarProductosRecomendados() {
        return this.repositorio.listarProductosRecomendados();
    }

    public LiveData<GenericResponse<List<Producto>>> listarProductosPorCategoria(int idC) {
        return this.repositorio.listarProductosPorCategoria(idC);
    }
}
