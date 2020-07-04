package com.tcc2.fisiowhatched.ui.exercicios;


import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tcc2.fisiowhatched.Escolher_tipo_de_usuario;
import com.tcc2.fisiowhatched.MainActivity;
import com.tcc2.fisiowhatched.R;
import com.tcc2.fisiowhatched.Tela_login;
import com.tcc2.fisiowhatched.Util.DialogAlerta;
import com.tcc2.fisiowhatched.Util.DialogProgress;
import com.tcc2.fisiowhatched.Util.util;
import com.tcc2.fisiowhatched.ui.Pacientes.Lista_Diario_paciente;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class exercicios_menu extends Fragment  implements RecyclerView_ListaExercicios.ClickEmpresa,
        Runnable {


    private RecyclerView recyclerView;
    private FirebaseDatabase database;

    private RecyclerView_ListaExercicios recyclerView_listaExercicios;
    private List<Exercicios> exercicios = new ArrayList<Exercicios>();

    private ChildEventListener childEventListener;
    private DatabaseReference reference_database;
    private List<String> keys = new ArrayList<String>();


    private boolean firebaseOffline = false;
    private Handler handler;
    private Thread thread;
    private DialogProgress progress;

    @SuppressLint("WrongConstant")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        /*FragmentManager fm = getActivity().getSupportFragmentManager();
    fm.popBackStack();*/

       // ((MainActivity)getActivity()).getSupportActionBar().setTitle("" + exercicio.getTitulo());
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ((MainActivity)getActivity()).getSupportActionBar().setCustomView(R.layout.custom_toolbar);
        TextView textView = ((MainActivity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.toolbar_title);
        textView.setText("Exercícios");
       ImageView imageView = ((MainActivity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.toolbar_image);
       ImageView imageView2 = ((MainActivity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.toolbar_back);


       imageView2.setVisibility(View.GONE);

     Uri url = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();

        if (url != null) {

            Picasso.with(getContext()).load(url).into(imageView, new com.squareup.picasso.Callback() {

                @Override
                public void onSuccess() {



                }

                @Override
                public void onError() {



                }
            });


        }


     /*   imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Teste", Toast.LENGTH_SHORT).show();
            }
        });*/


        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);





        View root = inflater.inflate(R.layout.exercicios_menu, container, false);


        recyclerView = root.findViewById(R.id.recyclerView_lista_exercicios);


        database = FirebaseDatabase.getInstance();


        // conexaoFirebaseBD();


        handler = new Handler();
        thread = new Thread(this);
        thread.start();

        ativarFirebaseOffline();
        iniciarRecyclerView();

        if (!util.isNetworkAvailable(getContext())) {

            progress = new DialogProgress();
            progress.show(getFragmentManager(), "1");
            DialogAlerta dialogAlerta = new DialogAlerta("Aviso!","Você está sem conexão com a internet :C");
            dialogAlerta.show(getFragmentManager(),"1");
            progress.dismiss();

        }





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

    private void ativarFirebaseOffline(){


        try{
            if(!firebaseOffline){

                FirebaseDatabase.getInstance().setPersistenceEnabled(true);

                firebaseOffline = true;

            }else{

                //firebase ja estiver funcionando offline
            }


        }catch(Exception e){
            //erro
        }
    }




    @Override
    public void run() {

    }


    private void conexaoFirebaseBD(){



        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(".info/connected");


        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                boolean conexao = dataSnapshot.getValue(Boolean.class);


                if (conexao) {

                    Toast.makeText(getContext(), "Temos conexao com o BD", Toast.LENGTH_LONG).show();


                } else {


                    if (util.isNetworkAvailable(getContext())) {

                        Toast.makeText(getContext(), "BLOQUEIO AO NOSSO BD - ", Toast.LENGTH_LONG).show();

                    } else {


                        // Toast.makeText(getBaseContext(),"SEM CONEXAO COM A INTERNET",Toast.LENGTH_LONG).show();

                    }


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }









    //---------------------------------------INICIAR RECYCLERVIEW------------------------------------------------
    private void iniciarRecyclerView(){




        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView_listaExercicios = new RecyclerView_ListaExercicios(getContext(),exercicios,this);

        recyclerView.setAdapter(recyclerView_listaExercicios);

    }



    //---------------------------------------CLICK NO ITEM------------------------------------------------

    @Override
    public void click_Empresa(Exercicios exercicios) {

        Fragment fragmentA = new DetalhesExercicio();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putParcelable("exercicios", exercicios);
        fragmentA.setArguments(bundle);
        fragmentTransaction.replace(R.id.nav_host_fragment2, fragmentA, "Fragment_A");
        fragmentTransaction.commit();

        // Toast.makeText(getBaseContext(),"Nome: "+empresa.getNome()+"\n\nPasta: "+empresa.getId(),Toast.LENGTH_LONG).show();

    }



    //---------------------------------------Ouvinte ---------------------------------------------------




    private void ouvinte(){




        reference_database = database.getReference().child("db").child("Exercicios");


        if(childEventListener == null){

            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                    String key = dataSnapshot.getKey();

                    keys.add(key);

                    Exercicios exercicio = dataSnapshot.getValue(Exercicios.class);

                    exercicio.setId(key);

                    exercicios.add(exercicio);

                    recyclerView_listaExercicios.notifyDataSetChanged();



                    // keys 0 = 0

                    //empresas 0 = coca cola



                    // keys 1 = 1

                    //empresas 1 = pepsi



                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                    String key = dataSnapshot.getKey();


                    int index = keys.indexOf(key);

                    Exercicios exercicio = dataSnapshot.getValue(Exercicios.class);

                    exercicio.setId(key);


                    exercicios.set(index,exercicio);


                    recyclerView_listaExercicios.notifyDataSetChanged();


                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {


                    String key = dataSnapshot.getKey();

                    int index = keys.indexOf(key);

                    exercicios.remove(index);

                    keys.remove(index);


                    recyclerView_listaExercicios.notifyItemRemoved(index);
                    recyclerView_listaExercicios.notifyItemChanged(index,exercicios.size());



                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };



            reference_database.addChildEventListener(childEventListener);
        }


    }






    //--------------------------------------------Ciclos de Vida------------------------------------

    public void onStart() {
        super.onStart();


        ouvinte();
    }





    @Override
    public void onDestroy() {
        super.onDestroy();


        if(childEventListener != null){

            reference_database.removeEventListener(childEventListener);
        }
    }


}