package com.tcc2.fisiowhatched.ui.exercicios;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.tcc2.fisiowhatched.MainActivity;
import com.tcc2.fisiowhatched.R;
import com.tcc2.fisiowhatched.Tela_login;
import com.tcc2.fisiowhatched.ui.exercicios.Exercicios;

import java.util.Objects;

public class DetalhesExercicio extends Fragment {

    private Exercicios exercicio;
    private ImageView imageView;
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView imageView4;
    private ImageView imageView5;
    private ImageView imageView6;
    private ImageView imageView7;
    private ImageView imageView8;
    private ImageView imageView9;
    private ImageView imageView10;

    private TextView detalhesexe;
    private TextView detalhesexe1;
    private TextView detalhesexe2;
    private TextView detalhesexe3;
    private TextView detalhesexe4;
    private TextView detalhesexe5;
    private TextView detalhesexe6;
    private TextView detalhesexe7;
    private TextView detalhesexe8;
    private TextView detalhesexe9;
    private TextView detalhesexe10;



    @SuppressLint("WrongConstant")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_detalhes_exercicio, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {

            exercicio = bundle.getParcelable("exercicios");
        }


        // ((MainActivity)getActivity()).getSupportActionBar().setTitle("" + exercicio.getTitulo());
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ((MainActivity)getActivity()).getSupportActionBar().setCustomView(R.layout.custom_toolbar);
        TextView textView = ((MainActivity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.toolbar_title);
        textView.setText("" + exercicio.getTitulo());
        ImageView perfil = ((MainActivity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.toolbar_image);
        ImageView voltar = ((MainActivity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.toolbar_back);


       // imageView2.setVisibility(View.GONE);

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

                Fragment fragmentA = new exercicios_menu();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment2, fragmentA, "Fragment_A");
                fragmentTransaction.commit();

            }
        });




        imageView = root.findViewById(R.id.imgDetalhesExe);
        imageView1 = root.findViewById(R.id.imgDetalhesExe1);
        imageView2 = root.findViewById(R.id.imgDetalhesExe2);
        imageView3 = root.findViewById(R.id.imgDetalhesExe3);
        imageView4 = root.findViewById(R.id.imgDetalhesExe4);
        imageView5 = root.findViewById(R.id.imgDetalhesExe5);
        imageView6 = root.findViewById(R.id.imgDetalhesExe6);
        imageView7 = root.findViewById(R.id.imgDetalhesExe7);
        imageView8 = root.findViewById(R.id.imgDetalhesExe8);
        imageView9 = root.findViewById(R.id.imgDetalhesExe9);
        imageView10 = root.findViewById(R.id.imgDetalhesExe10);

        detalhesexe = root.findViewById((R.id.edtDetalhesExe));
        detalhesexe1 = root.findViewById((R.id.edtDetalhesExe1));
        detalhesexe2 = root.findViewById((R.id.edtDetalhesExe2));
        detalhesexe3 = root.findViewById((R.id.edtDetalhesExe3));
        detalhesexe4 = root.findViewById((R.id.edtDetalhesExe4));
        detalhesexe5 = root.findViewById((R.id.edtDetalhesExe5));
        detalhesexe6 = root.findViewById((R.id.edtDetalhesExe6));
        detalhesexe7 = root.findViewById((R.id.edtDetalhesExe7));
        detalhesexe8= root.findViewById((R.id.edtDetalhesExe8));
        detalhesexe9 = root.findViewById((R.id.edtDetalhesExe9));
        detalhesexe10 = root.findViewById((R.id.edtDetalhesExe10));

        try {

            addimagem(imageView, detalhesexe, exercicio.getUrlimagemdetalhes(), exercicio.getDetalhes());
            addimagem(imageView1, detalhesexe1, exercicio.getUrlimagemdetalhes1(), exercicio.getDetalhes1());
            addimagem(imageView2, detalhesexe2, exercicio.getUrlimagemdetalhes2(), exercicio.getDetalhes2());
            addimagem(imageView3, detalhesexe3, exercicio.getUrlimagemdetalhes3(), exercicio.getDetalhes3());
            addimagem(imageView4, detalhesexe4, exercicio.getUrlimagemdetalhes4(), exercicio.getDetalhes4());
            addimagem(imageView5, detalhesexe5, exercicio.getUrlimagemdetalhes5(), exercicio.getDetalhes5());
            addimagem(imageView6, detalhesexe6, exercicio.getUrlimagemdetalhes6(), exercicio.getDetalhes6());
            addimagem(imageView7, detalhesexe7, exercicio.getUrlimagemdetalhes7(), exercicio.getDetalhes7());
            addimagem(imageView8, detalhesexe8, exercicio.getUrlimagemdetalhes8(), exercicio.getDetalhes8());
            addimagem(imageView9, detalhesexe9, exercicio.getUrlimagemdetalhes9(), exercicio.getDetalhes9());
            addimagem(imageView10, detalhesexe10, exercicio.getUrlimagemdetalhes10(), exercicio.getDetalhes10());

        }catch (Exception e ){

            AlertDialog.Builder dig = new AlertDialog.Builder(getContext());
            dig.setTitle("Aviso!");
            dig.setMessage("" + e);
            dig.setNeutralButton("ok", null);
            dig.show();

        }





return  root;

    }

public void addimagem(ImageView imageView, TextView textView, String url, String detalhes){


    if( url != null && !url.isEmpty() || detalhes != null && !detalhes.isEmpty()){




        Picasso.with(getContext()).load(url).into(imageView, new com.squareup.picasso.Callback() {

            @Override
            public void onSuccess() {



            }

            @Override
            public void onError() {



            }
        });


        textView.setText(detalhes);


    }else{

        imageView.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);

    }


}

}
