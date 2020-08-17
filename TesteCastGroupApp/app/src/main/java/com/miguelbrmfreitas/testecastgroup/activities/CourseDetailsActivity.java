package com.miguelbrmfreitas.testecastgroup.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.miguelbrmfreitas.testecastgroup.R;
import com.miguelbrmfreitas.testecastgroup.components.CustomButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

    Calendar mCalendar = Calendar.getInstance();

    private final int START_DATE = 1;
    private final int END_DATE = 2;

    private int mDatePickerClicked = START_DATE;

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
        String[] categories = bundle.getStringArray("categories_array");
        boolean isEditing = bundle.getBoolean("is_editing");

        Log.i(TAG, "is editing " + isEditing);

        // Configurando o picker de categorias
        mCategoryPicker.setMinValue(1);
        mCategoryPicker.setMaxValue(4);
        mCategoryPicker.setDisplayedValues(categories);

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

    private void createDateDialog(boolean isStart) {
        mDatePickerClicked = isStart? START_DATE: END_DATE; // Seta o date clicado
        // Cria o dialog para receber o input da data e chama o método para visualizar
        new DatePickerDialog(CourseDetailsActivity.this, mDate, mCalendar
                .get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}
