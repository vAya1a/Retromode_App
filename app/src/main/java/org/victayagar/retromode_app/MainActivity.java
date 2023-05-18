package org.victayagar.retromode_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    VideoView videoLogin;
    Button inicioSesion;
    MediaPlayer intro;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoLogin = (VideoView) findViewById(R.id.videoLogin);
        intro = MediaPlayer.create(this, R.raw.intro);

        intro.start();

        String ruta ="android.resource://org.victayagar.retromode_app/"+R.raw.login;
        Uri u = Uri.parse(ruta);
        videoLogin.setVideoURI(u);
        videoLogin.start();

        videoLogin.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
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

