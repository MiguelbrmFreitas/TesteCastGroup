package com.miguelbrmfreitas.testecastgroup.observer;

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
     */
    void notifyObservers();
}