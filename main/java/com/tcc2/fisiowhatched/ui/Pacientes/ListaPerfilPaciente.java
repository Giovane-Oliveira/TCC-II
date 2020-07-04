package com.tcc2.fisiowhatched.ui.Pacientes;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.tcc2.fisiowhatched.Escolher_tipo_de_usuario;
import com.tcc2.fisiowhatched.Main2Activity;
import com.tcc2.fisiowhatched.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListaPerfilPaciente extends Fragment implements RecyclerView_ListaPacientes.ClickPaciente, Runnable {

    private FirebaseDatabase database;
    private RecyclerView recyclerView_perfilpacientes;
    private RecyclerView_ListaPacientes recyclerView_listaPacientes;
    private List<Paciente> pacientes = new ArrayList<Paciente>();

    private ChildEventListener childEventListener;
    private DatabaseReference reference_database;
    private List<String> keys = new ArrayList<String>();
    private FirebaseAuth auth;


    @SuppressLint("WrongConstant")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_listaperfilpaciente, container, false);


        ((Main2Activity)getActivity()).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ((Main2Activity)getActivity()).getSupportActionBar().setCustomView(R.layout.custom_toolbar);
        TextView textView = ((Main2Activity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.toolbar_title);
        textView.setText("Perfil Pacientes");
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


     /*   voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragmentA = new ListaPerfilPaciente();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, fragmentA, "Fragment_A");
                fragmentTransaction.commit();

            }
        });*/


        recyclerView_perfilpacientes = root.findViewById(R.id.recyclerView_perfilpacientes);
        database = FirebaseDatabase.getInstance();

        iniciarRecyclerView();

        return root;
    }


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


    private void iniciarRecyclerView(){



        recyclerView_perfilpacientes.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView_listaPacientes = new RecyclerView_ListaPacientes(getContext(),pacientes,this);

        recyclerView_perfilpacientes.setAdapter(recyclerView_listaPacientes);

    }

    private void ouvinte() {


        auth = FirebaseAuth.getInstance();

        reference_database = database.getReference().child("db").child("Users");


        if (childEventListener == null) {

            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    String key = dataSnapshot.getKey();

                    keys.add(key);

                    Paciente infoPacientes = dataSnapshot.getValue(Paciente.class);

                    infoPacientes.setId(key);

                    pacientes.add(infoPacientes);

                    recyclerView_listaPacientes.notifyDataSetChanged();



                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                    String key = dataSnapshot.getKey();

                    keys.add(key);

                    Paciente infoPacientes = dataSnapshot.getValue(Paciente.class);

                    infoPacientes.setId(key);

                    pacientes.add(infoPacientes);

                    recyclerView_listaPacientes.notifyDataSetChanged();
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {


                    String key = dataSnapshot.getKey();

                    int index = keys.indexOf(key);

                    pacientes.remove(index);

                    keys.remove(index);


                    recyclerView_listaPacientes.notifyItemRemoved(index);
                    recyclerView_listaPacientes.notifyItemChanged(index, pacientes.size());


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



    @Override
    public void click_Paciente(Paciente paciente) {

        Fragment fragmentA = new Perfil_paciente();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putParcelable("paciente", paciente);
        bundle.putInt("opcao",1);
        fragmentA.setArguments(bundle);
        fragmentTransaction.replace(R.id.nav_host_fragment, fragmentA, "Fragment_A");
        fragmentTransaction.commit();

    }

    @Override
    public void run() {

    }


    @Override
    public void onStart() {
        super.onStart();

        ouvinte();


    }



    @Override
    public void onStop() {
        super.onStop();




    }




    @Override
    public void onDestroy() {
        super.onDestroy();


      /*if(valueEventListener != null){

            reference_database.removeEventListener(valueEventListener);
        }*/



        if(childEventListener != null){

            reference_database.removeEventListener(childEventListener);
        }
    }

}