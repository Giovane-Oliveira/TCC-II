package com.tcc2.fisiowhatched.ui.diario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.tcc2.fisiowhatched.MainActivity;
import com.tcc2.fisiowhatched.R;
import com.tcc2.fisiowhatched.Util.DialogAlerta;
import com.tcc2.fisiowhatched.Util.DialogProgress;
import com.tcc2.fisiowhatched.Util.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;


public class Gravar_dados_diario extends Fragment {
    private FirebaseAuth auth;
    private EditText data;
    private EditText titulo;
    private EditText anotacao;

    private Button enviar;
    private FirebaseDatabase firebaseDatabase;
    private boolean firebaseoffline = false;
    private DialogProgress progress;
    Calendar myCalendar = Calendar.getInstance();



    @SuppressLint("WrongConstant")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_gravar_dados_diario_activity, container, false);

        auth = FirebaseAuth.getInstance();

        data =  root.findViewById(R.id.datanovaanotacaoedt);
        titulo = root.findViewById(R.id.titulonovaanotacao);
        anotacao = root.findViewById(R.id.notadiarioedt);
        enviar = root.findViewById(R.id.btn_enviar_anotacao);


        ((MainActivity)getActivity()).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ((MainActivity)getActivity()).getSupportActionBar().setCustomView(R.layout.custom_toolbar);
        TextView textView = ((MainActivity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.toolbar_title);
        textView.setText("Nova Nota");
        ImageView perfil = ((MainActivity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.toolbar_image);
        ImageView voltar = ((MainActivity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.toolbar_back);


        // voltar.setVisibility(View.GONE);

        Uri url = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhotoUrl();

        if (url != null) {

            Picasso.with(getContext()).load(url).into(perfil, new com.squareup.picasso.Callback() {

                @Override
                public void onSuccess() {



                }

                @Override
                public void onError() {



                }
            });


        }


        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragmentA = new diario_menu();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment2, fragmentA, "Fragment_A");
                fragmentTransaction.commit();

            }
        });





         /* private void userProfile(){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                    .setDisplayName("Giovane").build();



            user.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){


                        AlertDialog.Builder dig = new AlertDialog.Builder(getBaseContext());
                        dig.setTitle("Erro");
                        dig.setMessage("" + user.getDisplayName());
                        dig.setNeutralButton("ok", null);
                        dig.show();
                    }
                }
            });
        }
    }*/




        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            private void updateLabel() {

                String myFormat = "dd/MM/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt","BR"));

                data.setText(sdf.format(myCalendar.getTime()));
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

        data.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {


                    String userID = auth.getCurrentUser().getUid();
                    if(util.verificarCampos(getContext(), titulo.getText().toString(),anotacao.getText().toString(),data.getText().toString(),userID,FirebaseAuth.getInstance().getCurrentUser().getDisplayName())){


                        salvar_dados(titulo.getText().toString(),anotacao.getText().toString(),data.getText().toString(),userID,FirebaseAuth.getInstance().getCurrentUser().getDisplayName());



                    }

                }catch (Exception e){

                    Toast.makeText(getContext(), "" + e, Toast.LENGTH_SHORT).show();

                }




            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();

        ativar_firebase_offline();

return  root;

    }

    private void ativar_firebase_offline() {

        try {
            if (!firebaseoffline) {

                FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                firebaseoffline = true;

            } else {


            }

        } catch (Exception e) {


        }


    }

private void limpardados(){


    titulo.setText("");
    anotacao.setText("");
    data.setText("");

    Fragment fragmentA = new diario_menu();
    FragmentManager fragmentManager = getFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.replace(R.id.nav_host_fragment2, fragmentA, "Fragment_A");
    fragmentTransaction.commit();




}

    private void salvar_dados(String titulo, String nota, String data, String userid, String paciente) {

        try {

            progress = new DialogProgress();
            progress.show(getFragmentManager(), "1");

            //FirebaseUser user = auth.getCurrentUser();
            //user.getDisplayName()

            //Buscar a chave do push
            String key = firebaseDatabase.getReference().child("db").child("Diario").push().getKey();


            Anotacao anotacao = new Anotacao(titulo, nota, data, paciente, key);

            DatabaseReference databaseReference = firebaseDatabase.getReference().child("db").child("Diario").child(userid);


       /*

       Map<String, Object> valor = new HashMap<>();
        valor.put("nome","Giovane");
        valor.put("idade",27);
        valor.put("fumante",false);*/



            // databaseReference.child("9").setValue(gerente).addOnCompleteListener(new OnCompleteListener<Void>() {
            databaseReference.child(key).setValue(anotacao).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {

                        Toast.makeText(getContext(), "Dados gravados com sucesso.", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                        limpardados();

                    } else {

                        Toast.makeText(getContext(), "Falha ao gravar dados.", Toast.LENGTH_SHORT).show();
                        DialogAlerta dialogAlerta = new DialogAlerta("Erro","Preencha os dados para salvar");
                        dialogAlerta.show(getFragmentManager(),"1");
                        progress.dismiss();
                    }
                }
            });
        }catch (Exception e){

            Toast.makeText(getContext(), "" + e, Toast.LENGTH_SHORT).show();

        }




    }


}
