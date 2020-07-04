package com.tcc2.fisiowhatched.ui.Pacientes;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tcc2.fisiowhatched.Escolher_tipo_de_usuario;
import com.tcc2.fisiowhatched.Main2Activity;
import com.tcc2.fisiowhatched.R;
import com.tcc2.fisiowhatched.Util.DialogProgress;
import com.tcc2.fisiowhatched.ui.diario.Anotacao;
import com.tcc2.fisiowhatched.ui.diario.DetalhesDiario;
import com.tcc2.fisiowhatched.ui.diario.RecyclerView_ListaDiario;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class Lista_Diario_paciente extends Fragment implements RecyclerView_ListaDiario.ClickEmpresa, Runnable, RecyclerView_ListaDiario_Paciente.ClickEmpresa {
    private Paciente paciente;
    private EditText buscarDiarioPaciente;
    private Button pesquisa;
    private RecyclerView recyclerView;
    private RecyclerView_ListaDiario_Paciente recyclerView_listaDiario;
    private List<Anotacao> anotacoes = new ArrayList<Anotacao>();
    Calendar myCalendar = Calendar.getInstance();
    private ChildEventListener childEventListener;
    private Query query;
    private List<String> keys = new ArrayList<String>();
    private boolean firebaseOffline = false;
    private DialogProgress progress;


    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_lista__diario_paciente, container, false);
        try{
        buscarDiarioPaciente = root.findViewById(R.id.edt_lista_diario_paciente);
        pesquisa = root.findViewById(R.id.btn_lista_diario_paciente);
        recyclerView = root.findViewById(R.id.recyclerView_lista_diario_paciente);




            Bundle bundle = this.getArguments();
            if (bundle != null) {
                paciente = bundle.getParcelable("paciente");

            }

            // ((MainActivity)getActivity()).getSupportActionBar().setTitle("" + exercicio.getTitulo());
            ((Main2Activity)getActivity()).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            ((Main2Activity)getActivity()).getSupportActionBar().setCustomView(R.layout.custom_toolbar);
            TextView textView = ((Main2Activity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.toolbar_title);
            textView.setText("" + paciente.getNome());
            ImageView perfil = ((Main2Activity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.toolbar_image);
            ImageView voltar = ((Main2Activity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.toolbar_back);


            //voltar.setVisibility(View.GONE);

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

                    Fragment fragmentA = new Notasdodiapaciente();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.nav_host_fragment, fragmentA, "Fragment_A");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }
            });




            final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                private void updateLabel() {

                    String myFormat = "dd/MM/yyyy"; //In which you need put here
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt","BR"));

                    buscarDiarioPaciente.setText(sdf.format(myCalendar.getTime()));
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

            buscarDiarioPaciente.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });


            pesquisa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    progress = new DialogProgress();
                    progress.show(getFragmentManager(), "1");

                    anotacoes.clear();
                    iniciarRecyclerView();

                    Query query = FirebaseDatabase.getInstance().getReference().child("db").child("Diario").child(paciente.getId()).orderByChild("data").equalTo(buscarDiarioPaciente.getText().toString());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snap: dataSnapshot.getChildren()) {

                                Anotacao notas = snap.getValue(Anotacao.class);
                                anotacoes.add(notas);
                                recyclerView_listaDiario.notifyDataSetChanged();

                                progress.dismiss();


                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e("Realtime", "onCancelled", databaseError.toException());
                            progress.dismiss();

                        }
                    });
                    progress.dismiss();
                }
            });



            //  Objects.requireNonNull(((Main2Activity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle("" + paciente.getNome());




            iniciarRecyclerView();
            ativarFirebaseOffline();

        }catch (Exception e){

            AlertDialog.Builder dig = new AlertDialog.Builder(getContext());
            dig.setTitle("Aviso!");
            dig.setMessage("" + e);
            dig.setNeutralButton("ok", null);
            dig.show();

        }





        return root;
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




    private void iniciarRecyclerView() {


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView_listaDiario = new RecyclerView_ListaDiario_Paciente(getContext(), anotacoes, this);

        recyclerView.setAdapter(recyclerView_listaDiario);


    }

    @Override
    public void click_Empresa(Anotacao diarios) {
        Fragment fragmentA = new DetalhesDiario();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putParcelable("diario", diarios);
        bundle.putInt("chave",1);
        fragmentA.setArguments(bundle);
        fragmentTransaction.replace(R.id.nav_host_fragment, fragmentA, "Fragment_A");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    @Override
    public void run() {

    }

    private void ouvinte() {

        progress = new DialogProgress();
        progress.show(getFragmentManager(), "1");


        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt", "BR"));

        String date = sdf.format(myCalendar.getTime());

        buscarDiarioPaciente.setText(date);

        //reference_database = database.getReference().child("db").child("Diario").child(userID);
        query = FirebaseDatabase.getInstance().getReference().child("db").child("Diario").child(paciente.getId()).orderByChild("data").equalTo(date);


        if (childEventListener == null) {

            childEventListener = query.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                    String key = dataSnapshot.getKey();

                    keys.add(key);

                    Anotacao notas = dataSnapshot.getValue(Anotacao.class);

                    notas.setId(key);

                    anotacoes.add(notas);

                    recyclerView_listaDiario.notifyDataSetChanged();

                    progress.dismiss();


                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                    String key = dataSnapshot.getKey();

                    keys.add(key);

                    Anotacao notas = dataSnapshot.getValue(Anotacao.class);

                    notas.setId(key);

                    anotacoes.add(notas);

                    recyclerView_listaDiario.notifyDataSetChanged();

                    progress.dismiss();

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {


                    String key = dataSnapshot.getKey();

                    int index = keys.indexOf(key);

                    anotacoes.remove(index);

                    keys.remove(index);


                    recyclerView_listaDiario.notifyItemRemoved(index);
                    recyclerView_listaDiario.notifyItemChanged(index, anotacoes.size());

                    progress.dismiss();


                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });

            progress.dismiss();

        }
        progress.dismiss();
    }


    @Override
    public void onStart() {
        super.onStart();

        ouvinte();


    }


    @Override
    public void onStop() {
        super.onStop();


        if (childEventListener != null) {

            query.removeEventListener(childEventListener);
        }


    }


    @Override
    public void onDestroy() {
        super.onDestroy();


      /*if(valueEventListener != null){

            reference_database.removeEventListener(valueEventListener);
        }*/


        if (childEventListener != null) {

            query.removeEventListener(childEventListener);
        }


    }

    private void ativarFirebaseOffline() {


        try {
            if (!firebaseOffline) {

                FirebaseDatabase.getInstance().setPersistenceEnabled(true);

                firebaseOffline = true;

            } else {

                //firebase ja estiver funcionando offline
            }


        } catch (Exception e) {
            //erro
        }
    }

}
