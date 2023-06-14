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

/*En esta clase DetalleProductoActivity, se realiza lo siguiente:

Se importan las clases necesarias y se definen las variables y vistas necesarias para la actividad.
Se utiliza el patrón de diseño del adaptador Gson para serializar y deserializar objetos Date y Time.
La clase extiende AppCompatActivity y anula el método onCreate para configurar y cargar la vista de la actividad.
El método init se encarga de configurar la barra de herramientas y de inicializar las vistas.
El método loadData se encarga de obtener los detalles del producto de la intención y de mostrar los datos en las vistas correspondientes.
Se utiliza la biblioteca Picasso para cargar la imagen del producto desde una URL en imgProductoDetalle.
El botón btnAgregarCarrito agrega un producto al carrito de compras al hacer clic en él.
El método successMessage muestra un mensaje de éxito utilizando SweetAlertDialog.
*/

public class DetalleProductoActivity extends AppCompatActivity {

    private ImageView imgProductoDetalle;
    private Button btnAgregarCarrito;
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

    private void init() {
        // Configuración de la barra de herramientas
        Toolbar toolbar = this.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_volveratras);
        toolbar.setNavigationOnClickListener(v -> {
            this.finish();
            this.overridePendingTransition(R.anim.rigth_in, R.anim.rigth_out);
        });

        // Inicialización de vistas
        this.imgProductoDetalle = findViewById(R.id.imgProductoDetalle);
        this.btnAgregarCarrito = findViewById(R.id.btnAgregarCarrito);
        this.tvNameProductoDetalle = findViewById(R.id.tvNameProductoDetalle);
        this.tvPrecioProductoDetalle = findViewById(R.id.tvPrecioProductoDetalle);
        this.tvDescripcionProductoDetalle = findViewById(R.id.tvDescripcionProductoDetalle);
    }

    private void loadData() {
        // Obtener los detalles del producto de la intención
        final String detalleString = this.getIntent().getStringExtra("detalleProducto");
        if (detalleString != null) {
            producto = g.fromJson(detalleString, Producto.class);

            // Establecer los datos del producto en las vistas
            this.tvNameProductoDetalle.setText(producto.getNombre());
            this.tvPrecioProductoDetalle.setText(String.format(Locale.ENGLISH, "€%.2f", producto.getPrecio()));
            this.tvDescripcionProductoDetalle.setText(producto.getDescripcionProducto());
            // Cargar la imagen del producto utilizando Picasso
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

    // Método para mostrar un mensaje de éxito utilizando SweetAlertDialog
    public void successMessage(String message) {
        new SweetAlertDialog(this,
                SweetAlertDialog.SUCCESS_TYPE).setTitleText("¡Hecho!")
                .setContentText(message).show();
    }
}