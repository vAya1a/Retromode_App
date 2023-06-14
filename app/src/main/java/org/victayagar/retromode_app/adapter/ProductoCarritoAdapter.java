package org.victayagar.retromode_app.adapter;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import org.victayagar.retromode_app.R;
import org.victayagar.retromode_app.api.ConfigApi;
import org.victayagar.retromode_app.communication.CarritoCommunication;
import org.victayagar.retromode_app.entidad.servicio.DetallePedido;

import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class ProductoCarritoAdapter extends RecyclerView.Adapter<ProductoCarritoAdapter.ViewHolder> {
    private final List<DetallePedido> detalles;
    private final CarritoCommunication c;

    public ProductoCarritoAdapter(List<DetallePedido> detalles, CarritoCommunication c) {
        this.detalles = detalles;
        this.c = c;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el diseño del elemento de la lista
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_productos_carrito, parent, false);
        return new ViewHolder(v, this.c);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Establecer los datos del elemento en la posición especificada
        holder.setItem(this.detalles.get(position));
    }

    @Override
    public int getItemCount() {
        return this.detalles.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgProductoDC, btnDecrease, btnAdd, btnEliminarPCarrito;
        private final EditText edtCantidad;
        private final TextView tvNombreProductoDC, tvPrecioPDC;
        private final CarritoCommunication c;

        public ViewHolder(@NonNull View itemView, CarritoCommunication c) {
            super(itemView);
            this.c = c;
            // Obtener las referencias de los elementos de la vista del elemento de la lista
            this.imgProductoDC = itemView.findViewById(R.id.imgProductoDC);
            this.btnEliminarPCarrito = itemView.findViewById(R.id.btnEliminarPCarrito);
            this.btnAdd = itemView.findViewById(R.id.btnAdd);
            this.btnDecrease = itemView.findViewById(R.id.btnDecrease);
            this.edtCantidad = itemView.findViewById(R.id.edtCantidad);
            this.tvNombreProductoDC = itemView.findViewById(R.id.tvNombreProductoDC);
            this.tvPrecioPDC = itemView.findViewById(R.id.tvPrecioPDC);
        }

        public void setItem(final DetallePedido dp) {
            // Establecer los valores de los elementos de la vista con los datos del DetallePedido
            this.tvNombreProductoDC.setText(dp.getProducto().getNombre());
            this.tvPrecioPDC.setText(String.format(Locale.ENGLISH, "€%.2f", dp.getPrecio()));
            int cant = dp.getCantidad();
            this.edtCantidad.setText(Integer.toString(cant));
            String url = ConfigApi.baseUrlE + "/api/documento-almacenado/download/" + dp.getProducto().getFoto().getFileName();

            // Cargar la imagen del producto utilizando Picasso
            Picasso picasso = new Picasso.Builder(itemView.getContext())
                    .downloader(new OkHttp3Downloader(ConfigApi.getClient()))
                    .build();
            picasso.load(url)
                    .error(R.drawable.image_not_found)
                    .into(this.imgProductoDC);

            //-------------Actualizar Cantidad del Carrito-------------------------
            btnAdd.setOnClickListener(v -> {
                if (dp.getCantidad() != 10) {//Si el valor todavía no llega a 10, que siga aumentando
                    dp.addOne();
                    ProductoCarritoAdapter.this.notifyDataSetChanged();
                }
            });

            // Manejar el evento de clic en el botón de disminuir
            btnDecrease.setOnClickListener(v -> {
                if (dp.getCantidad() != 1) {
                    // Si el valor de la cantidad no es 1, disminuir en 1
                    dp.removeOne();
                    ProductoCarritoAdapter.this.notifyDataSetChanged();
                }
            });

            //------------------Eliminar item del carrito-----------------------
            this.btnEliminarPCarrito.setOnClickListener(v -> {
                showMsg(dp.getProducto().getId());
            });
        }

        private void showMsg(int idProducto) {
            // Mostrar un cuadro de diálogo de confirmación para eliminar el producto del carrito
            new SweetAlertDialog(itemView.getContext(), SweetAlertDialog.WARNING_TYPE).setTitleText("¡Atención!")
                    .setContentText("¿Estás seguro de eliminar este producto de tu carrito de compras?")
                    .setCancelText("CANCELAR").setConfirmText("CONFIRMAR")
                    .showCancelButton(true).setCancelClickListener(sDialog -> {
                        sDialog.dismissWithAnimation();
                        // Mostrar un cuadro de diálogo de error si se cancela la operación
                        new SweetAlertDialog(itemView.getContext(), SweetAlertDialog.ERROR_TYPE).setTitleText("Operación cancelada")
                                .setContentText("No se ha eliminado ningún producto de tu carrito")
                                .show();
                    }).setConfirmClickListener(sweetAlertDialog -> {
                        // Eliminar el detalle del carrito utilizando la comunicación del carrito
                        c.eliminarDetalle(idProducto);
                        sweetAlertDialog.dismissWithAnimation();
                        // Mostrar un cuadro de diálogo de éxito después de eliminar el producto
                        new SweetAlertDialog(itemView.getContext(), SweetAlertDialog.SUCCESS_TYPE).setTitleText("¡Atención!")
                                .setContentText("El producto acaba de ser eliminado de tu carrito de compras")
                                .show();
                    }).show();
        }
    }
}
