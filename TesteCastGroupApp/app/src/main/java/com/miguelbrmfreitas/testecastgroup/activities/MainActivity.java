package com.miguelbrmfreitas.testecastgroup.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.miguelbrmfreitas.testecastgroup.R;
import com.miguelbrmfreitas.testecastgroup.adapters.CoursesAdapter;
import com.miguelbrmfreitas.testecastgroup.api.RepositoryApiServices;
import com.miguelbrmfreitas.testecastgroup.api.ResponseType;
import com.miguelbrmfreitas.testecastgroup.components.CustomButton;
import com.miguelbrmfreitas.testecastgroup.components.ToastMaker;
import com.miguelbrmfreitas.testecastgroup.fragments.DeleteDialogFragment;
import com.miguelbrmfreitas.testecastgroup.models.Category;
import com.miguelbrmfreitas.testecastgroup.models.Course;
import com.miguelbrmfreitas.testecastgroup.observer.Observer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Response;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Classe da Activity principal, onde o app começa
 */
public class MainActivity extends AppCompatActivity implements Observer, DeleteDialogFragment.DeleteDialogListener, CourseDetailsActivity.CourseSubmittedListener {

    private String TAG = "MainActivity";

    public static final String KEY_EXTRA = "MAIN_ACTIVITY";

    private RepositoryApiServices mRepository;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private CoursesAdapter mAdapter;
    private ArrayList<Course> mCourses = new ArrayList<Course>();
    private ArrayList<Category> mCategories = new ArrayList<Category>();

    private Handler mHandler;

    private final Context mContext = this;
    private final CourseDetailsActivity.CourseSubmittedListener mCourseSubmittedListener = this;

    private int mCurrentPosition = -1;

    private CustomButton mCustomButton;

    private ProgressBar mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);

        // Inicializa o Handler para as threads
        mHandler = new Handler(Looper.getMainLooper());

        mSpinner = findViewById(R.id.activity_main_progress_bar);

        // Faz o binding da RecyclerView com os cursos
        mRecyclerView = findViewById(R.id.activity_main_recycler_view);

        // Cria e configura o layout manager (com condifurações da disposição do layout da lista)
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setStackFromEnd(false);
        mLayoutManager.setReverseLayout(false);

        mRecyclerView.setLayoutManager(mLayoutManager); // seta o layout manager

        // Cria o adapter e seta na RecyclerView
        mAdapter = new CoursesAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        mRepository = RepositoryApiServices.getInstance(); // Recebe a instância global do repositório da API
        mRepository.registerObserver(this); // Registra a MainActivity como observer

        mRepository.getCategories(); // Recebe as categorias da API

        mCustomButton = findViewById(R.id.activity_main_fab);
        mCustomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Cria intent para trocar de Activity
                Intent intent = new Intent(mContext, CourseDetailsActivity.class);

                // Configura os dados a serem passados para a Activity
                Bundle bundle = new Bundle();

                bundle.putBoolean("is_editing", false);

                CourseDetailsActivity.setInitialConfig(mCourseSubmittedListener, mCategories);

                intent.putExtra(CourseDetailsActivity.KEY_EXTRA, bundle);
                startActivity(intent);
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
        // Gson gson = new Gson();

        JSONObject jsonObject = new JSONObject();
        JSONObject categoryJson = new JSONObject();
        try {
            // Cria os campos para enviar no POST
            jsonObject.put("description", course.getDescription());
            jsonObject.put("start_date", (course.getStartDate().getTime() * 1000) );
            jsonObject.put("end_date", (course.getEndDate().getTime() * 1000) );
            jsonObject.put("students_per_class", course.getStudentsPerClass());
            categoryJson.put("_id", course.getCategory().getId());
            jsonObject.put("category", categoryJson);

            String jsonString = jsonObject.toString(); // Transforma o objeto de Course em JSON String

            Log.i(TAG, jsonString);

            mRepository.postCourse(jsonString); // Chama o método POST da API
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cria um objeto Category a partir de um JSDN Object
     * @param jsonObject    Objeto JSON
     * @return      Objeto da model Category
     */
    private Category buildCategory(JSONObject jsonObject) {
        try {
            // Parsing do objeto JSON
            String code = jsonObject.getString("code");
            String description = jsonObject.getString("description");
            String id = jsonObject.getString("_id");

            return new Category(code, description, id);
        } catch (JSONException e) {
            e.printStackTrace();

            return null;
        }
    }

    /**
     * Cria um objeto Category a partir de um JSDN Object
     * @param jsonObject    Objeto JSON
     * @return      Objeto da model Category
     */
    private Course buildCourse(JSONObject jsonObject) {
        try {
            // Parsing do objeto JSON
            long startTimestamp = jsonObject.getLong("start_date");
            long endTimestamp = jsonObject.getLong("end_date");
            String description = jsonObject.getString("description");
            String id = jsonObject.getString("_id");
            int studentsNumber = jsonObject.has("students_per_class")? jsonObject.getInt("students_per_class") : 0;
            // Parse composto de category
            JSONObject categoryJsonObject = jsonObject.getJSONObject("category");
            Category category = buildCategory(categoryJsonObject);

            Course course = new Course(description, new Date((startTimestamp * 1000)), new Date((endTimestamp * 1000)), category, studentsNumber);
            course.setId(id);

            return course;
        } catch (JSONException e) {
            e.printStackTrace();

            return null;
        }
    }

    /**
     * Seta o array de categories
     * @param jsonArray     JSON Array a fazer o parser
     */
    private void setCategories(JSONArray jsonArray) {
        try {
            // Faz a iteração pelo JSON Array e cria o array da model Category
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                Category category = buildCategory(jsonObject);

                if (category != null) {
                    mCategories.add(category);
                    Log.i(TAG, mCategories.get(i).getDescription());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Seta o array de courses
     * @param jsonArray     JSON Array a fazer o parser
     */
    private void setCourses(JSONArray jsonArray) {
        try {
            // Faz a iteração pelo JSON Array e cria o array da model Course
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Course course = buildCourse(jsonObject);
                if (course != null) {
                    mCourses.add(course);
                    Log.i(TAG, mCourses.get(i).getDescription());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onApiServiceFinished(@NotNull Call call, @NotNull Response response, ResponseType responseType) {
        // Chama um método para lidar com cada resposta da API
        switch(responseType) {
            case GET_CATEGORIES:
                if (mCategories.isEmpty()) {
                    getCategoriesResponse(call, response);
                }
                break;
            case GET_COURSES:
                if (mCourses.isEmpty()) {
                    getCoursesResponse(call, response);
                }
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
            JSONArray jsonArray = new JSONArray(responseBody); // JSON Array de resposta
            setCategories(jsonArray);
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
            JSONArray jsonArray = new JSONArray(responseBody); // JSON Array de resposta
            Log.i(TAG, responseBody);

            // Constrói os cursos a partir da resposta do servidor
            setCourses(jsonArray);

            // Atualiza a UI na thread principal
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mAdapter.setData(mCourses); // Coloca os cursos no adapter da RecyclerView
                    mSpinner.setVisibility(View.GONE); // Some com o loading
                    mCustomButton.setVisibility(View.VISIBLE); // Torna o botão visível após carregarem os dados da API
                }
            });

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
        try {
            if (response.isSuccessful()) {
                // Avisa o resultado de sucesso na tela
                ToastMaker.showToast(this, getString(R.string.post_success));
            } else {
                // Pega os erros
                Log.i(TAG, "Deu treta");
                String responseBody = response.body().string();
                JSONObject responseJson = new JSONObject(responseBody); // JSON Object da resposta
                JSONArray jsonArray = responseJson.getJSONArray("errors"); // JSON Array de resposta
                Log.i(TAG, responseBody);
                String[] errorsArray = new String[jsonArray.length()];
                // Pega todas as mensagens de erro
                for(int k = 0; k < jsonArray.length(); k++) {
                    errorsArray[k] = jsonArray.getJSONObject(k).getString("msg");
                    ToastMaker.showToast(this, errorsArray[k]);
                }

//                Intent intent = new Intent(mContext, CourseDetailsActivity.class);
//
//                // Configura os dados a serem passados para a Activity
//                Bundle bundle = new Bundle();
//
//                bundle.putStringArray("errors", errorsArray);
//
////                CourseDetailsActivity.setInitialConfig(mCourseSubmittedListener, mCategories);
//
//                intent.putExtra(CourseDetailsActivity.KEY_EXTRA, bundle);
//                startActivity(intent);
//                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, response.message());
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
        if (response.isSuccessful()) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mCourses.remove(mCurrentPosition); // Remove
                    mAdapter.setData(mCourses);
                    Toast.makeText(mContext, "Curso deletado", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    /**
     * Mostra o fragment de deletar curso
     * @param courseId      ID do curso
     */
    public void showDeleteDialog(String courseId, int position) {
        mCurrentPosition = position;
        FragmentManager fm = getSupportFragmentManager();
        DeleteDialogFragment deleteDialogFragment = DeleteDialogFragment.newInstance(courseId);
        deleteDialogFragment.show(fm, "fragment_delete_dialog");
    }

    @Override
    public void deleteCourse(String courseId, boolean delete) {
        if (delete) {
            // Chama a rota de delete
            mRepository.deleteCourse(courseId);
        }
    }

    @Override
    public void onSubmit(Course newCourse) {
        Log.i(TAG, newCourse.getDescription());
        postCourse(newCourse);
    }
}