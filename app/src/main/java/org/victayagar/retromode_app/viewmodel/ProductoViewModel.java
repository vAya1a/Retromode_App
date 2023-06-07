package org.victayagar.retromode_app.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.victayagar.retromode_app.entidad.GenericResponse;
import org.victayagar.retromode_app.entidad.servicio.Producto;
import org.victayagar.retromode_app.repositorio.ProductoRepositorio;

import java.util.List;

public class ProductoViewModel extends AndroidViewModel {
    private final ProductoRepositorio repositorio;

    public ProductoViewModel(@NonNull Application application) {
        super(application);
        repositorio = ProductoRepositorio.getInstance();
    }
    public LiveData<GenericResponse<List<Producto>>> listarProductosRecomendados(){
        return this.repositorio.listarProductosRecomendados();
    }
    public LiveData<GenericResponse<List<Producto>>> listarProductosPorCategoria(int idC){
        return this.repositorio.listarProductosPorCategoria(idC);
    }
}
