package com.tcc2.fisiowhatched;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tcc2.fisiowhatched.Util.DialogAlerta;
import com.tcc2.fisiowhatched.Util.DialogProgress;
import com.tcc2.fisiowhatched.Util.Permissao;
import com.tcc2.fisiowhatched.Util.util;

import java.util.HashMap;
import java.util.Map;

import static java.security.AccessController.getContext;

public class Tela_login extends AppCompatActivity {
    private FirebaseAuth auth;
    private Button login, recuperar;
    private EditText email;
    private EditText senha;
    private EditText codigo;
    private TextInputLayout codigolayout;
    private int acao;
    private DialogProgress progress;


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_login);
        login = findViewById(R.id.button);
        email = findViewById(R.id.editText4);
        senha = findViewById(R.id.editText5);
        recuperar = findViewById(R.id.button2);
        codigo = findViewById(R.id.edtcodigo);
        codigolayout = findViewById(R.id.codigo);

        codigo.setVisibility(View.GONE);
        codigolayout.setVisibility(View.GONE);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_toolbar);
        TextView textView = getSupportActionBar().getCustomView().findViewById(R.id.toolbar_title);
        textView.setText("FisioWhatched");
        ImageView perfil = getSupportActionBar().getCustomView().findViewById(R.id.toolbar_image);
        ImageView voltar = getSupportActionBar().getCustomView().findViewById(R.id.toolbar_back);


        perfil.setVisibility(View.GONE);
        voltar.setVisibility(View.GONE);

        permissao();

        Bundle bundle = getIntent().getExtras();

        assert bundle != null;
        acao = bundle.getInt("tipo");

        if (acao == 2) {

            codigo.setVisibility(View.VISIBLE);
            codigolayout.setVisibility(View.VISIBLE);

        }


        auth = FirebaseAuth.getInstance();


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mail = email.getText().toString();
                String pass = senha.getText().toString();


                if (mail.trim().isEmpty() || pass.trim().isEmpty()) {

                    AlertDialog.Builder dig = new AlertDialog.Builder(Tela_login.this);
                    dig.setTitle("Aviso!");
                    dig.setMessage("Por favor, preencha os campos.");
                    dig.setNeutralButton("ok", null);
                    dig.show();


                } else {

                    if (util.isNetworkAvailable(Tela_login.this)) {

                        login_email(mail, pass);

                    } else {

                        AlertDialog.Builder dig = new AlertDialog.Builder(Tela_login.this);
                        dig.setTitle("Aviso!");
                        dig.setMessage("Você está sem conexão com a internet :(");
                        dig.setNeutralButton("ok", null);
                        dig.show();
                    }
                }


            }
        });

        recuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mail = email.getText().toString();

                if (mail.trim().isEmpty()) {

                    DialogAlerta dialogAlerta = new DialogAlerta("Aviso!","Informe apenas seu e-mail e clique novamente em recuperar");
                    dialogAlerta.show(getSupportFragmentManager(),"1");


                } else {

                    recuperarSenha(email.getText().toString());

                }


            }
        });

    }

    private void permissao() {


        String permissoes[] = new String[]{

                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
        };
        Permissao.permissao(this, 0, permissoes);


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        for (int result : grantResults) {

            if (result == PackageManager.PERMISSION_DENIED) {

                Toast.makeText(this, "Aceite as permissões para o app funcionar corretamente", Toast.LENGTH_SHORT).show();
                finish();
                break;
            }


        }

    }

    public void recuperarSenha(String mail) {

        auth.sendPasswordResetEmail("" + mail).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                AlertDialog.Builder dig = new AlertDialog.Builder(Tela_login.this);
                dig.setTitle("Aviso!");
                dig.setMessage("Enviamos um link para o seu email para redefinir sua senha");
                dig.setNeutralButton("ok", null);
                dig.show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                String erro = e.toString();
                util.operro(Tela_login.this, erro);
            }
        });

    }


    public void login_email(String email, String senha) {

        auth.signInWithEmailAndPassword("" + email, "" + senha).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    adicionar_usernobancodedados();


                } else {

                    String resposta = task.getException().toString();

                    util.operro(Tela_login.this, resposta);


                }
            }
        });

    }


    public void adicionar_usernobancodedados() {

        progress = new DialogProgress();
        progress.show(getSupportFragmentManager(), "1");

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String userID = user.getUid();
        final String userName = user.getDisplayName();


        if (!codigo.getText().toString().trim().isEmpty()) {


            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("db");

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    String chave = dataSnapshot.child("codigo").getValue(String.class);

                    if (chave.equals(codigo.getText().toString())) {

                        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("db").child("Fisioterapeuta").child(userID);

                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (userName != null && dataSnapshot.exists()) {

                                    progress.dismiss();
                                    Intent intent = new Intent(Tela_login.this, Main2Activity.class);
                                    startActivity(intent);
                                    finish();

                                } else if (userName != null && !dataSnapshot.exists() || userName == null && !dataSnapshot.exists()) {

                                    progress.dismiss();
                                    verificarusuario2();


                                } else {

                                    progress.dismiss();

                                    Intent intent = new Intent(Tela_login.this, Main2Activity.class);

                                    startActivity(intent);

                                    finish();
                                }

                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }

                        });


                    } else {

                        progress.dismiss();

                        FirebaseAuth.getInstance().signOut();

                        Toast.makeText(Tela_login.this, "Código incorreto", Toast.LENGTH_SHORT).show();

                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });

        } else {


            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("db").child("Users").child(userID);

            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {

                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(intent);
                        finish();

                    } else if (!dataSnapshot.exists() && userName == null || !dataSnapshot.exists() && userName != null) {
                        progress.dismiss();
                        verificarusuario1();

                    } else {
                        progress.dismiss();
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(Tela_login.this, "Seu email não está cadastrado", Toast.LENGTH_SHORT).show();


                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });
        }


    }


    public void verificarusuario2() {

        progress = new DialogProgress();
        progress.show(getSupportFragmentManager(), "1");

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("db").child("Users").child(userID);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    progress.dismiss();

                    FirebaseAuth.getInstance().signOut();

                    Toast.makeText(Tela_login.this, "Você está cadastrado como paciente", Toast.LENGTH_SHORT).show();

                }else{

                    progress.dismiss();

                    Intent intent = new Intent(Tela_login.this, Informar_nome_paciente.class);

                    intent.putExtra("CADASTRAR", 2);

                    startActivity(intent);

                    finish();

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });


    }


    public void verificarusuario1() {

        progress = new DialogProgress();
        progress.show(getSupportFragmentManager(), "1");

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("db").child("Fisioterapeuta").child(userID);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    FirebaseAuth.getInstance().signOut();

                    progress.dismiss();

                    Toast.makeText(Tela_login.this, "Você está cadastrado como fisiterapeuta", Toast.LENGTH_SHORT).show();

                }else{

                    progress.dismiss();

                    Intent intent = new Intent(Tela_login.this, Informar_nome_paciente.class);

                    intent.putExtra("CADASTRAR", 0);

                    startActivity(intent);

                    finish();


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });


    }


}


