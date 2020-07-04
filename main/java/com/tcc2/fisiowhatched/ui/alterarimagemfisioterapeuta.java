package com.tcc2.fisiowhatched.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.tcc2.fisiowhatched.Main2Activity;
import com.tcc2.fisiowhatched.R;
import com.tcc2.fisiowhatched.Util.DialogProgress;
import com.tcc2.fisiowhatched.Util.Permissao;
import com.tcc2.fisiowhatched.Util.util;
import com.tcc2.fisiowhatched.ui.Pacientes.ListaPerfilPaciente;
import com.tcc2.fisiowhatched.ui.Pacientes.Perfil_paciente;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;


public class alterarimagemfisioterapeuta extends Fragment {

    private ImageView imageView;
    private Button button;
    private Uri uri_imagem;
    private FirebaseStorage storage;


    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_alterarimagemfisioterapeuta, container, false);

        imageView = root.findViewById(R.id.imageViewStorageUploadfisioterapeuta);
        button = root.findViewById(R.id.btn_upload_enviarfisioterapeuta);

        // ((MainActivity)getActivity()).getSupportActionBar().setTitle("" + exercicio.getTitulo());
        ((Main2Activity)getActivity()).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ((Main2Activity)getActivity()).getSupportActionBar().setCustomView(R.layout.custom_toolbar);
        TextView textView = ((Main2Activity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.toolbar_title);
        textView.setText("Alterar Imagem");
        ImageView perfil = ((Main2Activity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.toolbar_image);
        ImageView voltar = ((Main2Activity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.toolbar_back);


        //voltar.setVisibility(View.GONE);

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

                Fragment fragmentA = new perfilfisioterapeuta();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, fragmentA, "Fragment_A");
                fragmentTransaction.commit();

            }
        });

        storage = FirebaseStorage.getInstance();

        permissao();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                button_enviar();

            }
        });


   return  root;
    }


    public void button_enviar() {


        if (util.isNetworkAvailable(getContext())) {

            if (imageView.getDrawable() != null) {

                remover_imagem();

                upload_imagem2();


            } else {

                Toast.makeText(getContext(), "Não existe nenhuma imagem para upload", Toast.LENGTH_SHORT).show();


            }


        } else {

            Toast.makeText(getContext(), "Sem conexão com a internet", Toast.LENGTH_SHORT).show();

        }


    }


    public void upload_imagem2() {


        final DialogProgress dialogProgress = new DialogProgress();

        dialogProgress.show(getFragmentManager(), "");

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();


        StorageReference reference = storage.getReference().child("imagensdeperfil").child(userID);
        final StorageReference nome_imagem = reference.child("perfil" + userID + ".jpg");


        Glide.with(getContext()).asBitmap().load(uri_imagem).apply(new RequestOptions().override(1024, 768))
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {

                        Toast.makeText(getContext(), "Erro ao converter imagem :(", Toast.LENGTH_SHORT).show();

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {


                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        resource.compress(Bitmap.CompressFormat.JPEG, 70, bytes);
                        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes.toByteArray());


                        try {
                            bytes.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        UploadTask uploadTask = nome_imagem.putStream(inputStream);

                        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {


                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                return nome_imagem.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {


                                if (task.isSuccessful()) {

                                    dialogProgress.dismiss();

                                    Uri uri = task.getResult();

                                    userprofile(uri);

                                    String url_imagem = uri.toString();


                                    Fragment fragmentA = new perfilfisioterapeuta();
                                    FragmentManager fragmentManager = getFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("uri", url_imagem);
                                    fragmentA.setArguments(bundle);
                                    fragmentTransaction.replace(R.id.nav_host_fragment, fragmentA, "Fragment_A");
                                    fragmentTransaction.commit();



                                } else {

                                    dialogProgress.dismiss();

                                    Toast.makeText(getContext(), "Erro ao realizar upload :C", Toast.LENGTH_SHORT).show();


                                }

                            }
                        });


                        return false;
                    }
                }).submit();


    }

    private void userprofile(Uri uri) {

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                .setPhotoUri(uri).build() ;

        user.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {



                }
            }
        });


    }






    public void remover_imagem() {


        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference reference = storage.getReference().child(userID).child("perfil" + userID + ".jpg");


        reference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    Toast.makeText(getContext(), "Imagem deletada com sucesso!", Toast.LENGTH_SHORT).show();

                }else{

                    //Toast.makeText(getContext(), "Erro ao remover a imagem " + task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });




    }






    private void permissao() {


        String permissoes[] = new String[]{

                Manifest.permission.CAMERA
        };
        Permissao.permissao(getActivity(), 0, permissoes);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        for (int result : grantResults) {

            if (result == PackageManager.PERMISSION_DENIED) {

                Toast.makeText(getContext(), "Aceite as permissões para o app acessar sua câmera ;)", Toast.LENGTH_SHORT).show();
                getActivity().finish();
                break;
            }


        }

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.storageuploadmenu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.galeria:

                obter_imagem_galeria();

                break;

            case R.id.camera:

                item_camera();
                obter_imagem_camera();

                break;
        }

        return super.onOptionsItemSelected(item);
    }


    public void item_camera() {

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {





        } else {


            obter_imagem_camera();

        }


    }


    public void obter_imagem_camera() {


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { // VERSÃO MAIS NOVA

            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

            ContentResolver contentResolver = getActivity().getContentResolver();

            uri_imagem = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri_imagem);

            startActivityForResult(intent, 1);

        } else { // VERSÃO ANTIGA

            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

            File diretorio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

            String nomeImagem = diretorio.getPath() + "/" + "perfil"+ userID  + ".jpg";

            File file = new File(nomeImagem);

            String autorizacao = "teste";

            uri_imagem = FileProvider.getUriForFile(getContext(), autorizacao, file);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri_imagem);

            startActivityForResult(intent, 1);

        }


    }


    public void obter_imagem_galeria() {


        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(Intent.createChooser(intent, "Escolha uma imagem"), 11);


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {


            if (requestCode == 11) {  //REQUEST DA GALERIA


                if (data != null) {  //VERIFICA SE TEM A IMAGEM ESCOLHIDA PELO USUARIO

                    uri_imagem = data.getData();

                    Glide.with(getContext()).asBitmap().load(uri_imagem).listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {

                            Toast.makeText(getContext(), "Erro ao carregar imagem :(", Toast.LENGTH_SHORT).show();

                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    }).into(imageView);

                } else {

                    Toast.makeText(getContext(), "Erro ao carregar imagem :(", Toast.LENGTH_SHORT).show();

                }


            } else if (requestCode == 1) {  //REQUEST DA CAMERA

                if (uri_imagem != null) { //VERIFICA SE A IMAGEM DA CAMERA FOI CARREGADA

                    Glide.with(getContext()).asBitmap().load(uri_imagem).listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {

                            Toast.makeText(getContext(), "Falha ao selecionar imagem :(", Toast.LENGTH_SHORT).show();

                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    }).into(imageView);


                } else {

                    Toast.makeText(getContext(), "Falha ao tirar foto :(", Toast.LENGTH_SHORT).show();

                }


            }
        }

    }



}
