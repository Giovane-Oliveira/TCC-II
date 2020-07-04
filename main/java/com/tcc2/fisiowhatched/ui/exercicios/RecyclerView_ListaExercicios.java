package com.tcc2.fisiowhatched.ui.exercicios;

import android.content.Context;


import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.tcc2.fisiowhatched.R;
import com.tcc2.fisiowhatched.Util.DialogProgress;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class RecyclerView_ListaExercicios extends RecyclerView.Adapter<RecyclerView_ListaExercicios.ViewHolder> {


    private Context context;
    private List<Exercicios> exercicios;
    private ClickEmpresa clickEmpresa;


    public RecyclerView_ListaExercicios(Context context, List<Exercicios> exercicios, ClickEmpresa clickEmpresa){

        this.context = context;
        this.exercicios = exercicios;
        this.clickEmpresa = clickEmpresa;


    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {



        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_exercicios,parent,false);

        ViewHolder holder = new ViewHolder(view);

        view.setTag(holder);

        return holder;
    }



    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {



        final Exercicios exercicios = this.exercicios.get(position);


        holder.titulo.setText(exercicios.getTitulo());

        holder.descricao.setText(exercicios.getDescricao());

      /*  Picasso.with(context).load(exercicios.getUrlimagem())
                .resize(100, 100)
                .centerCrop()
                .into(holder.imagem);*/


            holder.progressBar.setVisibility(View.VISIBLE);



        Picasso.with(context).load(exercicios.getUrlimagem()).into(holder.imagem, new com.squareup.picasso.Callback() {

            @Override
            public void onSuccess() {

                holder.progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onError() {

              holder.progressBar.setVisibility(View.GONE);

            }
        });








        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clickEmpresa.click_Empresa(exercicios);


            }
        });

    }




    @Override
    public int getItemCount() {
        return exercicios.size();
    }



    public interface ClickEmpresa{


        void click_Empresa(Exercicios exercicios);

    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        LinearLayout item;
        TextView titulo;
        TextView descricao;
        ImageView imagem;
        ProgressBar progressBar;



        public ViewHolder(View itemView) {
            super(itemView);


            item = itemView.findViewById(R.id.itemExercicio);
            titulo = itemView.findViewById(R.id.titulo_exercicio);
            descricao = itemView.findViewById(R.id.descricao_exercicio);
            imagem = itemView.findViewById(R.id.imagem_exercicio);
            progressBar = itemView.findViewById(R.id.progressBarExercicios);






        }





        }


    }




