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

/*
El DocumentoAlmacenadoViewModel es una clase de ViewModel
que extiende AndroidViewModel y se utiliza para proporcionar datos
y funciones relacionadas con documentos almacenados a la interfaz de usuario.
El DocumentoAlmacenadoViewModel utiliza AndroidViewModel como clase base y recibe
una instancia de Application en su constructor. Esto permite que el
ViewModel tenga una referencia a la aplicación y se pueda acceder a ella si es necesario.
*/

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
