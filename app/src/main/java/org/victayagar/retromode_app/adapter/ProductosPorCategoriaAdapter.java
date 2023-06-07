package org.victayagar.retromode_app.adapter;

import android.animation.LayoutTransition;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import org.victayagar.retromode_app.R;
import org.victayagar.retromode_app.api.ConfigApi;
import org.victayagar.retromode_app.entidad.servicio.DetallePedido;
import org.victayagar.retromode_app.entidad.servicio.Producto;

import java.util.List;
import java.util.Locale;

public class ProductosPorCategoriaAdapter extends RecyclerView.Adapter<ProductosPorCategoriaAdapter.ViewHolder> {
    private List<Producto> listadoProductoPorCategoria;

    public ProductosPorCategoriaAdapter(List<Producto> listadoProductoPorCategoria) {
        this.listadoProductoPorCategoria = listadoProductoPorCategoria;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_productos_por_categoria, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setItem(this.listadoProductoPorCategoria.get(position));
    }

    @Override
    public int getItemCount() {
        return this.listadoProductoPorCategoria.size();
    }
    public void  updateItems(List<Producto> productosByCategoria){
        this.listadoProductoPorCategoria.clear();
        this.listadoProductoPorCategoria.addAll(productosByCategoria);
        this.notifyDataSetChanged();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder{
        private final ImageView imgProductoC;
        private final TextView nameProductoC, txtPrecioProductoC;
        private final Button btnPedirPC;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imgProductoC = itemView.findViewById(R.id.imgProductoC);
            this.nameProductoC = itemView.findViewById(R.id.nameProductoC);
            this.txtPrecioProductoC = itemView.findViewById(R.id.txtPrecioProductoC);
            this.btnPedirPC = itemView.findViewById(R.id.btnPedirPC);
        }
        public void setItem(final Producto p){
            String url = ConfigApi.baseUrlE + "/api/documento-almacenado/download/" + p.getFoto().getFileName();

            Picasso picasso = new Picasso.Builder(itemView.getContext())
                    .downloader(new OkHttp3Downloader(ConfigApi.getClient()))
                    .build();
            picasso.load(url)
                    //.networkPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE) //No lo almacena el la caché ni en el disco
                    .error(R.drawable.image_not_found)
                    .into(imgProductoC);
            nameProductoC.setText(p.getNombre());
            txtPrecioProductoC.setText(String.format(Locale.ENGLISH, "€%.2f", p.getPrecio()));
            btnPedirPC.setOnClickListener(v -> {
                Toast.makeText(itemView.getContext(), "Hola, Mundo", Toast.LENGTH_SHORT).show();
            });
        }
    }
}
