package org.victayagar.retromode_app.api;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.victayagar.retromode_app.utils.DateSerializer;
import org.victayagar.retromode_app.utils.TimeSerializer;

import java.sql.Date;
import java.sql.Time;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*
Esta clase ConfigApi proporciona un punto de entrada centralizado
para acceder a las interfaces de API y configurar la conexión a
la API. Si tienes más preguntas, no dudes en hacerlas.
*/

public class ConfigApi {
    // URL base de la API
    public static final String baseUrlE = "http://10.0.2.2:9090";
    private static Retrofit retrofit;
    private static String token = "";
    private static UsuarioApi usuarioApi;
    private static ClienteApi clienteApi;
    private static DocumentoAlmacenadoApi documentoAlmacenadoApi;
    private static CategoriaApi categoriaApi;
    private static ProductoApi productoApi;
    private static PedidoApi pedidoApi;

    static {
        initClient();
    }

    // Inicializa la instancia de Retrofit con la URL base y el convertidor Gson
    private static void initClient() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .registerTypeAdapter(Time.class, new TimeSerializer())
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrlE)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getClient())
                .build();
    }

    // Crea y configura un cliente OkHttpClient con los interceptores necesarios
    public static OkHttpClient getClient() {
        HttpLoggingInterceptor loggin = new HttpLoggingInterceptor();
        loggin.level(HttpLoggingInterceptor.Level.BODY);

        StethoInterceptor stetho = new StethoInterceptor();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.addInterceptor(loggin)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addNetworkInterceptor(stetho);
        return builder.build();
    }

    // Actualiza el token utilizado en las solicitudes de la API y vuelve a inicializar Retrofit
    public static void setToken(String value) {
        token = value;
        initClient();
    }

    // Devuelve una instancia de UsuarioApi
    public static UsuarioApi getUsuarioApi() {
        if (usuarioApi == null) {
            usuarioApi = retrofit.create(UsuarioApi.class);
        }
        return usuarioApi;
    }

    // Devuelve una instancia de ClienteApi
    public static ClienteApi getClienteApi() {
        if (clienteApi == null) {
            clienteApi = retrofit.create(ClienteApi.class);
        }
        return clienteApi;
    }

    // Devuelve una instancia de DocumentoAlmacenadoApi
    public static DocumentoAlmacenadoApi getDocumentoAlmacenadoApi() {
        if (documentoAlmacenadoApi == null) {
            documentoAlmacenadoApi = retrofit.create(DocumentoAlmacenadoApi.class);
        }
        return documentoAlmacenadoApi;
    }

    // Devuelve una instancia de CategoriaApi
    public static CategoriaApi getCategoriaApi() {
        if (categoriaApi == null) {
            categoriaApi = retrofit.create(CategoriaApi.class);
        }
        return categoriaApi;
    }

    // Devuelve una instancia de ProductoApi
    public static ProductoApi getProductoApi() {
        if (productoApi == null) {
            productoApi = retrofit.create(ProductoApi.class);
        }
        return productoApi;
    }

    // Devuelve una instancia de PedidoApi
    public static PedidoApi getPedidoApi() {
        if (pedidoApi == null) {
            pedidoApi = retrofit.create(PedidoApi.class);
        }
        return pedidoApi;
    }
}
