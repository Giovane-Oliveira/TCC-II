package com.tcc2.fisiowhatched.ui.Pacientes;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tcc2.fisiowhatched.R;
import com.tcc2.fisiowhatched.Util.DialogProgress;
import com.tcc2.fisiowhatched.ui.diario.Anotacao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class RecyclerView_ListaPacientescomnota extends RecyclerView.Adapter<RecyclerView_ListaPacientescomnota.ViewHolder> {


    private Context context;
    private List<Paciente> pacientes;
    private RecyclerView_ListaPacientescomnota.ClickPaciente clickPaciente;
    Calendar myCalendar = Calendar.getInstance();
    private int i;


    public RecyclerView_ListaPacientescomnota(Context context, List<Paciente> pacientes, RecyclerView_ListaPacientescomnota.ClickPaciente clickPaciente, int i) {

        this.context = context;
        this.pacientes = pacientes;
        this.clickPaciente = clickPaciente;
        this.i = i;


    }


    @NonNull
    @Override
    public RecyclerView_ListaPacientescomnota.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listapacientescomnota, parent, false);

        RecyclerView_ListaPacientescomnota.ViewHolder holder = new RecyclerView_ListaPacientescomnota.ViewHolder(view);

        view.setTag(holder);

        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerView_ListaPacientescomnota.ViewHolder holder, final int position) {


        holder.item.setVisibility(View.GONE);
        holder.descricao.setVisibility(View.GONE);
        holder.titulo.setVisibility(View.GONE);
        holder.imagem.setVisibility(View.GONE);
        holder.progressBar.setVisibility(View.GONE);

        final Paciente pacientes = this.pacientes.get(position);


        String myFormat = "dd/MM/yyyy"; //In which you need put here
        final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt", "BR"));
        final String date = sdf.format(myCalendar.getTime());


        Query query1 = FirebaseDatabase.getInstance().getReference().child("db").child("Status").child(pacientes.getId());
        query1.addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    String data = dataSnapshot.child("data").getValue(String.class);
                    String status = dataSnapshot.child("status").getValue(String.class);
                    final String id = dataSnapshot.child("id").getValue(String.class);

                    if (status.equals("lido") && data.equals(date)) {

                        holder.status.setText("Lido");
                        holder.status.setTextColor(Color.BLUE);
                        

                        Query query2 = FirebaseDatabase.getInstance().getReference().child("db").child("Users").child(id);
                        query2.addListenerForSingleValueEvent(new ValueEventListener() {


                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (!dataSnapshot.exists()) {

                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("db").child("Status").child("" + id);

                                    databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                Toast.makeText(context, "removido", Toast.LENGTH_SHORT).show();


                                            }
                                        }
                                    });

                                    DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference().child("db").child("Diario").child("" + id);

                                    databaseReference2.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                Toast.makeText(context, "removido", Toast.LENGTH_SHORT).show();


                                            }
                                        }
                                    });

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    } else {

                        holder.status.setText(" Não Lido");
                        holder.status.setTextColor(Color.RED);

                        Query query2 = FirebaseDatabase.getInstance().getReference().child("db").child("Users").child("" + id);
                        query2.addListenerForSingleValueEvent(new ValueEventListener() {


                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (!dataSnapshot.exists()) {

                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("db").child("Status").child("" + id);


                                    databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {


                                            }
                                        }
                                    });

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }


                } else {


                    Query query4 = FirebaseDatabase.getInstance().getReference().child("db").child("Status");
                    query4.addListenerForSingleValueEvent(new ValueEventListener() {


                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()) {

                              //  List<Status> lista = new ArrayList<Status>();

                                for (DataSnapshot data : dataSnapshot.getChildren()) {


                                    final Status status = data.getValue(Status.class);


                                    Query query5 = FirebaseDatabase.getInstance().getReference().child("db").child("Users").child("" + status.getId());
                                    query5.addListenerForSingleValueEvent(new ValueEventListener() {


                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            if(!dataSnapshot.exists()){

                                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("db").child("Status").child("" + status.getId());


                                                databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {


                                                        }
                                                    }
                                                });


                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }

                                    });



                                }


                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Query query = FirebaseDatabase.getInstance().getReference().child("db").child("Diario").child(pacientes.getId()).orderByChild("data").equalTo(date);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (i == 0) {


                    if (dataSnapshot.exists()) {

                        holder.item.setVisibility(View.VISIBLE);
                        holder.descricao.setVisibility(View.VISIBLE);
                        holder.titulo.setVisibility(View.VISIBLE);
                        holder.imagem.setVisibility(View.VISIBLE);
                        holder.progressBar.setVisibility(View.VISIBLE);

                        holder.titulo.setText(pacientes.getNome());


                        Date dataNascimento = null;
                        try {
                            dataNascimento = sdf.parse(pacientes.getDatanasc());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        int idade = calculaIdade(dataNascimento);


                        holder.descricao.setText("" + pacientes.getEmail() + "\n\n" + idade + " anos");

                        if (pacientes.getUrl_imagem().equals("sem foto")) {

                            holder.imagem.setImageResource(R.drawable.ic_user_login);
                            holder.progressBar.setVisibility(View.GONE);


                        } else {


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


                } else {

                    holder.item.setVisibility(View.VISIBLE);
                    holder.descricao.setVisibility(View.VISIBLE);
                    holder.titulo.setVisibility(View.VISIBLE);
                    holder.imagem.setVisibility(View.VISIBLE);
                    holder.progressBar.setVisibility(View.VISIBLE);

                    holder.titulo.setText(pacientes.getNome());


                    Date dataNascimento = null;
                    try {
                        dataNascimento = sdf.parse(pacientes.getDatanasc());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    int idade = calculaIdade(dataNascimento);


                    holder.descricao.setText("" + pacientes.getEmail() + "\n\n" + idade + " anos");

                    if (pacientes.getUrl_imagem().equals("sem foto")) {

                        holder.imagem.setImageResource(R.drawable.ic_user_login);
                        holder.progressBar.setVisibility(View.GONE);


                    } else {


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

                            String PREFS = "MinhaPreferencia";
                            SharedPreferences.Editor editor = context.getSharedPreferences(PREFS, MODE_PRIVATE).edit();
                            editor.putString("uid", pacientes.getId());
                            editor.putString("status", "lido");
                            editor.commit();


                        }
                    });


                }


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Realtime", "onCancelled", databaseError.toException());
            }
        });


    }


    @Override
    public int getItemCount() {
        return pacientes.size();
    }


    public interface ClickPaciente {


        void click_Paciente(Paciente paciente);

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout item;
        TextView titulo;
        TextView descricao;
        TextView status;
        ImageView imagem;
        ProgressBar progressBar;


        public ViewHolder(View itemView) {
            super(itemView);


            item = itemView.findViewById(R.id.itemlistaPaciente2);
            titulo = itemView.findViewById(R.id.titulolistaPaciente2);
            descricao = itemView.findViewById(R.id.descricaolistaPaciente2);
            imagem = itemView.findViewById(R.id.imagemlistaPaciente2);
            progressBar = itemView.findViewById(R.id.progressBarlistaPaciente2);
            status = itemView.findViewById(R.id.desc);


        }


    }


    public static int calculaIdade(java.util.Date dataNasc) {

        Calendar dataNascimento = Calendar.getInstance();
        dataNascimento.setTime(dataNasc);
        Calendar hoje = Calendar.getInstance();

        int idade = hoje.get(Calendar.YEAR) - dataNascimento.get(Calendar.YEAR);

        if (hoje.get(Calendar.MONTH) < dataNascimento.get(Calendar.MONTH)) {
            idade--;
        } else {
            if (hoje.get(Calendar.MONTH) == dataNascimento.get(Calendar.MONTH) && hoje.get(Calendar.DAY_OF_MONTH) < dataNascimento.get(Calendar.DAY_OF_MONTH)) {
                idade--;
            }
        }

        return idade;
    }


}

