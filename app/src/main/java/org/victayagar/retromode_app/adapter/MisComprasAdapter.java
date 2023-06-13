package org.victayagar.retromode_app.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.victayagar.retromode_app.R;
import org.victayagar.retromode_app.activity.ui.compras.DetalleMisComprasActivity;
import org.victayagar.retromode_app.communication.AnularPedidoCommunication;
import org.victayagar.retromode_app.communication.Communication;
import org.victayagar.retromode_app.entidad.servicio.dto.PedidoConDetallesDTO;
import org.victayagar.retromode_app.utils.DateSerializer;
import org.victayagar.retromode_app.utils.TimeSerializer;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MisComprasAdapter extends RecyclerView.Adapter<MisComprasAdapter.ViewHolder> {
    private final List<PedidoConDetallesDTO> pedidos;
    private final Communication communication;
    private final AnularPedidoCommunication anularPedidoCommunication;

    public MisComprasAdapter(List<PedidoConDetallesDTO> pedidos, Communication communication, AnularPedidoCommunication anularPedidoCommunication) {
        this.pedidos = pedidos;
        this.communication = communication;
        this.anularPedidoCommunication = anularPedidoCommunication;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mis_compras, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setItem(this.pedidos.get(position));
    }

    @Override
    public int getItemCount() {
        return this.pedidos.size();
    }
    public void updateItems(List<PedidoConDetallesDTO> pedido){
        this.pedidos.clear();
        this.pedidos.addAll(pedido);
        this.notifyDataSetChanged();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setItem(final PedidoConDetallesDTO dto) {
            final TextView txtValueRefCompra = this.itemView.findViewById(R.id.txtValueRefCompra),
                    txtValueFechaCompra = this.itemView.findViewById(R.id.txtValueFechaCompra),
                    txtValuePrecioTotal = this.itemView.findViewById(R.id.txtValuePrecioTotal),
                    txtValueEstadoPedido = this.itemView.findViewById(R.id.txtValueEstadoPedido);
            final Button btnDescargarFactura = this.itemView.findViewById(R.id.btnDescargarFactura);
            txtValueRefCompra.setText("C000" + Integer.toString(dto.getPedido().getId()));
            txtValueFechaCompra.setText((dto.getPedido().getFechaCompra()).toString());
            txtValuePrecioTotal.setText(String.format(Locale.ENGLISH, "€%.2f", dto.getPedido().getMonto()));
            txtValueEstadoPedido.setText(dto.getPedido().isAnularPedido() ? "Pedido cancelado" : "Preparando, esperando envio...");

            itemView.setOnClickListener(v -> {
                final Intent i = new Intent(itemView.getContext(), DetalleMisComprasActivity.class);
                final Gson g;
                g = new GsonBuilder()
                        .registerTypeAdapter(Date.class, new DateSerializer())
                        .registerTypeAdapter(Time.class, new TimeSerializer())
                        .create();
                i.putExtra("detailsPurchases", g.toJson(dto.getDetallePedido()));
                communication.showDetails(i);//Esto es solo para dar una animación.
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    anularPedido(dto.getPedido().getId());
                    return false;
                }
            });
            btnDescargarFactura.setOnClickListener(view -> {
                int idCli = dto.getPedido().getCliente().getId();
                int idOrden = dto.getPedido().getId();
                String fileName = "Factura" + "00" + dto.getPedido().getId() + ".pdf";
                communication.exportInvoice(idCli, idOrden, fileName);
            });
        }
        private void anularPedido(int id) {
            new SweetAlertDialog(itemView.getContext(), SweetAlertDialog.WARNING_TYPE).setTitleText("¡Aviso!")
                    .setContentText("¿Estás seguro de cancelar el pedido solicitado? Una vez cancelado no podrás deshacer los cambios...")
                    .setCancelText("VOLVER ATRÁS").setConfirmText("CANCELAR PEDIDO")
                    .showCancelButton(true)
                    .setConfirmClickListener(sDialog -> {
                        sDialog.dismissWithAnimation();
                        new SweetAlertDialog(itemView.getContext(), SweetAlertDialog.SUCCESS_TYPE).setTitleText("¡Hecho!")
                                .setContentText(anularPedidoCommunication.anularPedido(id))
                                .show();
                    }).setCancelClickListener(sDialog -> {
                        sDialog.dismissWithAnimation();
                        new SweetAlertDialog(itemView.getContext(), SweetAlertDialog.ERROR_TYPE).setTitleText("¡Aviso!")
                                .setContentText("No se canceló ningún pedido")
                                .show();
                    }).show();
        }
    }
}
