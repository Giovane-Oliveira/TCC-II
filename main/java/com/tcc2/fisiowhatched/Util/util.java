package com.tcc2.fisiowhatched.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class util {


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager;
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean verificarCampos(Context context, String valor, String valor1, String valor2, String valor3, String valor4){


        if(valor.trim().isEmpty() ||  valor.trim().isEmpty() ||  valor1.trim().isEmpty() ||  valor2.trim().isEmpty() ||  valor3.trim().isEmpty() ||  valor4.trim().isEmpty()){

            AlertDialog.Builder dig = new AlertDialog.Builder(context);
            dig.setTitle("Aviso!");
            dig.setMessage("Por favor, preencha os campos.");
            dig.setNeutralButton("ok", null);
            dig.show();

            return false;
        }else{

            return true;
        }

    }

    public static boolean verificarCampos(Context context, String valor, String valor2){


        if(valor.trim().isEmpty() || valor2.trim().isEmpty() ){

            AlertDialog.Builder dig = new AlertDialog.Builder(context);
            dig.setTitle("Aviso!");
            dig.setMessage("Por favor, preencha os campos.");
            dig.setNeutralButton("ok", null);
            dig.show();

            return false;
        }else{

            return true;
        }

    }

    public static boolean verificarCampos(Context context, String valor){


        if(valor.trim().isEmpty()){

            AlertDialog.Builder dig = new AlertDialog.Builder(context);
            dig.setTitle("Aviso!");
            dig.setMessage("Por favor, preencha os campos.");
            dig.setNeutralButton("ok", null);
            dig.show();

            return false;
        }else{

            return true;
        }

    }


    public static void operro(Context context,String resposta){

        if(resposta.contains("least 6 characters")){

            AlertDialog.Builder dig = new AlertDialog.Builder(context);
            dig.setTitle("Erro");
            dig.setMessage("Sua senha contém menos de 6 caracteres");
            dig.setNeutralButton("ok", null);
            dig.show();

        }

        else if(resposta.contains("address is badly")){
            AlertDialog.Builder dig = new AlertDialog.Builder(context);
            dig.setTitle("Erro");
            dig.setMessage("O formato de email está incorreto");
            dig.setNeutralButton("ok", null);
            dig.show();

        }
//#################################cadastro###################################
        else if(resposta.contains("address is already")){
            AlertDialog.Builder dig = new AlertDialog.Builder(context);
            dig.setMessage("Seu email já está cadastrado");
            dig.setNeutralButton("ok", null);
            dig.show();

        }
//###############################################################################
        else if(resposta.contains("interrupted connection")){
            AlertDialog.Builder dig = new AlertDialog.Builder(context);
            dig.setTitle("Erro");
            dig.setMessage("Sem conexão com o firebase");
            dig.setNeutralButton("ok", null);
            dig.show();

        }

        else if(resposta.contains("password is invalid")){
            AlertDialog.Builder dig = new AlertDialog.Builder(context);
            dig.setTitle("Erro");
            dig.setMessage("Senha inválida!");
            dig.setNeutralButton("ok", null);
            dig.show();

        }

        else if(resposta.contains("There is no user")){
            AlertDialog.Builder dig = new AlertDialog.Builder(context);
            dig.setTitle("Erro");
            dig.setMessage("Este e-mail não está cadastrado!");
            dig.setNeutralButton("ok", null);
            dig.show();

        } else if(resposta.contains("blocked all requests")){
            AlertDialog.Builder dig = new AlertDialog.Builder(context);
            dig.setTitle("Erro");
            dig.setMessage("Atingiu o número de tentativas de acesso! Tente novamente mais tarde.");
            dig.setNeutralButton("ok", null);
            dig.show();

        } else{

            AlertDialog.Builder dig = new AlertDialog.Builder(context);
            dig.setTitle("Erro");
            dig.setMessage("" + resposta);
            dig.setNeutralButton("ok", null);
            dig.show();

        }

    }

}
