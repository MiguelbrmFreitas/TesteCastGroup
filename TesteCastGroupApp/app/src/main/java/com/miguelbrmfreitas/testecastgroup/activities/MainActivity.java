package com.miguelbrmfreitas.testecastgroup.activities;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.miguelbrmfreitas.testecastgroup.R;
import com.miguelbrmfreitas.testecastgroup.adapters.CoursesAdapter;
import com.miguelbrmfreitas.testecastgroup.api.RepositoryApiServices;
import com.miguelbrmfreitas.testecastgroup.api.ResponseType;
import com.miguelbrmfreitas.testecastgroup.models.Course;
import com.miguelbrmfreitas.testecastgroup.observer.Observer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Response;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

/**
 * Classe da Activity principal, onde o app começa
 */
public class MainActivity extends AppCompatActivity implements Observer {

    private String TAG = "MainActivity";

    private RepositoryApiServices mRepository;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    CoursesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);

        // Faz o binding da RecyclerView com os cursos
        mRecyclerView = findViewById(R.id.activity_main_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRepository = RepositoryApiServices.getInstance(); // Recebe a instância global do repositório da API
        mRepository.registerObserver(this); // Registra a MainActivity como observer

        mRepository.getCategories(); // Recebe as categorias da API

        FloatingActionButton fab = findViewById(R.id.activity_main_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void postCourse(Course course) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(course); // Transforma o objeto de Course em JSON

        mRepository.postCourse(jsonString); // Chama o método POST da API
    }

    @Override
    public void onApiServiceFinished(@NotNull Call call, @NotNull Response response, ResponseType responseType) {
        // Chama um método para lidar com cada resposta da API
        switch(responseType) {
            case GET_CATEGORIES:
                getCategoriesResponse(call, response);
                break;
            case GET_COURSES:
                getCoursesResponse(call, response);
                break;
            case POST_COURSES:
                postCourseResponse(call, response);
                break;
            case PUT_COURSES:
                putCourseResponse(call, response);
                break;
            case DELETE_COURSES:
                deleteCourseResponse(call, response);
                break;
            default:
                break;
        }
    }

    /**
     * Classe para definir a execução após a chamada GET /categories da API
     * @param call      Objeto de chamada
     * @param response  Objeto da resposta
     */
    private void getCategoriesResponse(@NotNull Call call, @NotNull Response response) {
        Log.i(TAG, response.message());
        try {
            String responseBody = response.body().string();
            JSONArray jsonArray = new JSONArray(responseBody);
            Log.i(TAG, responseBody);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Recebe os cursos depois que já tiver recebido as categorias
        mRepository.getCourses();
    }

    /**
     * Classe para definir a execução após a chamada GET /courses da API
     * @param call      Objeto de chamada
     * @param response  Objeto da resposta
     */
    private void getCoursesResponse(@NotNull Call call, @NotNull Response response) {
        Log.i(TAG, response.message());
        try {
            String responseBody = response.body().string();
            JSONArray jsonArray = new JSONArray(responseBody);
            Log.i(TAG, responseBody);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Classe para definir a execução após a chamada POST /courses da API
     * @param call      Objeto de chamada
     * @param response  Objeto da resposta
     */
    private void postCourseResponse(@NotNull Call call, @NotNull Response response) {

    }

    /**
     * Classe para definir a execução após a chamada PUT /courses/:id da API
     * @param call      Objeto de chamada
     * @param response  Objeto da resposta
     */
    private void putCourseResponse(@NotNull Call call, @NotNull Response response) {

    }

    /**
     * Classe para definir a execução após a chamada DELETE /courses/:id da API
     * @param call      Objeto de chamada
     * @param response  Objeto da resposta
     */
    private void deleteCourseResponse(@NotNull Call call, @NotNull Response response) {

    }
}