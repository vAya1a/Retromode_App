package org.victayagar.retromode_app.activity.ui.inicio;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.victayagar.retromode_app.R;
import org.victayagar.retromode_app.adapter.SliderAdapter;
import org.victayagar.retromode_app.entidad.SliderItem;

import java.util.ArrayList;
import java.util.List;


public class InicioFragment extends Fragment {

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
    }
    private void initAdapter(){
        sliderAdapter = new SliderAdapter(getContext());
        svCarrusel.setSliderAdapter(sliderAdapter);
        svCarrusel.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        svCarrusel.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        svCarrusel.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        svCarrusel.setIndicatorSelectedColor(Color.WHITE);
        svCarrusel.setIndicatorUnselectedColor(Color.GRAY);
        svCarrusel.setScrollTimeInSec(4); //set scroll delay in seconds :
        svCarrusel.startAutoCycle();
    }
    private void loadData(){

        List<SliderItem> lista = new ArrayList<>();
        lista.add(new SliderItem(R.drawable.nuevo_stock, "¡Nuevo stock!"));
        lista.add(new SliderItem(R.drawable.promo_amigo, "¡Referidos!"));
        lista.add(new SliderItem(R.drawable.catalogo_camisas, "¡Echa un vistazo a nuestras camisas!"));
        lista.add(new SliderItem(R.drawable.vaqueros, "¡Tenemos gran variedad de vaqueros!"));
        sliderAdapter.updateItem(lista);
    }
}