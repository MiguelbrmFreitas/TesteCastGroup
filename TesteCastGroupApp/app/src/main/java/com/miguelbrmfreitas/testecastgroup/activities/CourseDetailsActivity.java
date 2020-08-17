package com.miguelbrmfreitas.testecastgroup.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.miguelbrmfreitas.testecastgroup.R;
import com.miguelbrmfreitas.testecastgroup.api.ResponseType;
import com.miguelbrmfreitas.testecastgroup.components.CustomButton;
import com.miguelbrmfreitas.testecastgroup.components.ToastMaker;
import com.miguelbrmfreitas.testecastgroup.models.Category;
import com.miguelbrmfreitas.testecastgroup.models.Course;
import com.miguelbrmfreitas.testecastgroup.observer.Observer;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import okhttp3.Call;
import okhttp3.Response;

public class CourseDetailsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener
{
    private String TAG = "CourseDetailsActivity";

    public static final String KEY_EXTRA = "COURSE_DETAILS_ACTIVITY";

    private EditText mStartDatePicker;
    private EditText mEndDatePicker;
    private EditText mEditTextDescription;
    private EditText mStudentsPicker;
    private NumberPicker mCategoryPicker;

    private CustomButton mCustomButton;

    private DatePickerDialog.OnDateSetListener mDate = this;
    private Context mContext = this;

    private Calendar mCalendar = Calendar.getInstance();

    private final int START_DATE = 1;
    private final int END_DATE = 2;

    private int mDatePickerClicked = START_DATE;

    private static ArrayList<Category> mCategories = new ArrayList<Category>();

    private static CourseDetailsActivity.CourseSubmittedListener mCourseSubmittedListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);
        Toolbar toolbar = findViewById(R.id.activity_course_details_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Coloca o botão de voltar

        // Faz binding dos componentes
        mStartDatePicker = findViewById(R.id.fragment_course_details_start_date_picker);
        mEndDatePicker = findViewById(R.id.activity_course_details_end_date_picker);
        mEditTextDescription = findViewById(R.id.activity_course_details_description_edit_text);
        mStudentsPicker = findViewById(R.id.activity_course_details_students_number_picker);
        mCategoryPicker = findViewById(R.id.activity_course_details_category_picker);
        mCustomButton = findViewById(R.id.activity_course_details_fab);

        // Recebendo os argumentos do Bundle
        Bundle bundle = getIntent().getBundleExtra(KEY_EXTRA);
        final boolean isEditing = bundle.getBoolean("is_editing");

        Log.i(TAG, "is editing " + isEditing);

        // Configurando o picker de categorias
        mCategoryPicker.setMinValue(1);
        mCategoryPicker.setMaxValue(4);
        if (mCategories.size() > 0) {
            setCategories(mCategories);
        }

        // Só faz se estiver editando (PUT)
        if (isEditing) {
            // Recupera do bundle
            Date startDate = new Date(bundle.getLong("start_date"));
            Date endDate = new Date(bundle.getLong("end_date"));
            String description = bundle.getString("description");
            int studentsNumber = bundle.getInt("students_number");
            String categoryId = bundle.getString("category_id");
            int categoryCode = bundle.getInt("category_code");

            // Seta os estados iniciais
            //mStartDatePicker.updateDate(startDate.getYear(), startDate.getMonth(), startDate.getDay());
            //mEndDatePicker.updateDate(endDate.getYear(), endDate.getMonth(), endDate.getDay());
            mEditTextDescription.setText(description);
            //mStudentsPicker.setValue(studentsNumber);
            mCategoryPicker.setValue(categoryCode);
        } else {
            // Estados iniciais para o caso do POST (novo curso)
            mCategoryPicker.setValue(1);

            // Configuração de datas
            Date newStartDate = new Date();
            Date newEndDate = getDayPlusOne(new Date());

            mStartDatePicker.setText(dateToString(newStartDate));
            mEndDatePicker.setText(dateToString(newEndDate));

            mStudentsPicker.setText("1");
        }

        // Abre o dialog para escolher uma data inicial
        mStartDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDateDialog(true);
            }
        });

        // Abre o dialog para escolher uma data final
        mEndDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDateDialog(false);
            }
        });

        mCustomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isEditing) {
                    // Valida se todos os campos necessários estão preenchidos
                    Log.i(TAG, "E aeeee");
                    boolean validationPassed = validateFields();
                    if (validationPassed) {
                        Intent intent = new Intent(mContext, MainActivity.class);

                        // Constrói o curso a partir dos fields e envia para a MainActivity
                        Course newCourse = buildCourse();
                        mCourseSubmittedListener.onSubmit(newCourse);

                        finish();

                        ToastMaker.showToast(mContext, "Curso adicionado!");
                        startActivity(intent);
                    }
                } else {
                    Log.i(TAG, "Não faz sentido");
                }
            }
        });
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        String myFormat = "dd/MM/yyyy"; // Formato da data
        final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt","BR")); // Colocando a formatação em PT-BR

        // Atualiza o objeto de calendário
        mCalendar.set(Calendar.YEAR, datePicker.getYear());
        mCalendar.set(Calendar.MONTH, datePicker.getMonth());
        mCalendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());

        // Altera o edit text correto
        if (mDatePickerClicked == START_DATE) {
            updateStartDateLabel(sdf);
        } else {
            updateEndDateLabel(sdf);
        }
    }

    private void setCategories(ArrayList<Category> categories) {
        String[] arrayCategories = new String[categories.size()];
        for (int j = 0; j < categories.size(); j++) {
            arrayCategories[j] = categories.get(j).getDescription();
        }

        mCategoryPicker.setDisplayedValues(arrayCategories);
    }

    /**
     * Classe para retornar a data atual mais um dia
     * @return      Data acrescida de um dia
     */
    private Date getDayPlusOne(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, 1);
        date = c.getTime();

        return date;
    }

    /**
     * Coloca no edit text de data inicial a data escolhida
     * @param sdf   Formatação da data
     */
    private void updateStartDateLabel(SimpleDateFormat sdf) {
        mStartDatePicker.setText(sdf.format(mCalendar.getTime()));
    }

    /**
     * Coloca no edit text de data final a data escolhida
     * @param sdf   Formatação da data
     */
    private void updateEndDateLabel(SimpleDateFormat sdf) {
        mEndDatePicker.setText(sdf.format(mCalendar.getTime()));
    }

    /**
     * Cria um diálogo com um picker para setar a data
     * @param isStart       Diz se é a data de início ou não
     */
    private void createDateDialog(boolean isStart) {
        mDatePickerClicked = isStart? START_DATE: END_DATE; // Seta o date clicado
        // Cria o dialog para receber o input da data e chama o método para visualizar
        new DatePickerDialog(CourseDetailsActivity.this, mDate, mCalendar
                .get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    /**
     * Valida os fields da Activity
     * @return      true se passar no teste, false calso contrário
     */
    private boolean validateFields() {
        try {
            // Variáveis para validação
            String description = mEditTextDescription.getText().toString();
            Date startDate = stringToDate(mStartDatePicker.getText().toString());
            Date endDate = stringToDate(mEndDatePicker.getText().toString());
            int studentsNumber = Integer.parseInt(mStudentsPicker.getText().toString());

            Log.i(TAG, "Start :" + startDate.getTime() + "   End: " + endDate.getTime());

            Calendar localCalendar = Calendar.getInstance();

            // Faz a validação de todos os casos e mostra um alerta na tela se houver um erro
            if (description.length() < 6 || description.length() > 140) {
                ToastMaker.showToast(this, getString(R.string.description_validation));
                return false;
            } else if (startDate.compareTo(endDate) >= 0) {
                ToastMaker.showToast(this, getString(R.string.date_compare_validation));
                return false;
            } else if(getDayPlusOne(startDate).compareTo(localCalendar.getTime()) < 0) {
                Log.i(TAG, localCalendar.getTime().toString());
                ToastMaker.showToast(this, getString(R.string.date_current_validation));
                return false;
            } else if(studentsNumber <= 0) {
                ToastMaker.showToast(this, getString(R.string.students_validation));
                return false;
            } else {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            ToastMaker.showToast(this, getString(R.string.parsing_error));
            return false;
        }
    }

    /**
     * Cria um novo objeto da model Course. Já assume que os campos estão validados
     * @return      Objeto da classe Course com os novos dados
     */
    private Course buildCourse() {
        try {
            String description = mEditTextDescription.getText().toString();
            Date startDate = stringToDate(mStartDatePicker.getText().toString());
            Date endDate = stringToDate(mEndDatePicker.getText().toString());
            Log.i(TAG, startDate.toString());
            int studentsNumber = Integer.parseInt(mStudentsPicker.getText().toString());
            int index = mCategoryPicker.getValue() - 1;
            Category category = mCategories.get(index);

            return new Course(description, startDate, endDate, category, studentsNumber);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retorna uma data a partir de um texto no formato DD/MM/YYYY
     * @param stringDate        Data em string a ser convertida
     * @return                  Objeto Date criado a partir da string
     */
    private Date stringToDate(String stringDate) throws ParseException {
        // Define a formatação da data
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", new Locale("pt","BR"));
        try {
            Date date = format.parse(stringDate); // Cria o objeto Date
            return date;
        } catch (ParseException e) {
            // Lança exceção se houver erro no parser
            throw e;
        }
    }

    /**
     * Retorna uma data formatada em String a partir de um objeto Date
     * @param date      Objeto Date a ser transformado em String
     * @return          String com a data formatada
     */
    private String dateToString(Date date) {
        // Define a formatação da data
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("pt","BR"));
        try {
            return dateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void setInitialConfig(CourseDetailsActivity.CourseSubmittedListener listener, ArrayList<Category> categories) {
        mCourseSubmittedListener = listener;
        mCategories = categories;
    }

    /**
     * Interface para definir um listener para chamar da MainActivity
     */
    public interface CourseSubmittedListener {
        /**
         * Método de callback para submeter o novo curso
         * @param newCourse     novo curso a ser enviado
         */
        void onSubmit(Course newCourse);
    }
}
