package com.miguelbrmfreitas.testecastgroup.components;

import android.content.Context;
import android.widget.Toast;

/**
 * Classe para mostrar um Toast na tela
 */
public class ToastMaker
{
    /**
     * Mostra um Toast com um aviso na tela
     * @param context       Contexto da mensagem
     * @param message       String com o conteúdo da mensagem
     */
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
