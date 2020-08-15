package com.miguelbrmfreitas.testecastgroup.api;

import com.miguelbrmfreitas.testecastgroup.observer.Observer;
import com.miguelbrmfreitas.testecastgroup.observer.Subject;

/**
 * Classe para fazer as chamadas à API
 */
public class RepositoryApiServices implements Subject
{
    private static String TAG = "RepositoryApiServices";
    private String mBaseUrl = "https://teste-cast-group.herokuapp.com/api/";

    public void getCategories() {

    }

    @Override
    public void registerObserver(Observer observer) {

    }

    @Override
    public void removeObserver(Observer observer) {

    }

    @Override
    public void notifyObservers() {

    }
}
