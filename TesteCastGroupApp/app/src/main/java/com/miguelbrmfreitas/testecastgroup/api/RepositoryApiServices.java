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
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Classe para fazer as chamadas à API
 */
public class RepositoryApiServices implements Subject
{
    private static String TAG = "RepositoryApiServices";
    // Endereço base da API
    private String mBaseUrl = "https://teste-cast-group.herokuapp.com/api"; // Trocar para SEU_IP:5001/api se for testar localmente

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

    /**
     * Faz a chamada para a API
     * @param request       Objeto do request a ser feito
     * @param client        Objeto do cliente OkHTTP
     * @param responseType  Tipo da resposta para o callback
     */
    private void makeRequest (Request request, OkHttpClient client, final ResponseType responseType) {
        try {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    notifyObservers(call, response, responseType);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Faz a chamada GET para /categories para receber as categorias da API
     */
    public void getCategories() {
        String url = mBaseUrl + "/categories";

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        makeRequest(request, client, ResponseType.GET_CATEGORIES);
    }

    /**
     * Faz a chamada GET para /courses para receber os cursos da API
     */
    public void getCourses() {
        String url = mBaseUrl + "/courses";

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        makeRequest(request, client, ResponseType.GET_COURSES);
    }

    /**
     * Envia um request POST para /courses para cadastrar um novo curso
     * @param jsonString        String do objeto JSON a ser mandado no body da request
     */
    public void postCourse(String jsonString) {
        String url = mBaseUrl + "/courses";

        MediaType mediaType = MediaType.parse("application/json");

        RequestBody requestBody = RequestBody.create(jsonString, mediaType);

        OkHttpClient client = new OkHttpClient();

        // Cria o POST request
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();

        // Executa o POST request e recebe a resposta da API
        makeRequest(request, client, ResponseType.POST_COURSES);
    }

    /**
     * Envia um request POST para /courses/:id para cadastrar um novo curso
     * @param jsonString        String do objeto JSON a ser mandado no body da request
     * @param id                id do curso a ser atualizado
     */
    public void putCourse(String jsonString, String id) {
        String url = mBaseUrl + "/courses/" + id;

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        Log.i(TAG, "" +id);

        RequestBody requestBody = RequestBody.create(jsonString, mediaType);

        OkHttpClient client = new OkHttpClient();

        // Cria o PUT request
        Request request = new Request.Builder()
                .url(url)
                .put(requestBody)
                .header("Accept", "/*")
                .header("Content-Type", "application/json")
                .header("Connection", "keep-alive")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Cache-Control", "no-cache")
                .build();

        // Executa o PUT request e recebe a resposta da API
        makeRequest(request, client, ResponseType.PUT_COURSES);
    }

    /**
     * Envia um request DELETE para /courses/:id para deletar um curso do banco de dados
     * @param id                id do curso a ser deletado
     */
    public void deleteCourse(String id) {
        String url = mBaseUrl + "/courses/" + id;

        OkHttpClient client = new OkHttpClient();

        // Cria o DELETE request
        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();

        Response response = null;

        // Executa o DELETE request e recebe a resposta da API
        makeRequest(request, client, ResponseType.DELETE_COURSES);
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
