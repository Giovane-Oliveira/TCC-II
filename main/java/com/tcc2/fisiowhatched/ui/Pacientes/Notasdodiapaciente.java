package com.tcc2.fisiowhatched.ui.Pacientes;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.Toast;

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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tcc2.fisiowhatched.Escolher_tipo_de_usuario;
import com.tcc2.fisiowhatched.Main2Activity;
import com.tcc2.fisiowhatched.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class Notasdodiapaciente extends Fragment implements RecyclerView_ListaPacientescomnota.ClickPaciente, Runnable {

    private RecyclerView recyclerView_pacientes;
    private FirebaseDatabase database;

    private RecyclerView_ListaPacientescomnota recyclerView_listaPacientescomnota;
    private List<Paciente> pacientes = new ArrayList<Paciente>();

    private ChildEventListener childEventListener;
    private DatabaseReference reference_database;
    private List<String> keys = new ArrayList<String>();
    private FirebaseAuth auth;
    Calendar myCalendar = Calendar.getInstance();

    @SuppressLint("WrongConstant")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.notasdodiafragment, container, false);

        recyclerView_pacientes = root.findViewById(R.id.recyclerView_pacientes);
        database = FirebaseDatabase.getInstance();

        iniciarRecyclerView(0);


        // ((MainActivity)getActivity()).getSupportActionBar().setTitle("" + exercicio.getTitulo());
        ((Main2Activity) getActivity()).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ((Main2Activity) getActivity()).getSupportActionBar().setCustomView(R.layout.custom_toolbar);
        TextView textView = ((Main2Activity) getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.toolbar_title);
        textView.setText("Notas do Dia");
        ImageView perfil = ((Main2Activity) getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.toolbar_image);
        ImageView voltar = ((Main2Activity) getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.toolbar_back);


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


    /*    voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragmentA = new ListaPerfilPaciente();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, fragmentA, "Fragment_A");
                fragmentTransaction.commit();

            }
        });*/


        return root;
    }


    private void iniciarRecyclerView(int n) {


        recyclerView_pacientes.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView_listaPacientescomnota = new RecyclerView_ListaPacientescomnota(getContext(), pacientes, this, n);

        recyclerView_pacientes.setAdapter(recyclerView_listaPacientescomnota);


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

                    recyclerView_listaPacientescomnota.notifyDataSetChanged();


                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                    String key = dataSnapshot.getKey();

                    keys.add(key);

                    Paciente infoPacientes = dataSnapshot.getValue(Paciente.class);

                    infoPacientes.setId(key);

                    pacientes.add(infoPacientes);

                    recyclerView_listaPacientescomnota.notifyDataSetChanged();
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {


                    String key = dataSnapshot.getKey();

                    int index = keys.indexOf(key);

                    pacientes.remove(index);

                    keys.remove(index);


                    recyclerView_listaPacientescomnota.notifyItemRemoved(index);
                    recyclerView_listaPacientescomnota.notifyItemChanged(index, pacientes.size());


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


    //menu
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.notasdodia_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.fazer_logout:

                logout();


                break;

            case R.id.ver_todos:

                iniciarRecyclerView(1);

                break;

            case R.id.notas_do_dia:

                iniciarRecyclerView(0);

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


    @Override
    public void run() {

    }

    @Override
    public void click_Paciente(final Paciente paciente) {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt", "BR"));
        final String date = sdf.format(myCalendar.getTime());

        String check = FirebaseDatabase.getInstance().getReference().child("db").child("Status").child(paciente.getId()).child("id").setValue(paciente.getId()).toString();

        if (!check.isEmpty()) {

            FirebaseDatabase.getInstance().getReference().child("db").child("Status").child(paciente.getId()).child("id").setValue(paciente.getId());
            FirebaseDatabase.getInstance().getReference().child("db").child("Status").child(paciente.getId()).child("data").setValue(date);
            FirebaseDatabase.getInstance().getReference().child("db").child("Status").child(paciente.getId()).child("status").setValue("lido");

        }else {

            Query query1 = FirebaseDatabase.getInstance().getReference().child("db").child("Status").child(paciente.getId());
            query1.addListenerForSingleValueEvent(new ValueEventListener() {


                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {

                        String data = dataSnapshot.child("data").getValue(String.class);
                        String status = dataSnapshot.child("status").getValue(String.class);


                        if (status.equals("lido") && !data.equals(date)) {
                            FirebaseDatabase.getInstance().getReference().child("db").child("Status").child(paciente.getId()).child("status").setValue("Não Lido");

                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }


            });

        }






        Fragment fragmentA = new Lista_Diario_paciente();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putParcelable("paciente", paciente);
        fragmentA.setArguments(bundle);
        fragmentTransaction.replace(R.id.nav_host_fragment, fragmentA, "Fragment_A");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

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


        if (childEventListener != null) {

            reference_database.removeEventListener(childEventListener);
        }
    }

}