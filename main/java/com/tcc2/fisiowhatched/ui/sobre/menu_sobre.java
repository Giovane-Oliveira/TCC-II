package com.tcc2.fisiowhatched.ui.sobre;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tcc2.fisiowhatched.Escolher_tipo_de_usuario;
import com.tcc2.fisiowhatched.Main2Activity;
import com.tcc2.fisiowhatched.MainActivity;
import com.tcc2.fisiowhatched.R;
import com.tcc2.fisiowhatched.ui.diario.diario_menu;

public class menu_sobre extends Fragment {

    private TextView textView;
    private EditText editText;


    @SuppressLint("WrongConstant")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.sobre_menu, container, false);


        // ((MainActivity)getActivity()).getSupportActionBar().setTitle("" + exercicio.getTitulo());

        ((MainActivity)getActivity()).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ((MainActivity)getActivity()).getSupportActionBar().setCustomView(R.layout.custom_toolbar);
        TextView titulo = ((MainActivity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.toolbar_title);
        titulo.setText("Sobre");
        ImageView perfil = ((MainActivity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.toolbar_image);
        ImageView voltar = ((MainActivity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.toolbar_back);


        voltar.setVisibility(View.GONE);

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


      /*  voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragmentA = new ListaPerfilPaciente();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, fragmentA, "Fragment_A");
                fragmentTransaction.commit();

            }
        });*/


        textView = root.findViewById(R.id.textViewsobre);
        editText = root.findViewById(R.id.edtDetalhessobre);

        editText.setKeyListener( null );
        editText.setFocusable( false );
        editText.setCursorVisible(false);


        buscardados();

        return root;
    }

private void buscardados() {


    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("db").child("Sobre");

    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            if(dataSnapshot.exists()){

                String texto = dataSnapshot.child("sobre").getValue(String.class);

                editText.setText(texto);

            }else{

                Toast.makeText(getContext(), "dsfdfdsfdsfdf", Toast.LENGTH_SHORT).show();

            }


        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }

    });

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

}