package org.victayagar.retromode_app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import org.victayagar.retromode_app.R;
import org.victayagar.retromode_app.api.ConfigApi;
import org.victayagar.retromode_app.entidad.servicio.DetallePedido;
import org.victayagar.retromode_app.entidad.servicio.Producto;
import org.victayagar.retromode_app.utils.Carrito;
import org.victayagar.retromode_app.utils.DateSerializer;
import org.victayagar.retromode_app.utils.TimeSerializer;

import java.sql.Date;
import java.sql.Time;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DetalleProductoActivity extends AppCompatActivity {

    private ImageView imgProductoDetalle;
    private Button btnAgregarCarrito, btnComprar;
    private TextView tvNameProductoDetalle, tvPrecioProductoDetalle, tvDescripcionProductoDetalle;
    final Gson g = new GsonBuilder()
            .registerTypeAdapter(Date.class, new DateSerializer())
            .registerTypeAdapter(Time.class, new TimeSerializer())
            .create();
    Producto producto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_producto);
        init();
        loadData();
    }

    private void init(){
        Toolbar toolbar = this.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_volveratras);
        toolbar.setNavigationOnClickListener(v -> {
            this.finish();
            this.overridePendingTransition(R.anim.rigth_in, R.anim.rigth_out);
        });
        this.imgProductoDetalle = findViewById(R.id.imgProductoDetalle);
        this.btnAgregarCarrito = findViewById(R.id.btnAgregarCarrito);
        this.btnComprar = findViewById(R.id.btnComprar);
        this.tvNameProductoDetalle = findViewById(R.id.tvNameProductoDetalle);
        this.tvPrecioProductoDetalle = findViewById(R.id.tvPrecioProductoDetalle);
        this.tvDescripcionProductoDetalle = findViewById(R.id.tvDescripcionProductoDetalle);
    }
    private void loadData() {
        final String detalleString = this.getIntent().getStringExtra("detalleProducto");
        if (detalleString != null) {
            producto = g.fromJson(detalleString, Producto.class);
            this.tvNameProductoDetalle.setText(producto.getNombre());
            this.tvPrecioProductoDetalle.setText(String.format(Locale.ENGLISH, "€%.2f", producto.getPrecio()));
            this.tvDescripcionProductoDetalle.setText(producto.getDescripcionProducto());
            String url = ConfigApi.baseUrlE + "/api/documento-almacenado/download/" + producto.getFoto().getFileName();

            Picasso picasso = new Picasso.Builder(this)
                    .downloader(new OkHttp3Downloader(ConfigApi.getClient()))
                    .build();
            picasso.load(url)
                    .error(R.drawable.image_not_found)
                    .into(this.imgProductoDetalle);
        } else {
            System.out.println("Error al obtener los detalles del producto");
        }
        //Agregar productos al carrito
        this.btnAgregarCarrito.setOnClickListener(v -> {
            DetallePedido detallePedido = new DetallePedido();
            detallePedido.setProducto(producto);
            detallePedido.setCantidad(1);
            detallePedido.setPrecio(producto.getPrecio());
            successMessage(Carrito.agregarProductos(detallePedido));
        });
    }
    public void successMessage(String message) {
        new SweetAlertDialog(this,
                SweetAlertDialog.SUCCESS_TYPE).setTitleText("¡Hecho!")
                .setContentText(message).show();
    }
}