package org.victayagar.retromode_app.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;
import org.victayagar.retromode_app.R;
import org.victayagar.retromode_app.entidad.servicio.Cliente;
import org.victayagar.retromode_app.entidad.servicio.DocumentoAlmacenado;
import org.victayagar.retromode_app.entidad.servicio.Usuario;
import org.victayagar.retromode_app.viewmodel.ClienteViewModel;
import org.victayagar.retromode_app.viewmodel.DocumentoAlmacenadoViewModel;
import org.victayagar.retromode_app.viewmodel.UsuarioViewModel;

import java.io.File;
import java.time.LocalDateTime;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RegistrarUsuarioActivity extends AppCompatActivity {
    private File f;
    private ClienteViewModel clienteViewModel;
    private UsuarioViewModel usuarioViewModel;
    private DocumentoAlmacenadoViewModel documentoAlmacenadoViewModel;
    private Button btnSubirImagen, btnGuardarDatos;
    private CircleImageView imageUser;
    private AutoCompleteTextView dropdownTipoDoc, dropdownProvincia;
    private EditText edtNameUser, edtPrimerApellidoU, edtSegundoApellidoU, edtNumDocU, edtCiudadU, edtTelefonoU, edtDireccionU, edtPasswordUser, edtEmailUser;
    private TextInputLayout txtInputNameUser, txtInputPrimerApellidoU, txtInputSegundoApellidoU,
            txtInputTipoDoc, txtInputNumeroDocU, txtInputCiudad, txtInputProvincia,
            txtInputTelefonoU, txtInputDireccionU, txtInputEmailUser, txtInputPasswordUser;
    private final static int LOCATION_REQUEST_CODE = 23;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);
        this.init();
        this.initViewModel();
        this.spinners();
    }

    private void spinners() {
        //LISTA DE TIPOS DE DOCUMENTOS
        String[] tipoDoc = getResources().getStringArray(R.array.tipoDoc);
        ArrayAdapter arrayTipoDoc = new ArrayAdapter(this, R.layout.dropdown_item, tipoDoc);
        dropdownTipoDoc.setAdapter(arrayTipoDoc);
        //LISTA DE PROVINCIAS
        String[] provincias = getResources().getStringArray(R.array.provincias);
        ArrayAdapter arrayProvincias = new ArrayAdapter(this, R.layout.dropdown_item, provincias);
        dropdownProvincia.setAdapter(arrayProvincias);

    }

    private void initViewModel() {
        final ViewModelProvider vmp = new ViewModelProvider(this);
        this.clienteViewModel = vmp.get(ClienteViewModel.class);
        this.usuarioViewModel = vmp.get(UsuarioViewModel.class);
        this.documentoAlmacenadoViewModel = vmp.get(DocumentoAlmacenadoViewModel.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    LOCATION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Gracias por conceder los permisos para " +
                            "leer el almacenamiento, estos permisos son necesarios para poder " +
                            "escoger tu foto de perfil", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "No podemos realizar el registro si no nos concedes los permisos para leer el almacenamiento.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void init() {
        btnGuardarDatos = findViewById(R.id.btnGuardarDatos);
        btnSubirImagen = findViewById(R.id.btnSubirImagen);
        imageUser = findViewById(R.id.imageUser);
        edtNameUser = findViewById(R.id.edtNameUser);
        edtPrimerApellidoU = findViewById(R.id.edtPrimerApellidoU);
        edtSegundoApellidoU = findViewById(R.id.edtSegundoApellidoU);
        edtNumDocU = findViewById(R.id.edtNumDocU);
        edtCiudadU = findViewById(R.id.edtCiudadU);
        edtTelefonoU = findViewById(R.id.edtTelefonoU);
        edtDireccionU = findViewById(R.id.edtDireccionU);
        edtPasswordUser = findViewById(R.id.edtPasswordUser);
        edtEmailUser = findViewById(R.id.edtEmailUser);
        //AutoCompleteTextView
        dropdownTipoDoc = findViewById(R.id.dropdownTipoDoc);
        dropdownProvincia = findViewById(R.id.dropdownProvincia);
        //TextInputLayout
        txtInputNameUser = findViewById(R.id.txtInputNameUser);
        txtInputPrimerApellidoU = findViewById(R.id.txtInputPrimerApellidoU);
        txtInputSegundoApellidoU = findViewById(R.id.txtInputSegundoApellidoU);
        txtInputTipoDoc = findViewById(R.id.txtInputTipoDoc);
        txtInputNumeroDocU = findViewById(R.id.txtInputNumeroDocU);
        txtInputCiudad = findViewById(R.id.txtInputCiudad);
        txtInputProvincia = findViewById(R.id.txtInputProvincia);
        txtInputTelefonoU = findViewById(R.id.txtInputTelefonoU);
        txtInputDireccionU = findViewById(R.id.txtInputDireccionU);
        txtInputEmailUser = findViewById(R.id.txtInputEmailUser);
        txtInputPasswordUser = findViewById(R.id.txtInputPasswordUser);
        btnSubirImagen.setOnClickListener(v -> this.cargarImagen());
        btnGuardarDatos.setOnClickListener(v -> this.guardarDatos());
        ///ONCHANGE LISTENER A LOS EDITEXT
        edtNameUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputNameUser.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtPrimerApellidoU.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputPrimerApellidoU.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtSegundoApellidoU.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputSegundoApellidoU.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtNumDocU.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputNumeroDocU.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtCiudadU.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputCiudad.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtTelefonoU.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputTelefonoU.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtDireccionU.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputDireccionU.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        dropdownTipoDoc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputTipoDoc.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        dropdownProvincia.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtInputProvincia.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void guardarDatos() {
        Cliente c;
        if (validar()) {
            c = new Cliente();
            try {
                c.setNombre(edtNameUser.getText().toString());
                c.setPrimerApellido(edtPrimerApellidoU.getText().toString());
                c.setSegundoApellido(edtSegundoApellidoU.getText().toString());
                c.setTipoDoc(dropdownTipoDoc.getText().toString());
                c.setNumDoc(edtNumDocU.getText().toString());
                c.setCiudad(edtCiudadU.getText().toString());
                c.setProvincia(dropdownProvincia.getText().toString());
                c.setTelefono(edtTelefonoU.getText().toString());
                c.setDireccionEnvio(edtDireccionU.getText().toString());
                c.setId(0);
                LocalDateTime ldt = LocalDateTime.now(); //Para generar el nombre al archivo en base a la fecha, hora, año
                RequestBody rb = RequestBody.create(f, MediaType.parse("multipart/form-data")), somedata; //Le estamos enviando un archivo (imagen) desde el formulario
                String filename = "" + ldt.getDayOfMonth() + (ldt.getMonthValue() + 1) +
                        ldt.getYear() + ldt.getHour()
                        + ldt.getMinute() + ldt.getSecond(); //Asignar un nombre al archivo (imagen)
                MultipartBody.Part part = MultipartBody.Part.createFormData("file", f.getName(), rb);
                somedata = RequestBody.create("profilePh" + filename, MediaType.parse("text/plain")); //Le estamos enviando un nombre al archivo.
                this.documentoAlmacenadoViewModel.save(part, somedata).observe(this, response -> {
                    if (response.getRpta() == 1) {
                        c.setFoto(new DocumentoAlmacenado());
                        c.getFoto().setId(response.getBody().getId());//Asignamos la foto al cliente
                        this.clienteViewModel.guardarCliente(c).observe(this, cResponse -> {
                            if (cResponse.getRpta() == 1) {
                                int idc = cResponse.getBody().getId();//Obtener el id del cliente.
                                Usuario u = new Usuario();
                                u.setEmail(edtEmailUser.getText().toString());
                                u.setClave(edtPasswordUser.getText().toString());
                                u.setVigencia(true);
                                u.setCliente(new Cliente(idc));
                                this.usuarioViewModel.save(u).observe(this, uResponse -> {
                                    if (uResponse.getRpta() == 1) {
                                        c.setFoto(null);
                                        successMessage("¡Genial!, " + "¡Bienvenido a Retromode!");
                                    } else {
                                        toastIncorrecto("No se ha podido guardar los datos, intentelo de nuevo");
                                    }
                                });
                            } else {
                                toastIncorrecto("No se ha podido guardar los datos, intentelo de nuevo");
                            }
                        });
                    } else {
                        toastIncorrecto("No se ha podido guardar los datos, intentelo de nuevo");
                    }
                });
            } catch (Exception e) {
                warningMessage("Se ha producido un error : " + e.getMessage());
            }
        } else {
            errorMessage("Por favor, complete todos los campos del formulario");
        }
    }

    private void cargarImagen() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/");
        startActivityForResult(Intent.createChooser(i, "Seleccione la Aplicación"), 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            final String realPath = getRealPathFromURI(uri);
            this.f = new File(realPath);
            this.imageUser.setImageURI(uri);
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        String result;
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            result = contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    private boolean validar() {
        boolean retorno = true;
        String nombre, primerApellido, segundoApellido, numDoc, telefono, ciudad, direccion, correo, clave,
                dropTipoDoc, dropProvincia;
        nombre = edtNameUser.getText().toString();
        primerApellido = edtPrimerApellidoU.getText().toString();
        segundoApellido = edtSegundoApellidoU.getText().toString();
        numDoc = edtNumDocU.getText().toString();
        ciudad = edtCiudadU.getText().toString();
        telefono = edtTelefonoU.getText().toString();
        direccion = edtDireccionU.getText().toString();
        correo = edtEmailUser.getText().toString();
        clave = edtPasswordUser.getText().toString();
        dropTipoDoc = dropdownTipoDoc.getText().toString();
        dropProvincia = dropdownProvincia.getText().toString();
        if (this.f == null) {
            errorMessage("Debe selecionar una foto de perfil");
            retorno = false;
        }
        if (nombre.isEmpty()) {
            txtInputNameUser.setError("Ingrese nombre");
            retorno = false;
        } else {
            txtInputNameUser.setErrorEnabled(false);
        }
        if (primerApellido.isEmpty()) {
            txtInputPrimerApellidoU.setError("Ingrese primer apellido");
            retorno = false;
        } else {
            txtInputPrimerApellidoU.setErrorEnabled(false);
        }
        if (segundoApellido.isEmpty()) {
            txtInputSegundoApellidoU.setError("Ingrese segundo apellido");
            retorno = false;
        } else {
            txtInputSegundoApellidoU.setErrorEnabled(false);
        }
        if (numDoc.isEmpty()) {
            txtInputNumeroDocU.setError("Ingrese nº documento");
            retorno = false;
        } else {
            txtInputNumeroDocU.setErrorEnabled(false);
        }
        if (ciudad.isEmpty()) {
            txtInputCiudad.setError("Ingrese ciudad");
            retorno = false;
        } else {
            txtInputCiudad.setErrorEnabled(false);
        }
        if (telefono.isEmpty()) {
            txtInputTelefonoU.setError("Ingrese nº telefónico");
            retorno = false;
        } else {
            txtInputTelefonoU.setErrorEnabled(false);
        }
        if (direccion.isEmpty()) {
            txtInputDireccionU.setError("Ingrese dirección de su casa");
            retorno = false;
        } else {
            txtInputDireccionU.setErrorEnabled(false);
        }
        if (correo.isEmpty()) {
            txtInputEmailUser.setError("Ingrese correo electrónico");
            retorno = false;
        } else {
            txtInputEmailUser.setErrorEnabled(false);
        }
        if (clave.isEmpty()) {
            txtInputPasswordUser.setError("Ingrese clave para el inicio de sesión");
            retorno = false;
        } else {
            txtInputPasswordUser.setErrorEnabled(false);
        }
        if (dropTipoDoc.isEmpty()) {
            txtInputTipoDoc.setError("Selecciona el Tipo doc.");
            retorno = false;
        } else {
            txtInputTipoDoc.setErrorEnabled(false);
        }
        if (dropProvincia.isEmpty()) {
            txtInputProvincia.setError("Seleccione provincia");
            retorno = false;
        } else {
            txtInputProvincia.setErrorEnabled(false);
        }
        return retorno;
    }

    public void successMessage(String message) {
        new SweetAlertDialog(this,
                SweetAlertDialog.SUCCESS_TYPE).setTitleText("¡Hecho!")
                .setContentText(message).show();
        edtNameUser.setText("");
        edtPasswordUser.setText("");
        edtPrimerApellidoU.setText("");
        edtSegundoApellidoU.setText("");
        edtNumDocU.setText("");
        edtCiudadU.setText("");
        edtDireccionU.setText("");
        edtTelefonoU.setText("");
        edtEmailUser.setText("");
        dropdownTipoDoc.setText("");
        dropdownProvincia.setText("");
    }

    public void errorMessage(String message) {
        new SweetAlertDialog(this,
                SweetAlertDialog.ERROR_TYPE).setTitleText("Ups...").setContentText(message).show();
    }

    public void warningMessage(String message) {
        new SweetAlertDialog(this,
                SweetAlertDialog.WARNING_TYPE).setTitleText("Notificación del Sistema")
                .setContentText(message).setConfirmText("OK").show();
    }

    public void toastIncorrecto(String msg) {
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
}