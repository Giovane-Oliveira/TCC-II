package com.tcc2.fisiowhatched.ui.diario;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tcc2.fisiowhatched.Main2Activity;
import com.tcc2.fisiowhatched.R;

import java.util.List;

public class RecyclerView_ListaDiario extends RecyclerView.Adapter<RecyclerView_ListaDiario.ViewHolder> {


    private Context context;
    private List<Anotacao> notas;
    private ClickEmpresa clickEmpresa;


    public RecyclerView_ListaDiario(Context context, List<Anotacao> notas, ClickEmpresa clickEmpresa) {

        this.context = context;
        this.notas = notas;
        this.clickEmpresa = clickEmpresa;


    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_diario, parent, false);

        ViewHolder holder = new ViewHolder(view);

        view.setTag(holder);

        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {


        final Anotacao anotacoes = this.notas.get(position);


        holder.titulo.setText(anotacoes.getTitulo());

        holder.descricao.setText(anotacoes.getNota());

        /*Picasso.with(context).load(diarios.getUrlimagem())
                .resize(100, 100)
                .centerCrop()
                .into(holder.imagem);*/





        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clickEmpresa.click_Empresa(anotacoes);


            }
        });

    }


    @Override
    public int getItemCount() {
        return notas.size();
    }


    public interface ClickEmpresa {


        void click_Empresa(Anotacao diarios);

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout item;
        TextView titulo;
        TextView descricao;
        CheckBox checkBox;


        public ViewHolder(View itemView) {
            super(itemView);


            item = itemView.findViewById(R.id.itemDiario);
            titulo = itemView.findViewById(R.id.titulo_diario);
            descricao = itemView.findViewById(R.id.descricao_diario);




        }


    }
}




