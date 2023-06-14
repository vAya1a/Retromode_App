package org.victayagar.retromode_app.activity.ui.configuracion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.victayagar.retromode_app.R;

/*
Esta clase es un simple Fragment que muestra la vista correspondiente a la
configuración de la aplicación. Infla el diseño de la vista desde el archivo XML fragment_configuracion.xml
y devuelve la vista inflada
*/
public class ConfiguracionFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_configuracion, container, false);

    }

}