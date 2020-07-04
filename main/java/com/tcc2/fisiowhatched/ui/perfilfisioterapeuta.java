package com.tcc2.fisiowhatched.ui;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.tcc2.fisiowhatched.Escolher_tipo_de_usuario;
import com.tcc2.fisiowhatched.Main2Activity;
import com.tcc2.fisiowhatched.R;
import com.tcc2.fisiowhatched.Util.DialogProgress;
import com.tcc2.fisiowhatched.ui.Pacientes.Alterarimagemdeperfil;
import com.tcc2.fisiowhatched.ui.Pacientes.ListaPerfilPaciente;
import com.tcc2.fisiowhatched.ui.Pacientes.Paciente;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;


public class perfilfisioterapeuta extends Fragment {

    private EditText nome;
    private EditText datansc;
    private EditText cpf;
    private EditText email;

    private String nome1;
    private String datanasc1;
    private String cpf1;
    private String email1;

    private ImageView imageView;
    private Button alterarimagem;
    private Button alterar;
    private Button editar;
    private String url_imagem;
    private Paciente paciente;
    Calendar myCalendar = Calendar.getInstance();
    private ProgressBar progressBar;



    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_perfilfisioterapeuta, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            url_imagem = bundle.getString("uri");
            FirebaseDatabase.getInstance().getReference().child("db").child("Fisioterapeuta").child(userID).child("url_imagem").setValue(url_imagem);

        }

        // ((MainActivity)getActivity()).getSupportActionBar().setTitle("" + exercicio.getTitulo());
        ((Main2Activity)getActivity()).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ((Main2Activity)getActivity()).getSupportActionBar().setCustomView(R.layout.custom_toolbar);
        TextView textView = ((Main2Activity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.toolbar_title);
        String name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        textView.setText("" + name);
        ImageView perfil = ((Main2Activity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.toolbar_image);
        ImageView voltar = ((Main2Activity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.toolbar_back);


        voltar.setVisibility(View.GONE);

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



        nome = root.findViewById(R.id.nomepaciente_profilefisioterapeuta);
        email = root.findViewById(R.id.email_profilefisioterapeuta);
        cpf = root.findViewById(R.id.edtcpf_profilefisioterapeuta);
        datansc = root.findViewById(R.id.edtdatanasc_profilefisioterapeuta);
        imageView = root.findViewById(R.id.imagemdeperfilfisioterapeuta);
        alterarimagem = root.findViewById(R.id.btnalterarimagemdeperfilfisioterapeuta);
        editar = root.findViewById(R.id.btneditardadosdoperfilfisioterapeuta);
        alterar = root.findViewById(R.id.btnenviar_profilefisioterapeuta);
        progressBar = root.findViewById(R.id.progressBarperfil_fisioterapeuta);


        nome.setEnabled(false);
        email.setEnabled(false);
        cpf.setEnabled(false);
        datansc.setEnabled(false);

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nome.setEnabled(true);
                cpf.setEnabled(true);
                datansc.setEnabled(true);


                final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    private void updateLabel() {

                        String myFormat = "dd/MM/yyyy"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt","BR"));

                        datansc.setText(sdf.format(myCalendar.getTime()));
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

                datansc.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });

            }
        });

        alterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                if(nome1.equals(nome.getText().toString()) && email1.equals(email.getText().toString()) && cpf1.equals(cpf.getText().toString()) && datanasc1.equals(datansc.getText().toString())){

                    Toast.makeText(getContext(), "Não há dados para alterar", Toast.LENGTH_SHORT).show();

                }else{

                    if(cpf.length() == 11){

                        alterar_dados(cpf.getText().toString(), datansc.getText().toString(), email.getText().toString(), userID, nome.getText().toString(), url_imagem);


                    }else{

                        Toast.makeText(getContext(), "cpf inválido!", Toast.LENGTH_SHORT).show();

                    }



                }

            }
        });




        alterarimagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragmentA = new alterarimagemfisioterapeuta();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, fragmentA, "Fragment_A");
                fragmentTransaction.commit();
            }
        });

        ouvinte_1();
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        download_imagem_2(imageView, userID);


        return root;
    }


    // ----------------------------------------- Menu ---------------------------------------------------------

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.principal, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_logout:

                logout();
                break;


        }
        return super.onOptionsItemSelected(item);

    }

    public void logout() {


        // Email
        FirebaseAuth.getInstance().signOut();


        startActivity(new Intent(getContext(), Escolher_tipo_de_usuario.class));
        getActivity().finish();


    }

//-------------------------------------------------------------------------- menu ---------------------------------------------



    public void ouvinte_1() {

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("db").child("Fisioterapeuta").child(userID);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Paciente paciente = dataSnapshot.getValue(Paciente.class);
                nome.setText(paciente.getNome() + "");
                datansc.setText(paciente.getDatanasc() + "");
                cpf.setText(paciente.getCpf()+ "");
                email.setText(paciente.getEmail()+ "");

                nome1 = paciente.getNome();
                datanasc1 = paciente.getDatanasc();
                cpf1 = paciente.getCpf();
                email1 = paciente.getEmail();




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });

    }



    private void alterar_dados(String cpf, String datanasc, String email, String userID, String nome, String url_imagem) {

        final DialogProgress progress = new DialogProgress();
        progress.show(getActivity().getSupportFragmentManager(), "1");

        Uri uri = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();

        if(url_imagem == null && uri == null){

            url_imagem = "sem foto";

        }else{

            url_imagem = uri.toString();

        }

        Paciente paciente = new Paciente(cpf, datanasc, email, userID, nome, url_imagem);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("db").child("Fisioterapeuta");

        Map<String, Object> atualizar = new HashMap<>();
        atualizar.put(userID, paciente);


        databaseReference.updateChildren(atualizar).addOnCompleteListener(new OnCompleteListener<Void>() {

            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progress.dismiss();
                    Toast.makeText(getContext(), "Dados alterados com sucesso.", Toast.LENGTH_SHORT).show();


                } else {


                }
            }
        });


    }

    public void download_imagem_2(final ImageView imageView, String userID) {

        progressBar.setVisibility(View.VISIBLE);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference reference = storage.getReference().child("imagensdeperfil").child(userID).child("perfil" + userID + ".jpg");

        reference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {

                if (task.isSuccessful()) {

                    String url = task.getResult().toString();

                    Picasso.with(getContext()).load(url).into(imageView, new com.squareup.picasso.Callback() {

                        @Override
                        public void onSuccess() {

                            progressBar.setVisibility(View.GONE);


                        }

                        @Override
                        public void onError() {

                            progressBar.setVisibility(View.GONE);

                            Toast.makeText(getContext(), "Erro ao carregar a imagem de perfil", Toast.LENGTH_SHORT).show();


                        }
                    });

                }else{

                    progressBar.setVisibility(View.GONE);


                }

            }
        });


    }




}
