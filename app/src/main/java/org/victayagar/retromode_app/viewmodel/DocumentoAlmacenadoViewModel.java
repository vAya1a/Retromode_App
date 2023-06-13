package org.victayagar.retromode_app.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.victayagar.retromode_app.entidad.GenericResponse;
import org.victayagar.retromode_app.entidad.servicio.DocumentoAlmacenado;
import org.victayagar.retromode_app.repositorio.DocumentoAlmacenadoRepositorio;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class DocumentoAlmacenadoViewModel extends AndroidViewModel {
    private final DocumentoAlmacenadoRepositorio repositorio;

    public DocumentoAlmacenadoViewModel(@NonNull Application application) {
        super(application);
        this.repositorio = DocumentoAlmacenadoRepositorio.getInstance();
    }

    public LiveData<GenericResponse<DocumentoAlmacenado>> save(MultipartBody.Part part, RequestBody requestBody) {
        return this.repositorio.savePhoto(part, requestBody);
    }
}
