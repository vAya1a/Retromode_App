package org.victayagar.retromode_app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import org.victayagar.retromode_app.R;
import org.victayagar.retromode_app.adapter.ProductosPorCategoriaAdapter;
import org.victayagar.retromode_app.entidad.servicio.Producto;
import org.victayagar.retromode_app.viewmodel.ProductoViewModel;

import java.util.ArrayList;
import java.util.List;

/*
 La clase ListarProductosPorCategoriaActivity se encarga de
 mostrar una lista de productos filtrados por categoría, utilizando un RecyclerView
 y un adaptador personalizado.
 */

public class ListarProductosPorCategoriaActivity extends AppCompatActivity {
    private ProductoViewModel productoViewModel;
    private ProductosPorCategoriaAdapter adapter;
    private List<Producto> productos = new ArrayList<>();
    private RecyclerView rcvProductoPorCategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_productos_por_categoria);
        init(); // Inicializa la actividad
        initViewModel(); // Inicializa el ViewModel para obtener los datos de los productos
        initAdapter(); // Inicializa el adaptador y configura el RecyclerView
        loadData(); // Carga los datos de los productos por categoría
    }

    // Inicializa la barra de herramientas y su comportamiento de navegación
    private void init() {
        Toolbar toolbar = this.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_volveratras);
        toolbar.setNavigationOnClickListener(v -> {
            this.finish();
            this.overridePendingTransition(R.anim.rigth_in, R.anim.rigth_out);
        });
    }

    // Inicializa el ViewModel necesario para obtener los datos de los productos por categoría
    private void initViewModel() {
        final ViewModelProvider vmp = new ViewModelProvider(this);
        this.productoViewModel = vmp.get(ProductoViewModel.class);
    }

    // Inicializa el adaptador y configura el RecyclerView para mostrar la lista de productos
    private void initAdapter() {
        adapter = new ProductosPorCategoriaAdapter(productos);
        rcvProductoPorCategoria = findViewById(R.id.rcvProductosPorCategoria);
        rcvProductoPorCategoria.setAdapter(adapter);
        rcvProductoPorCategoria.setLayoutManager(new LinearLayoutManager(this));
    }

    // Obtiene los datos de los productos por categoría y los carga en el adaptador
    private void loadData() {
        int idC = getIntent().getIntExtra("idC", 0); // Recibe el ID de la categoría seleccionada
        productoViewModel.listarProductosPorCategoria(idC).observe(this, response -> adapter.updateItems(response.getBody()));
    }
}