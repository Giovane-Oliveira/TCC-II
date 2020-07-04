package com.tcc2.fisiowhatched;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tcc2.fisiowhatched.Util.util;
import com.tcc2.fisiowhatched.ui.Pacientes.Perfil_paciente;


import java.util.Objects;

public class Escolher_tipo_de_usuario extends AppCompatActivity {
    private ImageButton paciente, fisioterapeuta;
    private TextView desc_paciente, desc_fisioterapeuta;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escolher_tipo_de_usuario);

        paciente =  findViewById(R.id.btn_paciente);
        fisioterapeuta = findViewById(R.id.btn_fisioterapeuta);
        desc_paciente = findViewById(R.id.descricaopaciente);
        desc_fisioterapeuta = findViewById(R.id.descricaofisioterapia);


        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_toolbar);
        TextView textView = getSupportActionBar().getCustomView().findViewById(R.id.toolbar_title);
        textView.setText("Escolha o usuário");
        ImageView perfil = getSupportActionBar().getCustomView().findViewById(R.id.toolbar_image);
        ImageView voltar = getSupportActionBar().getCustomView().findViewById(R.id.toolbar_back);


      perfil.setVisibility(View.GONE);
      voltar.setVisibility(View.GONE);










verificarusuario();




        paciente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getBaseContext(), Tela_login.class);

                intent.putExtra("tipo",1);

                startActivity(intent);




            }
        });

        fisioterapeuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(getBaseContext(), Tela_login.class);

                intent.putExtra("tipo",2);

                startActivity(intent);





            }
        });

    }


    public void verificarusuario(){



            if(util.isNetworkAvailable(Objects.requireNonNull(this))){

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if(user != null){

                    paciente.setVisibility(View.GONE);
                    fisioterapeuta.setVisibility(View.GONE);
                    desc_paciente.setVisibility(View.GONE);
                    desc_fisioterapeuta.setVisibility(View.GONE);

                    final String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();


                Query query = FirebaseDatabase.getInstance().getReference().child("db").child("Users").child(userID).child("cpf");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists()){

                            finish();
                            startActivity(new Intent(Escolher_tipo_de_usuario.this,MainActivity.class));

                        }else{

                         /*   finish();
                            startActivity(new Intent(Escolher_tipo_de_usuario.this,Main2Activity.class));*/

                            Query query = FirebaseDatabase.getInstance().getReference().child("db").child("Fisioterapeuta").child(userID).child("cpf");
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if(dataSnapshot.exists()){

                                        finish();
                                        startActivity(new Intent(Escolher_tipo_de_usuario.this,Main2Activity.class));

                                    }else{

                                       FirebaseAuth.getInstance().signOut();
                                        startActivity(new Intent(Escolher_tipo_de_usuario.this,splash_screen.class));


                                    }


                                }





                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.e("Realtime", "onCancelled", databaseError.toException());
                                }
                            });

                        }


                    }





                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("Realtime", "onCancelled", databaseError.toException());
                    }
                });

            }else{

                    paciente.setVisibility(View.VISIBLE);
                    fisioterapeuta.setVisibility(View.VISIBLE);
                    desc_paciente.setVisibility(View.VISIBLE);
                    desc_fisioterapeuta.setVisibility(View.VISIBLE);

                }

        }else{

                AlertDialog.Builder dig = new AlertDialog.Builder(this);
                dig.setMessage("Sem conexão com a internet :(");
                dig.setNeutralButton("ok", null);
                dig.show();

            }










    }
}
