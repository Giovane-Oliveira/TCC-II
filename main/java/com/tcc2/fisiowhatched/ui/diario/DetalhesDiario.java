package com.tcc2.fisiowhatched.ui.diario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.tcc2.fisiowhatched.Main2Activity;
import com.tcc2.fisiowhatched.MainActivity;
import com.tcc2.fisiowhatched.R;
import com.tcc2.fisiowhatched.Util.DialogProgress;
import com.tcc2.fisiowhatched.Util.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DetalhesDiario extends Fragment {

    private Anotacao diario;
    private EditText texto;
    private Button alterar, excluir;
    private Switch editar;
    private FirebaseAuth auth;
    private EditText titulo_edt;
    private TextInputLayout titulo_layout;


    private FirebaseDatabase firebaseDatabase;
    private boolean firebaseoffline = false;
    private DialogProgress progress;

    @SuppressLint("WrongConstant")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_detalhes_diario, container, false);


        texto = root.findViewById(R.id.detalhes_anotacao_diario);
        alterar = root.findViewById(R.id.btn_alterar_anotacao_diario);
        editar = root.findViewById(R.id.btn_editar_nota_detalhes);
        titulo_edt = root.findViewById(R.id.edt_titulo_nota);
        titulo_layout = root.findViewById(R.id.edt_titulo_nota_field);
        excluir = root.findViewById(R.id.edt_excluir_nota);





        Bundle bundle = this.getArguments();
        if (bundle != null) {
            diario = bundle.getParcelable("diario");
            int chave = bundle.getInt("chave");

            if (chave == 1) {

                alterar.setVisibility(View.GONE);
                excluir.setVisibility(View.GONE);
                editar.setVisibility(View.GONE);

                ((Main2Activity)getActivity()).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                ((Main2Activity)getActivity()).getSupportActionBar().setCustomView(R.layout.custom_toolbar);
                TextView textView = ((Main2Activity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.toolbar_title);
                textView.setText("" + diario.getTitulo());
                ImageView perfil = ((Main2Activity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.toolbar_image);
                ImageView voltar2 = ((Main2Activity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.toolbar_back);


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


                voltar2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {

                            getActivity().onBackPressed();

                        }catch (Exception e){
                            AlertDialog.Builder dig = new AlertDialog.Builder(getContext());
                            dig.setTitle("Aviso!");
                            dig.setMessage("" + e);
                            dig.setNeutralButton("ok", null);
                            dig.show();

                        }



                    }
                });




            } else {

                ((MainActivity)getActivity()).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                ((MainActivity)getActivity()).getSupportActionBar().setCustomView(R.layout.custom_toolbar);
                TextView textView = ((MainActivity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.toolbar_title);
                textView.setText("" + diario.getTitulo());
                ImageView perfil = ((MainActivity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.toolbar_image);
                ImageView voltar = ((MainActivity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.toolbar_back);


                // voltar.setVisibility(View.GONE);

                Uri url = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();

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

                        try{

                            Fragment fragmentA = new diario_menu();
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.nav_host_fragment2, fragmentA, "Fragment_A");
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();

                        }catch (Exception e){


                            AlertDialog.Builder dig = new AlertDialog.Builder(getContext());
                            dig.setTitle("Aviso!");
                            dig.setMessage("" + e);
                            dig.setNeutralButton("ok", null);
                            dig.show();


                        }



                    }
                });


                alterar.setVisibility(View.VISIBLE);
                editar.setVisibility(View.VISIBLE);


            }
        }







        texto.setText("" + diario.getNota());

        titulo_edt.setText("" + diario.getTitulo());
        texto.setEnabled(false);
        titulo_edt.setEnabled(false);


       /* texto.setKeyListener( null );
        texto.setFocusable( false );
        texto.setCursorVisible(false);*/

        editar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){

                    texto.setEnabled(true);
                    titulo_edt.setEnabled(true);

                }else{

                    texto.setEnabled(false);
                    titulo_edt.setEnabled(false);
                }


            }
        });






        excluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remover_dados();
            }
        });


        alterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (util.verificarCampos(getContext(), titulo_edt.getText().toString(), texto.getText().toString())) {

                    alterar_dados(titulo_edt.getText().toString(), texto.getText().toString(), diario.getData(), diario.getNomePaciente(), diario.getId());


                }


            }
        });


        firebaseDatabase = FirebaseDatabase.getInstance();
        ativar_firebase_offline();


        return root;

    }


    private void alterar_fragment() {


        Fragment fragmentB = new diario_menu();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment2, fragmentB);
        fragmentTransaction.commit();

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


    private void alterar_dados(String titulo, String nota, String data, String nomePaciente, String id) {

        progress = new DialogProgress();
        progress.show(getActivity().getSupportFragmentManager(), "1");

        auth = FirebaseAuth.getInstance();
        String userID = auth.getCurrentUser().getUid();

        Anotacao anotacao = new Anotacao(titulo, nota, data, nomePaciente, id);

        DatabaseReference databaseReference = firebaseDatabase.getReference().child("db").child("Diario").child(userID);

        Map<String, Object> atualizar = new HashMap<>();
        atualizar.put(id, anotacao);


        databaseReference.updateChildren(atualizar).addOnCompleteListener(new OnCompleteListener<Void>() {

            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    Toast.makeText(getContext(), "Dados alterados com sucesso.", Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                    alterar_fragment();
                } else {


                }
            }
        });


    }


    private void remover_dados() {

        progress = new DialogProgress();
        progress.show(getActivity().getSupportFragmentManager(), "1");


        String key = FirebaseAuth.getInstance().getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("db").child("Diario").child(key);

        databaseReference.child(diario.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Dados excluídos com sucesso.", Toast.LENGTH_SHORT).show();

                    progress.dismiss();
                    alterar_fragment();

                } else {


                }
            }
        });

    }


}