package com.miguelbrmfreitas.testecastgroup.observer;

import com.miguelbrmfreitas.testecastgroup.api.ResponseType;

import org.jetbrains.annotations.NotNull;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Interface que define um Suvject ao qual os observers vão se inscrever
 */
public interface Subject {
    /**
     * Registra um observer
     * @param observer      Observer a ser registrado
     */
    void registerObserver(Observer observer);

    /**
     * Remove um observer
     * @param observer      Observer a ser removido
     */
    void removeObserver(Observer observer);

    /**
     * Notifica os observers sobre mudanças
     * @param call              Objeto de chamada
     * @param response          Objeto da resposta
     * @param responseType      Objeto com um ENUM do tipo da resposta para decidir a ação a ser tomada
     */
    void notifyObservers(@NotNull Call call, @NotNull Response response, ResponseType responseType);
}