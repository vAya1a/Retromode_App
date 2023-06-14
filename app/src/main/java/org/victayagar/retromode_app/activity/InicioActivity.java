package org.victayagar.retromode_app.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import org.victayagar.retromode_app.R;
import org.victayagar.retromode_app.api.ConfigApi;
import org.victayagar.retromode_app.databinding.ActivityInicioBinding;
import org.victayagar.retromode_app.entidad.servicio.Usuario;
import org.victayagar.retromode_app.utils.DateSerializer;
import org.victayagar.retromode_app.utils.TimeSerializer;

import java.sql.Date;
import java.sql.Time;

import de.hdodenhof.circleimageview.CircleImageView;


/*
En esta clase InicioActivity, se realiza lo siguiente:

-Se importan las clases necesarias y se definen las variables y vistas necesarias para la actividad.
-La clase extiende AppCompatActivity y anula los métodos necesarios para configurar y cargar la vista de la actividad.
-Se utiliza el enlace de datos (DataBinding) para inflar y establecer la vista principal de la actividad (ActivityInicioBinding).
-Se configura la barra de herramientas (Toolbar) y el cajón de navegación (DrawerLayout y NavigationView).
-Se utiliza el componente NavController para manejar la navegación entre los fragmentos utilizando la biblioteca
de navegación de Android (Navigation y NavigationUI).
-Se implementan los métodos onCreateOptionsMenu y onOptionsItemSelected para configurar y manejar las
acciones del menú de opciones.
-El método mostrarCarrito inicia la actividad ProductosCarritoActivity al hacer clic en la opción del
carrito de compras en el menú.
-El método onStart se llama cuando la actividad se vuelve visible y se utiliza para cargar los datos del
usuario desde las preferencias compartidas y mostrarlos en las vistas correspondientes.
-El método loadData obtiene los datos del usuario serializados en formato JSON desde las preferencias compartidas,
los deserializa utilizando Gson y los muestra en las vistas correspondientes en el cajón de navegación.
-El método logout se encarga de cerrar sesión, eliminando los datos del usuario de las preferencias compartidas
y finalizando la actividad actual.
-El método onSupportNavigateUp se utiliza para habilitar la navegación hacia arriba en la barra de acciones.
*/

public class InicioActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityInicioBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityInicioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarInicio.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Pasando cada ID de menú como un conjunto de IDs porque cada
        // menú debe considerarse como destinos de nivel superior.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_inicio, R.id.nav_miscompras, R.id.nav_configuracion)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_inicio);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflar el menú; esto agrega elementos a la barra de acciones si está presente.
        getMenuInflater().inflate(R.menu.inicio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cerrarSesion:
                this.logout();
                break;
            case R.id.carritoCompra:
                this.mostrarCarrito();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void mostrarCarrito() {
        Intent i = new Intent(this, ProductosCarritoActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }

    private void loadData() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        final Gson g = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .registerTypeAdapter(Time.class, new TimeSerializer())
                .create();
        String usuarioJson = sp.getString("UsuarioJson", null);
        if (usuarioJson != null) {
            final Usuario u = g.fromJson(usuarioJson, Usuario.class);
            final View vistaHeader = binding.navView.getHeaderView(0);
            final TextView txtViewNombre = vistaHeader.findViewById(R.id.txtViewNombre),
                    txtViewCorreo = vistaHeader.findViewById(R.id.txtViewCorreo);
            final CircleImageView imgFoto = vistaHeader.findViewById(R.id.imgFotoPerfil);
            txtViewNombre.setText(u.getCliente().getNombreCompletoCliente());
            txtViewCorreo.setText(u.getEmail());
            String url = ConfigApi.baseUrlE + "/api/documento-almacenado/download/" + u.getCliente().getFoto().getFileName();
            final Picasso picasso = new Picasso.Builder(this)
                    .downloader(new OkHttp3Downloader(ConfigApi.getClient()))
                    .build();
            picasso.load(url)
                    .error(R.drawable.image_not_found)
                    .into(imgFoto);
        }
    }

    //Metodo para cerrar sesión
    private void logout() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("UsuarioJson");
        editor.apply();
        this.finish();
        this.overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_inicio);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}