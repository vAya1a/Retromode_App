package org.victayagar.retromode_app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import org.victayagar.retromode_app.R;
import org.victayagar.retromode_app.api.ConfigApi;
import org.victayagar.retromode_app.entidad.servicio.DetallePedido;

import java.util.List;
import java.util.Locale;

/*
La clase DetalleMisComprasAdapter es un adaptador personalizado para el
RecyclerView que se utiliza en la pantalla de detalle de compras del usuario.
*/

public class DetalleMisComprasAdapter extends RecyclerView.Adapter<DetalleMisComprasAdapter.ViewHolder> {
    private final List<DetallePedido> detalles;

    public DetalleMisComprasAdapter(List<DetallePedido> detalles) {
        this.detalles = detalles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla el diseño del elemento de la lista
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detalle_miscompras, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Asigna los datos del detalle de pedido al ViewHolder en la posición dada
        holder.setItem(this.detalles.get(position));
    }

    @Override
    public int getItemCount() {
        return detalles.size();
    }

    public void updateItems(List<DetallePedido> detalles) {
        // Actualiza la lista de detalles con una nueva lista y notifica los cambios
        this.detalles.clear();
        this.detalles.addAll(detalles);
        this.notifyDataSetChanged();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgProducto;
        private TextView txtValueCodDetailPurchases, txtValueProducto, txtValueCantidad, txtValuePrecioProducto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Inicializa los elementos de la vista del elemento de la lista
            this.imgProducto = itemView.findViewById(R.id.imgProducto);
            this.txtValueCodDetailPurchases = itemView.findViewById(R.id.txtValueCodDetailPurchases);
            this.txtValueProducto = itemView.findViewById(R.id.txtValueProducto);
            this.txtValueCantidad = itemView.findViewById(R.id.txtValueCantidad);
            this.txtValuePrecioProducto = itemView.findViewById(R.id.txtValuePrecioProducto);
        }

        public void setItem(final DetallePedido detalle) {
            String url = ConfigApi.baseUrlE + "/api/documento-almacenado/download/" + detalle.getProducto().getFoto().getFileName();

            // Configuración de Picasso para cargar y mostrar la imagen del producto
            Picasso picasso = new Picasso.Builder(itemView.getContext())
                    .downloader(new OkHttp3Downloader(ConfigApi.getClient()))
                    .build();
            picasso.load(url)
                    .error(R.drawable.image_not_found)
                    .into(imgProducto);

            // Asigna los valores del detalle de pedido a los elementos de la vista
            txtValueCodDetailPurchases.setText("C000" + Integer.toString(detalle.getPedido().getId()));
            txtValueProducto.setText(detalle.getProducto().getNombre());
            txtValueCantidad.setText(Integer.toString(detalle.getCantidad()));
            txtValuePrecioProducto.setText(String.format(Locale.ENGLISH, "€%.2f", detalle.getPrecio()));
        }

    }
}
