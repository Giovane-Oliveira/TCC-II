package com.tcc2.fisiowhatched.ui.diario;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import com.tcc2.fisiowhatched.MainActivity;
import com.tcc2.fisiowhatched.R;
import com.tcc2.fisiowhatched.Util.DialogProgress;
import com.tcc2.fisiowhatched.splash_screen;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class diario_menu extends Fragment implements RecyclerView_ListaDiario.ClickEmpresa, Runnable {

    private EditText data;
    private Button pesquisa;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private FirebaseDatabase database;


    private RecyclerView_ListaDiario recyclerView_listaDiarios;
    private List<Anotacao> anotacoes = new ArrayList<Anotacao>();

    private ChildEventListener childEventListener;
    private DatabaseReference reference_database;
    private List<String> keys = new ArrayList<String>();


    private boolean firebaseOffline = false;
    private Handler handler;
    private Thread thread;
    private Query query;

    private String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

    Calendar myCalendar = Calendar.getInstance();


    @SuppressLint("WrongConstant")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.diario_menu, container, false);


        data = root.findViewById(R.id.edt_diario);
        pesquisa = root.findViewById(R.id.btn_diario);
        recyclerView = root.findViewById(R.id.recyclerView_diario);
        floatingActionButton = root.findViewById(R.id.floatingActionButtondiario);


        ((MainActivity) getActivity()).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ((MainActivity) getActivity()).getSupportActionBar().setCustomView(R.layout.custom_toolbar);
        TextView textView = ((MainActivity) getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.toolbar_title);
        textView.setText("Diário");
        ImageView perfil = ((MainActivity) getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.toolbar_image);
        ImageView voltar = ((MainActivity) getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.toolbar_back);


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


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            private void updateLabel() {

                String myFormat = "dd/MM/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt", "BR"));

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


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragmentA = new Gravar_dados_diario();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment2, fragmentA, "Fragment_A");
                fragmentTransaction.commit();


            }
        });


        pesquisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                anotacoes.clear();
                iniciarRecyclerView();

                Query query = database.getReference().child("db").child("Diario").child(userID).orderByChild("data").equalTo(data.getText().toString());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snap : dataSnapshot.getChildren()) {

                            Anotacao notas = snap.getValue(Anotacao.class);
                            anotacoes.add(notas);
                            recyclerView_listaDiarios.notifyDataSetChanged();
                        }


                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("Realtime", "onCancelled", databaseError.toException());
                    }
                });


            }
        });


        recyclerView = root.findViewById(R.id.recyclerView_diario);


        database = FirebaseDatabase.getInstance();


        // conexaoFirebaseBD();


        handler = new Handler();
        thread = new Thread(this);
        thread.start();


        iniciarRecyclerView();
        ativarFirebaseOffline();


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

        try {

            FirebaseAuth.getInstance().signOut();


            startActivity(new Intent(getContext(), Escolher_tipo_de_usuario.class));
            getActivity().finish();

        } catch (Exception e) {

            Toast.makeText(getContext(), "" + e, Toast.LENGTH_SHORT).show();

        }



    }

//-------------------------------------------------------------------------- menu ---------------------------------------------


    @Override
    public void run() {


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

    private void iniciarRecyclerView() {


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView_listaDiarios = new RecyclerView_ListaDiario(getContext(), anotacoes, this);

        recyclerView.setAdapter(recyclerView_listaDiarios);


    }


    private void ouvinte() {


        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt", "BR"));

        String date = sdf.format(myCalendar.getTime());

        data.setText(date);

        //reference_database = database.getReference().child("db").child("Diario").child(userID);
        query = database.getReference().child("db").child("Diario").child(userID).orderByChild("data").equalTo(date);


        if (childEventListener == null) {

            childEventListener = query.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                    String key = dataSnapshot.getKey();

                    keys.add(key);

                    Anotacao notas = dataSnapshot.getValue(Anotacao.class);

                    notas.setId(key);

                    anotacoes.add(notas);

                    recyclerView_listaDiarios.notifyDataSetChanged();


                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                    String key = dataSnapshot.getKey();

                    keys.add(key);

                    Anotacao notas = dataSnapshot.getValue(Anotacao.class);

                    notas.setId(key);

                    anotacoes.add(notas);

                    recyclerView_listaDiarios.notifyDataSetChanged();

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {


                    String key = dataSnapshot.getKey();

                    int index = keys.indexOf(key);

                    anotacoes.remove(index);

                    keys.remove(index);


                    recyclerView_listaDiarios.notifyItemRemoved(index);
                    recyclerView_listaDiarios.notifyItemChanged(index, anotacoes.size());


                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });


        }

    }




















   /* private void ouvinte_2(){


        auth = FirebaseAuth.getInstance();
        String userID = auth.getCurrentUser().getUid();

        reference_database = database.getReference().child("db").child("Diario").child(userID);

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                List<Anotacao> notas = new ArrayList<Anotacao>();

                for(DataSnapshot data: dataSnapshot.getChildren()){


                    Anotacao anotacao = data.getValue(Anotacao.class);


                    notas.add(anotacao);

                }

                recyclerView_listaDiarios.notifyDataSetChanged();





            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        recyclerView_listaDiarios.notifyDataSetChanged();
        reference_database.addValueEventListener(valueEventListener);


    }*/


    //--------------------------------------------Ciclos de Vida------------------------------------


    @Override
    public void onStart() {
        super.onStart();

        ouvinte();


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


    @Override
    public void click_Empresa(Anotacao diarios) {

        try {


            Fragment fragmentA = new DetalhesDiario();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putParcelable("diario", diarios);
            bundle.putInt("chave", 0);
            fragmentA.setArguments(bundle);
            fragmentTransaction.replace(R.id.nav_host_fragment2, fragmentA, "Fragment_A");
            fragmentTransaction.commit();


        } catch (Exception e) {


            Toast.makeText(getContext(), "" + e, Toast.LENGTH_SHORT).show();


        }

    }
}