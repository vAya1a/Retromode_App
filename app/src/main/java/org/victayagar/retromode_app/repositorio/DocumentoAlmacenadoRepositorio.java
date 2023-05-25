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

public class DocumentoAlmacenadoRepositorio {
    private final DocumentoAlmacenadoApi api;
    private static DocumentoAlmacenadoRepositorio repositorio;

    public DocumentoAlmacenadoRepositorio() {
        this.api = ConfigApi.getDocumentoAlmacenadoApi();
    }
    public static DocumentoAlmacenadoRepositorio getInstance(){
        if(repositorio == null){
            repositorio = new DocumentoAlmacenadoRepositorio();
        }
        return repositorio;
    }

    public LiveData<GenericResponse<DocumentoAlmacenado>> savePhoto(MultipartBody.Part part, RequestBody requestBody){
        final MutableLiveData<GenericResponse<DocumentoAlmacenado>> mld = new MutableLiveData<>();
        this.api.save(part, requestBody).enqueue(new Callback<GenericResponse<DocumentoAlmacenado>>() {
            @Override
            public void onResponse(Call<GenericResponse<DocumentoAlmacenado>> call, Response<GenericResponse<DocumentoAlmacenado>> response) {
                mld.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<DocumentoAlmacenado>> call, Throwable t) {
                mld.setValue(new GenericResponse<>());
                System.out.println("Se ha producido un error : " + t.getMessage());
                t.printStackTrace();
            }
        });
        return mld;
    }
}
