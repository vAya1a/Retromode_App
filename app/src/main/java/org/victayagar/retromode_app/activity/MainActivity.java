package org.victayagar.retromode_app.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;
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

import cn.pedant.SweetAlert.SweetAlertDialog;

/*
la clase MainActivity se encarga de gestionar la pantalla de inicio de sesión
de la aplicación, incluyendo la validación de credenciales, el inicio de sesión
del usuario y la navegación a otras actividades.
*/

public class MainActivity extends AppCompatActivity {

    VideoView videoLogin;

    private EditText editarMail, editarPassword;
    private Button btnLogin;
    private UsuarioViewModel viewModel;
    private TextInputLayout txtInputUsuario, txtInputPassword;
    private TextView txtNuevoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.initViewModel();
        this.init();

        videoLogin = findViewById(R.id.videoLogin);

        // Configurar el video de inicio de sesión
        String ruta = "android.resource://org.victayagar.retromode_app/" + R.raw.login;
        Uri u = Uri.parse(ruta);
        videoLogin.setVideoURI(u);
        videoLogin.start();

        videoLogin.setOnPreparedListener(mp -> mp.setLooping(true));
    }

    private void initViewModel() {
        // Inicializar el ViewModel
        viewModel = new ViewModelProvider(this).get(UsuarioViewModel.class);
    }

    private void init() {
        editarMail = findViewById(R.id.editarMail);
        editarPassword = findViewById(R.id.editarPassword);
        txtInputUsuario = findViewById(R.id.txtInputUsuario);
        txtInputPassword = findViewById(R.id.txtInputPassword);
        txtNuevoUsuario = findViewById(R.id.txtNuevoUsuario);
        btnLogin = findViewById(R.id.btnLogin);
        // Configurar el evento click del botón de inicio de sesión
        btnLogin.setOnClickListener(v -> {
            try {
                if (validar()) {
                    // Realizar el inicio de sesión
                    viewModel.login(editarMail.getText().toString(), editarPassword.getText().toString()).observe(this, response -> {
                        if (response.getRpta() == 1) {
                            toastCorrecto(response.getMessage());
                            Usuario u = response.getBody();
                            // Guardar el objeto Usuario en SharedPreferences
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                            SharedPreferences.Editor editor = preferences.edit();
                            final Gson g = new GsonBuilder()
                                    .registerTypeAdapter(Date.class, new DateSerializer())
                                    .registerTypeAdapter(Time.class, new TimeSerializer())
                                    .create();
                            editor.putString("UsuarioJson", g.toJson(u, new TypeToken<Usuario>() {
                            }.getType()));
                            editor.apply();
                            editarMail.setText("");
                            editarPassword.setText("");
                            // Abrir la actividad InicioActivity
                            startActivity(new Intent(this, InicioActivity.class));
                        } else {
                            toastIncorrecto("Datos inválidos");
                        }
                    });
                } else {
                    toastWarning("Por favor, complete todos los campos");
                }
            } catch (Exception e) {
                toastIncorrecto("Se ha producido un error al intentar iniciar sesión : " + e.getMessage());
            }
        });
        // Configurar el evento click del enlace "Nuevo usuario"
        txtNuevoUsuario.setOnClickListener(v -> {
            Intent i = new Intent(this, RegistrarUsuarioActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
        });
        // Configurar el evento click del enlace "Nuevo usuario"
        editarMail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputUsuario.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editarPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputPassword.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void toastCorrecto(String msg) {
        // Mostrar un Toast personalizado de éxito
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_toast_ok, (ViewGroup) findViewById(R.id.ll_custom_toast_ok));
        TextView txtMensaje = view.findViewById(R.id.txtMensajeToast1);
        txtMensaje.setText(msg);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }

    public void toastIncorrecto(String msg) {
        // Mostrar un Toast personalizado de error
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_toast_error, (ViewGroup) findViewById(R.id.ll_custom_toast_error));
        TextView txtMensaje = view.findViewById(R.id.txtMensajeToast2);
        txtMensaje.setText(msg);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }

    public void toastWarning(String msg) {
        // Mostrar un Toast personalizado de advertencia
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_toast_warning, (ViewGroup) findViewById(R.id.ll_custom_toast_warning));
        TextView txtMensaje = view.findViewById(R.id.txtMensajeToast3);
        txtMensaje.setText(msg);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }

    private boolean validar() {
        // Validar los campos de correo y contraseña
        boolean retorno = true;
        String usuario, password;
        usuario = editarMail.getText().toString();
        password = editarPassword.getText().toString();
        if (usuario.isEmpty()) {
            txtInputUsuario.setError("Ingrese su correo electrónico");
            retorno = false;
        } else {
            txtInputUsuario.setErrorEnabled(false);
        }
        if (password.isEmpty()) {
            txtInputPassword.setError("Ingrese su contraseña");
        } else {
            txtInputPassword.setErrorEnabled(false);
        }
        return retorno;
    }

    @Override
    public void onBackPressed() {
        // Mostrar un diálogo de confirmación al presionar el botón de retroceso
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE).setTitleText("Has pulsado atrás")
                .setContentText("¿Estás seguro de que quieres salir?")
                .setConfirmText("Cerrar").setCancelText("Cancelar")
                .showCancelButton(true).setCancelClickListener(sDialog -> {
                    sDialog.dismissWithAnimation();
                    new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("Operación cancelada")
                            .setContentText("No saliste de la app")
                            .show();
                }).setConfirmClickListener(sweetAlertDialog -> {
                    sweetAlertDialog.dismissWithAnimation();
                    System.exit(0);
                }).show();
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

