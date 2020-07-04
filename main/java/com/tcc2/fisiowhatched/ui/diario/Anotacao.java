package com.tcc2.fisiowhatched.ui.diario;

import android.os.Parcel;
import android.os.Parcelable;



public class Anotacao implements Parcelable {

    private String titulo;
    private String nota;
    private String data;
    private String id;
    private String nomePaciente;



    public Anotacao(){



    }

    public Anotacao(String titulo, String nota, String data, String nomePaciente, String id) {
        this.titulo = titulo;
        this.nota = nota;
        this.data = data;
        this.nomePaciente = nomePaciente;
        this.id = id;
    }

    public String getNomePaciente() {
        return nomePaciente;
    }

    public void setNomePaciente(String nomePaciente) {
        this.nomePaciente = nomePaciente;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public int describeContents() {


        return 0;
    }



    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.titulo);
        dest.writeString(this.nota);
        dest.writeString(this.data);
        dest.writeString(this.nomePaciente);
        dest.writeString(this.id);

    }

    public Anotacao(Parcel in) {
        this.titulo = in.readString();
        this.nota = in.readString();
        this.data = in.readString();
        this.nomePaciente = in.readString();
        this.id = in.readString();

    }

    public static final Creator<Anotacao> CREATOR = new Creator<Anotacao>() {
        @Override
        public Anotacao createFromParcel(Parcel source) {
            return new Anotacao(source);
        }

        @Override
        public Anotacao[] newArray(int size) {
            return new Anotacao[size];
        }
    };
}
