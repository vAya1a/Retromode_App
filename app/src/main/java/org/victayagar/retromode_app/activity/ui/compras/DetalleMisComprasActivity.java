package org.victayagar.retromode_app.activity.ui.compras;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.victayagar.retromode_app.R;
import org.victayagar.retromode_app.adapter.DetalleMisComprasAdapter;
import org.victayagar.retromode_app.entidad.servicio.DetallePedido;
import org.victayagar.retromode_app.utils.DateSerializer;
import org.victayagar.retromode_app.utils.TimeSerializer;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;


/* Esta clase llamada DetalleMisComprasActivity extiende AppCompatActivity y se encarga de mostrar los detalles de las
compras realizadas. Aquí están las funciones principales:

onCreate(): Es el método de entrada de la actividad, donde se establece el diseño de la interfaz de usuario y se
llaman a las funciones de inicialización del adaptador y carga de datos.
init(): Inicializa las vistas y la barra de herramientas, configurando el RecyclerView y el botón de retroceso.
initAdapter(): Inicializa el adaptador y lo asigna al RecyclerView.
loadData(): Carga los detalles de las compras a partir de una cadena JSON recibida a través de un intento. Utiliza la
biblioteca Gson para deserializar la cadena JSON en una lista de objetos DetallePedido y actualiza el adaptador con estos detalles.
onBackPressed(): Maneja el evento de retroceso (back), finalizando la actividad y animando la transición de salida.
*/


public class DetalleMisComprasActivity extends AppCompatActivity {
    private RecyclerView rcvDetalleMisCompras;
    private DetalleMisComprasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_mis_compras);
        init();
        initAdapter();
        loadData();
    }

    // Inicializa las vistas y la barra de herramientas
    private void init() {
        rcvDetalleMisCompras = findViewById(R.id.rcvDetalleMisCompras);
        rcvDetalleMisCompras.setLayoutManager(new GridLayoutManager(this, 1));
        Toolbar toolbar = this.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_volveratras);
        toolbar.setNavigationOnClickListener(v -> this.onBackPressed());
    }

    // Inicializa el adaptador y lo asigna al RecyclerView
    private void initAdapter() {
        adapter = new DetalleMisComprasAdapter(new ArrayList<>());
        rcvDetalleMisCompras.setAdapter(adapter);
    }

    // Carga los datos de los detalles de las compras
    private void loadData() {
        final String detalleString = this.getIntent().getStringExtra("detailsPurchases");
        if (detalleString != null) {
            final Gson g = new GsonBuilder()
                    .registerTypeAdapter(Date.class, new DateSerializer())
                    .registerTypeAdapter(Time.class, new TimeSerializer())
                    .create();
            List<DetallePedido> detalles = g.fromJson(detalleString,
                    new TypeToken<List<DetallePedido>>() {
                    }.getType());
            adapter.updateItems(detalles);
        }
    }


    // Maneja el evento de retroceso (back)
    @Override
    public void onBackPressed() {
        this.finish();
        this.overridePendingTransition(R.anim.down_in, R.anim.down_out);
    }
}