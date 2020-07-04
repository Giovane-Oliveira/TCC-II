package com.tcc2.fisiowhatched.ui.Pacientes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tcc2.fisiowhatched.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RecyclerView_ListaPacientes extends RecyclerView.Adapter<RecyclerView_ListaPacientes.ViewHolder> {


    private Context context;
    private List<Paciente> pacientes;
    private RecyclerView_ListaPacientes.ClickPaciente clickPaciente;


    public RecyclerView_ListaPacientes(Context context, List<Paciente> pacientes, ListaPerfilPaciente clickPaciente){

        this.context = context;
        this.pacientes = pacientes;
        this.clickPaciente = clickPaciente;


    }


    @NonNull
    @Override
    public RecyclerView_ListaPacientes.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_pacientes,parent,false);

        RecyclerView_ListaPacientes.ViewHolder holder = new RecyclerView_ListaPacientes.ViewHolder(view);

        view.setTag(holder);

        return holder;
    }



    @Override
    public void onBindViewHolder(@NonNull final RecyclerView_ListaPacientes.ViewHolder holder, int position) {

        holder.progressBar.setVisibility(View.VISIBLE);


        final Paciente pacientes = this.pacientes.get(position);


        holder.titulo.setText(pacientes.getNome());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date dataNascimento = null;
        try {
            dataNascimento = sdf.parse(pacientes.getDatanasc());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int idade = calculaIdade(dataNascimento);



        holder.descricao.setText("" + pacientes.getEmail() + "\n\n" + idade + " anos");

        if(pacientes.getUrl_imagem().equals("sem foto")){

            holder.imagem.setImageResource(R.drawable.ic_user_login);
            holder.progressBar.setVisibility(View.GONE);


        }else{




            Picasso.with(context).load(pacientes.getUrl_imagem()).into(holder.imagem, new com.squareup.picasso.Callback() {

                @Override
                public void onSuccess() {

                    holder.progressBar.setVisibility(View.GONE);

                }

                @Override
                public void onError() {

                    holder.progressBar.setVisibility(View.GONE);

                }
            });





        }


        //holder.imagem.setImageResource(R.drawable.ic_dashboard_black_24dp);





        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clickPaciente.click_Paciente(pacientes);


            }
        });

    }




    @Override
    public int getItemCount() {
        return pacientes.size();
    }



    public interface ClickPaciente{


        void click_Paciente(Paciente paciente);

    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        LinearLayout item;
        TextView titulo;
        TextView descricao;
        ImageView imagem;
        ProgressBar progressBar;



        public ViewHolder(View itemView) {
            super(itemView);


            item = itemView.findViewById(R.id.itemlistaPaciente);
            titulo = itemView.findViewById(R.id.titulolistaPaciente);
            descricao = itemView.findViewById(R.id.descricaolistaPaciente);
            imagem = itemView.findViewById(R.id.imagemlistaPaciente);
            progressBar = itemView.findViewById(R.id.progressBarlistaPaciente);






        }





    }


    public static int calculaIdade(java.util.Date dataNasc) {

        Calendar dataNascimento = Calendar.getInstance();
        dataNascimento.setTime(dataNasc);
        Calendar hoje = Calendar.getInstance();

        int idade = hoje.get(Calendar.YEAR) - dataNascimento.get(Calendar.YEAR);

        if (hoje.get(Calendar.MONTH) < dataNascimento.get(Calendar.MONTH)) {
            idade--;
        }
        else
        {
            if (hoje.get(Calendar.MONTH) == dataNascimento.get(Calendar.MONTH) && hoje.get(Calendar.DAY_OF_MONTH) < dataNascimento.get(Calendar.DAY_OF_MONTH)) {
                idade--;
            }
        }

        return idade;
    }


}

