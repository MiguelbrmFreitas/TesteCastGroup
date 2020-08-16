package com.miguelbrmfreitas.testecastgroup.api;

import android.util.Log;

import com.miguelbrmfreitas.testecastgroup.observer.Observer;
import com.miguelbrmfreitas.testecastgroup.observer.Subject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Classe para fazer as chamadas à API
 */
public class RepositoryApiServices implements Subject
{
    private static String TAG = "RepositoryApiServices";
    private String mBaseUrl = "https://teste-cast-group.herokuapp.com/api"; // Endereço base da API

    private ArrayList<Observer> mObservers; // Array de observers inscritos

    private static RepositoryApiServices INSTANCE = null;

    private RepositoryApiServices() {
        mObservers = new ArrayList<>();
    }

    /**
     * Cria um Singleton da classe para manter uma instância única
     * @return      Instância global da classe RepositoryApiServices
     */
    public static RepositoryApiServices getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new RepositoryApiServices();
        }
        return INSTANCE;
    }

    public void getCategories() {
        String url = mBaseUrl + "/categories";

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                notifyObservers(call, response, ResponseType.GET_CATEGORIES);
            }
        });

    }

    @Override
    public void registerObserver(Observer observer) {
        if(!mObservers.contains(observer)) {
            mObservers.add(observer);
        }
    }

    @Override
    public void removeObserver(Observer observer) {
        if(!mObservers.contains(observer)) {
            mObservers.remove(observer);
        }
    }

    @Override
    public void notifyObservers(@NotNull Call call, @NotNull Response response, ResponseType responseType) {
        for (Observer observer : mObservers) {
            observer.onApiServiceFinished(call, response, responseType);
        }
    }


}
