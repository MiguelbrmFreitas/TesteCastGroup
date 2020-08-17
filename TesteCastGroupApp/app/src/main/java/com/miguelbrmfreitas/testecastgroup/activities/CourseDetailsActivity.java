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

import java.util.Date;

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
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

    }
}
