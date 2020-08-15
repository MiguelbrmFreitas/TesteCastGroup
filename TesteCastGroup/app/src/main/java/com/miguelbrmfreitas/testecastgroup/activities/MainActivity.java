package com.miguelbrmfreitas.testecastgroup.activities;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.miguelbrmfreitas.testecastgroup.R;
import com.miguelbrmfreitas.testecastgroup.api.ResponseType;
import com.miguelbrmfreitas.testecastgroup.observer.Observer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import okhttp3.Call;
import okhttp3.Response;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import org.jetbrains.annotations.NotNull;

/**
 * Classe da Activity principal, onde o app começa
 */
public class MainActivity extends AppCompatActivity implements Observer {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
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

    @Override
    public void onApiServiceFinished(@NotNull Call call, @NotNull Response response, ResponseType responseType) {
        // Chama um método para lidar com cada resposta da API
        switch(responseType) {
            case GET_CATEGORIES:
                getCategories(call, response);
                break;
            case GET_COURSES:
                getCourses(call, response);
                break;
            case POST_COURSES:
                postCourse(call, response);
                break;
            case PUT_COURSES:
                putCourse(call, response);
                break;
            case DELETE_COURSES:
                deleteCourse(call, response);
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
    private void getCategories(@NotNull Call call, @NotNull Response response) {

    }

    /**
     * Classe para definir a execução após a chamada GET /courses da API
     * @param call      Objeto de chamada
     * @param response  Objeto da resposta
     */
    private void getCourses(@NotNull Call call, @NotNull Response response) {

    }

    /**
     * Classe para definir a execução após a chamada POST /courses da API
     * @param call      Objeto de chamada
     * @param response  Objeto da resposta
     */
    private void postCourse(@NotNull Call call, @NotNull Response response) {

    }

    /**
     * Classe para definir a execução após a chamada PUT /courses/:id da API
     * @param call      Objeto de chamada
     * @param response  Objeto da resposta
     */
    private void putCourse(@NotNull Call call, @NotNull Response response) {

    }

    /**
     * Classe para definir a execução após a chamada DELETE /courses/:id da API
     * @param call      Objeto de chamada
     * @param response  Objeto da resposta
     */
    private void deleteCourse(@NotNull Call call, @NotNull Response response) {

    }
}