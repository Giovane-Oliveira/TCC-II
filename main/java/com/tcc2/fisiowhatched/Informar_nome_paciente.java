package com.tcc2.fisiowhatched;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tcc2.fisiowhatched.Util.util;
import com.tcc2.fisiowhatched.ui.Pacientes.Paciente;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Informar_nome_paciente extends AppCompatActivity {

    private EditText editText;
    private EditText sobrenome;
    private EditText cpf;
    private EditText datanasc;
    private Button btn;
    Calendar myCalendar = Calendar.getInstance();


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informar_nome_paciente);

        editText = findViewById(R.id.infonamepaciente);
        sobrenome = findViewById(R.id.edt_sobrenome);
        btn = findViewById(R.id.btninfopaciente);
        cpf = findViewById(R.id.edtcpf_profile);
        datanasc = findViewById(R.id.edtdatanasc_profile);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_toolbar);
        TextView textView = getSupportActionBar().getCustomView().findViewById(R.id.toolbar_title);
        textView.setText("Dados Complementares");
        ImageView perfil = getSupportActionBar().getCustomView().findViewById(R.id.toolbar_image);
        ImageView voltar = getSupportActionBar().getCustomView().findViewById(R.id.toolbar_back);


        perfil.setVisibility(View.GONE);
        voltar.setVisibility(View.GONE);



try{


    final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        private void updateLabel() {

            String myFormat = "dd/MM/yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt", "BR"));

            datanasc.setText(sdf.format(myCalendar.getTime()));
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    datanasc.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            new DatePickerDialog(Informar_nome_paciente.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        }
    });





}catch (Exception e){


    Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show();


}


        Bundle bundle = getIntent().getExtras();

        assert bundle != null;
        final int acao = bundle.getInt("CADASTRAR");

        if(acao == 2){

            editText.setHint("Informe seu nome");
            sobrenome.setHint("Informe seu sobrenome");
            cpf.setHint("Informe seu cpf");
            datanasc.setHint("Informe sua data de nascimento");


        }else{

            editText.setHint("Informe o nome do paciente");
            sobrenome.setHint("Informe o sobre do paciente");
            cpf.setHint("Informe o cpf do paciente");
            datanasc.setHint("Data de nascimento do paciente");

        }



            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    try {


                        if (util.verificarCampos(Informar_nome_paciente.this, editText.getText().toString(), sobrenome.getText().toString())) {

                           if(cpf.length() == 11){

                               userprofile(acao);

                            }else{

                               AlertDialog.Builder dig = new AlertDialog.Builder(Informar_nome_paciente.this);
                               dig.setTitle("Aviso!");
                               dig.setMessage("Informe os 11 números do seu cpf.");
                               dig.setNeutralButton("ok", null);
                               dig.show();
                           }


                        }else{

                            AlertDialog.Builder dig = new AlertDialog.Builder(Informar_nome_paciente.this);
                            dig.setTitle("Aviso!");
                            dig.setMessage("Preencha os campos!");
                            dig.setNeutralButton("ok", null);
                            dig.show();

                        }



                    } catch (Exception e) {

                        AlertDialog.Builder dig = new AlertDialog.Builder(Informar_nome_paciente.this);
                        dig.setTitle("Aviso!");
                        dig.setMessage("" + e);
                        dig.setNeutralButton("ok", null);
                        dig.show();

                    }


                }
            });







    }




    private void userprofile(final int acao) {

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

           UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                    .setDisplayName("" + editText.getText().toString() + " " + "" + sobrenome.getText().toString()).build() ;

            user.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {

                        gravardados(acao);

                    }
                }
            });


        }





    private void gravardados(int acao) {


        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        final String userEmail = user.getEmail();
        final String userID = user.getUid();
        final String userName = user.getDisplayName();

  if(acao == 2){

      DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("db").child("Fisioterapeuta").child(userID);


      Paciente paciente = new Paciente(cpf.getText().toString(), datanasc.getText().toString(), userEmail, userID, userName, "sem foto");


      databaseReference.setValue(paciente).addOnCompleteListener(new OnCompleteListener<Void>() {
          @Override
          public void onComplete(@NonNull Task<Void> task) {
              if (task.isSuccessful()) {

                  Intent intent = new Intent(Informar_nome_paciente.this, Main2Activity.class);
                  startActivity(intent);
                  finish();


              }

          }

      });

  }else if(acao == 0){


      DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("db").child("Users").child(userID);


      Paciente paciente = new Paciente(cpf.getText().toString(), datanasc.getText().toString(), userEmail, userID, userName, "sem foto");


      databaseReference.setValue(paciente).addOnCompleteListener(new OnCompleteListener<Void>() {
          @Override
          public void onComplete(@NonNull Task<Void> task) {
              if (task.isSuccessful()) {

                  Intent intent = new Intent(Informar_nome_paciente.this, MainActivity.class);
                  startActivity(intent);
                  finish();


              }

          }

      });


  }




        }

    }

