package org.victayagar.retromode_app.activity.ui.inicio;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.victayagar.retromode_app.R;
import org.victayagar.retromode_app.adapter.CategoriaAdapter;
import org.victayagar.retromode_app.adapter.ProductosRecomendadosAdapter;
import org.victayagar.retromode_app.adapter.SliderAdapter;
import org.victayagar.retromode_app.communication.Communication;
import org.victayagar.retromode_app.entidad.SliderItem;
import org.victayagar.retromode_app.entidad.servicio.Producto;
import org.victayagar.retromode_app.viewmodel.CategoriaViewModel;
import org.victayagar.retromode_app.viewmodel.ProductoViewModel;

import java.util.ArrayList;
import java.util.List;


public class InicioFragment extends Fragment implements Communication {
    private CategoriaViewModel categoriaViewModel;
    private ProductoViewModel productoViewModel;
    private RecyclerView rcvProductosRecomendados;
    private ProductosRecomendadosAdapter adapter;
    private List<Producto> productos = new ArrayList<>();
    private GridView gvCategorias;
    private CategoriaAdapter categoriaAdapter;

    private SliderView svCarrusel;
    private SliderAdapter sliderAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inicio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        initAdapter();
        loadData();
    }

    private void init(View v){

        svCarrusel = v.findViewById(R.id.svCarrusel);
        ViewModelProvider vmp = new ViewModelProvider(this);
        //Categorias
        categoriaViewModel = vmp.get(CategoriaViewModel.class);
        gvCategorias = v.findViewById(R.id.gvCategorias);
        //Productos
        rcvProductosRecomendados = v.findViewById(R.id.rcvProductosRecomendados);
        rcvProductosRecomendados.setLayoutManager(new GridLayoutManager(getContext(), 1));
        productoViewModel = vmp.get(ProductoViewModel.class);
    }
    private void initAdapter(){
        //Carrusel de imagenes
        sliderAdapter = new SliderAdapter(getContext());
        svCarrusel.setSliderAdapter(sliderAdapter);
        svCarrusel.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        svCarrusel.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        svCarrusel.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        svCarrusel.setIndicatorSelectedColor(Color.WHITE);
        svCarrusel.setIndicatorUnselectedColor(Color.GRAY);
        svCarrusel.setScrollTimeInSec(4); //set scroll delay in seconds :
        svCarrusel.startAutoCycle();
        //Categorias
        categoriaAdapter = new CategoriaAdapter(getContext(), R.layout.item_categorias, new ArrayList<>());
        gvCategorias.setAdapter(categoriaAdapter);
        //Productos
        adapter = new ProductosRecomendadosAdapter(productos, this);
        rcvProductosRecomendados.setAdapter(adapter);
    }
    private void loadData(){

        List<SliderItem> lista = new ArrayList<>();
        lista.add(new SliderItem(R.drawable.nuevo_stock, "¡Nuevo stock!"));
        lista.add(new SliderItem(R.drawable.promo_amigo, "¡Referidos!"));
        lista.add(new SliderItem(R.drawable.vaqueros1, "¡Tenemos gran variedad de vaqueros!"));
        sliderAdapter.updateItem(lista);
        categoriaViewModel.listarCategoriasActivas().observe(getViewLifecycleOwner(), response -> {
            if(response.getRpta() == 1){
                categoriaAdapter.clear();
                categoriaAdapter.addAll(response.getBody());
                categoriaAdapter.notifyDataSetChanged();
            }else{
                System.out.println("Error al obtener las categorías activas");
            }
        });
        productoViewModel.listarProductosRecomendados().observe(getViewLifecycleOwner(), response -> {
           adapter.updateItems(response.getBody());
        });
    }

    @Override
    public void showDetails(Intent i) {
        getActivity().startActivity(i);
        getActivity().overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    @Override
    public void exportInvoice(int idCli, int idOrden, String fileName) {

    }
}