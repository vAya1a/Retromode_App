package org.victayagar.retromode_app.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.victayagar.retromode_app.R;
import org.victayagar.retromode_app.entidad.servicio.Usuario;
import org.victayagar.retromode_app.utils.DateSerializer;
import org.victayagar.retromode_app.utils.TimeSerializer;
import org.victayagar.retromode_app.viewmodel.UsuarioViewModel;

import java.sql.Date;
import java.sql.Time;

public class MainActivity extends AppCompatActivity {

    VideoView videoLogin;

    private EditText editarMail, editarPassword;
    private Button btnLogin;
    private UsuarioViewModel viewModel;
    private TextView txtInputUsuario, txtInputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.initViewModel();
        this.init();

        videoLogin = findViewById(R.id.videoLogin);

        String ruta = "android.resource://org.victayagar.retromode_app/" + R.raw.login;
        Uri u = Uri.parse(ruta);
        videoLogin.setVideoURI(u);
        videoLogin.start();

        videoLogin.setOnPreparedListener(mp -> mp.setLooping(true));
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(UsuarioViewModel.class);
    }
    private void init(){
        editarMail = findViewById(R.id.editarMail);
        editarPassword = findViewById(R.id.editarPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(v -> {
            viewModel.login(editarMail.getText().toString(), editarPassword.getText().toString()).observe(this, response -> {
                if(response.getRpta() == 1) {
                    Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
                    Usuario u = response.getBody();
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = preferences.edit();
                    final Gson g = new GsonBuilder()
                            .registerTypeAdapter(Date.class, new DateSerializer())
                            .registerTypeAdapter(Time.class, new TimeSerializer())
                            .create();
                    editor.putString("UsuarioJson", g.toJson(u, new TypeToken<Usuario>(){
                    }.getType()));
                    editor.apply();
                    editarMail.setText("");
                    editarPassword.setText("");
                    startActivity(new Intent(this, InicioActivity.class));
                }else{
                    Toast.makeText(this, "Ocurrió un error" + response.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    protected void onResume() {
        videoLogin.resume();
        super.onResume();
    }

    @Override
    protected void onRestart() {
        videoLogin.start();
        super.onRestart();
    }

    @Override
    protected void onPause() {
        videoLogin.suspend();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        videoLogin.stopPlayback();
        super.onDestroy();
    }
}

