package com.miguelbrmfreitas.testecastgroup.observer;

import com.miguelbrmfreitas.testecastgroup.api.ResponseType;

import org.jetbrains.annotations.NotNull;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Interface que define um Observer para respostas da API
 */
public interface Observer
{
    /**
     * Método para receber a notificação do observer e despachar uma ação de acordo com a resposta
     * @param call              Objeto de chamada
     * @param response          Objeto da resposta
     * @param responseType      Objeto com um ENUM do tipo da resposta para decidir a ação a ser tomada
     */
    void onApiServiceFinished(@NotNull Call call, @NotNull Response response, ResponseType responseType);
}
