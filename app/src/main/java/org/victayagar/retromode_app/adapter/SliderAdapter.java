package org.victayagar.retromode_app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;


import org.victayagar.retromode_app.R;
import org.victayagar.retromode_app.entidad.SliderItem;

/*
Este código es un adaptador para un componente de vista de desplazamiento automático
de imágenes (slider) que muestra una lista de elementos SliderItem. Utiliza Glide para
cargar las imágenes y muestra el título correspondiente en un TextView. El método
updateItem permite actualizar la lista de elementos en el slider.
*/

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH> {
    private Context context;
    private List<SliderItem> mSliderItems = new ArrayList<>();

    public SliderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        // Inflar el diseño del elemento del slider
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {
        // Establecer los datos del elemento en la posición especificada

        SliderItem sliderItem = mSliderItems.get(position);

        viewHolder.textView.setText(sliderItem.getTitulo());
        viewHolder.textView.setTextSize(16);
        viewHolder.textView.setTextColor(Color.WHITE);
        // Cargar la imagen utilizando Glide
        Glide.with(viewHolder.itemView)
                .load(sliderItem.getImagen())
                .fitCenter()
                .into(viewHolder.imageView);

    }

    @Override
    public int getCount() {
        return mSliderItems.size();
    }

    public void updateItem(List<SliderItem> lista) {
        // Actualizar la lista de elementos del slider
        mSliderItems.clear();
        mSliderItems.addAll(lista);
        this.notifyDataSetChanged();
    }

    protected class SliderAdapterVH extends SliderViewAdapter.ViewHolder {
        View itemView;
        ImageView imageView;
        TextView textView;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_auto_image_slider);
            textView = itemView.findViewById(R.id.tv_auto_image_slider);
            this.itemView = itemView;
        }
    }

}
