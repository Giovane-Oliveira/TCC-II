package com.tcc2.fisiowhatched.ui.Pacientes;

import android.os.Parcel;
import android.os.Parcelable;

import com.tcc2.fisiowhatched.ui.exercicios.Exercicios;


public class Paciente extends Exercicios implements Parcelable {


    private String datanasc;
    private String cpf;
    private String email;
    private String id;
    private String nome;
    private String url_imagem;


    public Paciente() {
    }

    public Paciente(String cpf, String datanasc, String email, String id, String nome, String url_imagem) {
        this.cpf = cpf;
        this.datanasc = datanasc;
        this.email = email;
        this.id = id;
        this.nome = nome;
        this.url_imagem = url_imagem;
    }

    public String getUrl_imagem() {
        return url_imagem;
    }

    @Override
    public void setUrl_imagem(String url_imagem) {
        this.url_imagem = url_imagem;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getDatanasc() {
        return datanasc;
    }

    public void setDatanasc(String datanasc) {
        this.datanasc = datanasc;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(this.cpf);
        dest.writeString(this.datanasc);
        dest.writeString(this.email);
        dest.writeString(this.id);
        dest.writeString(this.nome);
        dest.writeString(this.url_imagem);

    }

    public Paciente(Parcel in) {
        this.cpf = in.readString();
        this.datanasc = in.readString();
        this.email = in.readString();
        this.id = in.readString();
        this.nome = in.readString();
        this.url_imagem = in.readString();

    }


    public static final Creator<Paciente> CREATOR = new Creator<Paciente>() {
        @Override
        public Paciente createFromParcel(Parcel source) {
            return new Paciente(source);
        }

        @Override
        public Paciente[] newArray(int size) {
            return new Paciente[size];
        }
    };
}
