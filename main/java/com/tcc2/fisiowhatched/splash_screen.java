package com.tcc2.fisiowhatched;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tcc2.fisiowhatched.Util.util;
import com.tcc2.fisiowhatched.ui.diario.Anotacao;

import java.util.Objects;

public class splash_screen extends AppCompatActivity implements Runnable {

    private ProgressBar progressBar;
    private Thread thread;
    private Handler handler;
    private int i;
    private TextView textView;


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.textViewspalsh);


        handler = new Handler();
        thread = new Thread(this);
        thread.start();

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_toolbar);
        TextView textView = getSupportActionBar().getCustomView().findViewById(R.id.toolbar_title);
        textView.setText("FisioWhatched");
        ImageView perfil = getSupportActionBar().getCustomView().findViewById(R.id.toolbar_image);
        ImageView voltar = getSupportActionBar().getCustomView().findViewById(R.id.toolbar_back);


        perfil.setVisibility(View.GONE);
        voltar.setVisibility(View.GONE);







    }

    @Override
    public void run() {

        i = 1;

        try {

            while(i < 100){


                Thread.sleep(50);
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        textView.setText("%" + i);
                        i++;
                        progressBar.setProgress(i);


                    }
                });
            }

            finish();
            startActivity(new Intent(splash_screen.this,Escolher_tipo_de_usuario.class));


        }catch(Exception e){


        }
    }




}

