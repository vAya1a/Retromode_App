package org.victayagar.retromode_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import org.victayagar.retromode_app.R;
import org.victayagar.retromode_app.activity.ListarProductosPorCategoriaActivity;
import org.victayagar.retromode_app.api.ConfigApi;
import org.victayagar.retromode_app.entidad.servicio.Categoria;

import java.util.List;

/*
Esta clase extiende ArrayAdapter y se utiliza para mostrar las categorías en una vista de lista.
Cada elemento de la lista muestra una imagen de la categoría y su nombre. Al hacer clic en un elemento
de la lista, se inicia la actividad ListarProductosPorCategoriaActivity con el ID de la categoría
como dato extra en el intent.
*/


public class CategoriaAdapter extends ArrayAdapter<Categoria> {
    private final String url = ConfigApi.baseUrlE + "/api/documento-almacenado/download/";

    public CategoriaAdapter(@NonNull Context context, int resource, @NonNull List<Categoria> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_categorias, parent, false);
        }
        Categoria c = this.getItem(position);
        ImageView imgCategoria = convertView.findViewById(R.id.imgCategoria);
        TextView txtNombreCategoria = convertView.findViewById(R.id.txtNombreCategoria);

        // Configuración de Picasso para cargar y mostrar la imagen de la categoría
        Picasso picasso = new Picasso.Builder(convertView.getContext())
                .downloader(new OkHttp3Downloader(ConfigApi.getClient()))
                .build();
        picasso.load(url + c.getFoto().getFileName()) // Carga la imagen desde la URL completa
                .error(R.drawable.image_not_found) // Muestra esta imagen en caso de error de carga
                .into(imgCategoria); // Asocia la imagen al ImageView
        txtNombreCategoria.setText(c.getNombre()); // Muestra el nombre de la categoría en el TextView
        convertView.setOnClickListener(v -> {
            // Inicia la actividad ListarProductosPorCategoriaActivity al hacer clic en el elemento de la lista
            Intent i = new Intent(getContext(), ListarProductosPorCategoriaActivity.class);
            i.putExtra("idC", c.getId());// Envía el ID de la categoría como dato extra en el intent
            getContext().startActivity(i);
        });
        return convertView;
    }
}