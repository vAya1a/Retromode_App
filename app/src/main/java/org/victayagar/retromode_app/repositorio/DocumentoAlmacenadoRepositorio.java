package org.victayagar.retromode_app.repositorio;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.victayagar.retromode_app.api.ConfigApi;
import org.victayagar.retromode_app.api.DocumentoAlmacenadoApi;
import org.victayagar.retromode_app.entidad.GenericResponse;
import org.victayagar.retromode_app.entidad.servicio.DocumentoAlmacenado;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
El repositorio DocumentoAlmacenadoRepositorio se encarga de
interactuar con la API de documentos almacenados para guardar una foto.
*/

public class DocumentoAlmacenadoRepositorio {
    private final DocumentoAlmacenadoApi api;
    private static DocumentoAlmacenadoRepositorio repositorio;

    public DocumentoAlmacenadoRepositorio() {
        this.api = ConfigApi.getDocumentoAlmacenadoApi();
    }

    public static DocumentoAlmacenadoRepositorio getInstance() {
        if (repositorio == null) {
            repositorio = new DocumentoAlmacenadoRepositorio();
        }
        return repositorio;
    }

    public LiveData<GenericResponse<DocumentoAlmacenado>> savePhoto(MultipartBody.Part part, RequestBody requestBody) {
        // Creamos un MutableLiveData para almacenar la respuesta de la API
        final MutableLiveData<GenericResponse<DocumentoAlmacenado>> mld = new MutableLiveData<>();
        // Realizamos la llamada a la API para guardar la foto
        this.api.save(part, requestBody).enqueue(new Callback<GenericResponse<DocumentoAlmacenado>>() {
            @Override
            public void onResponse(Call<GenericResponse<DocumentoAlmacenado>> call, Response<GenericResponse<DocumentoAlmacenado>> response) {
                // En caso de respuesta exitosa, establecemos el valor del MutableLiveData con la respuesta de la API
                mld.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<DocumentoAlmacenado>> call, Throwable t) {
                // En caso de error, mostramos un mensaje por consola, establecemos un GenericResponse vacío en el MutableLiveData y
                // imprimimos el stack trace de la excepción
                mld.setValue(new GenericResponse<>());
                System.out.println("Se ha producido un error : " + t.getMessage());
                t.printStackTrace();
            }
        });
        // Devolvemos el MutableLiveData como LiveData para que los observadores puedan recibir actualizaciones
        return mld;
    }
}
