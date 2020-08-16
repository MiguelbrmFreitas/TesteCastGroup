package com.miguelbrmfreitas.testecastgroup.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.miguelbrmfreitas.testecastgroup.R;
import com.miguelbrmfreitas.testecastgroup.models.Course;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.CoursesViewHolder>
{
    private ArrayList<Course> mCourses;
    private Context mContext;

    public CoursesAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public CoursesViewHolder onCreateViewHolder(ViewGroup parent,
                                                  int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_card_view, parent, false);

        return new CoursesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoursesViewHolder holder, int position) {
        final CoursesViewHolder coursesViewHolder = holder;

        // Cria os conteúdos do TextView
        String description = mCourses.get(position).getDescription();
        String dateInterval = getDateIntervalString(mCourses.get(position).getStartDate(), mCourses.get(position).getEndDate());
        int studentsNumber = mCourses.get(position).getStudentsPerClass();
        String category = mCourses.get(position).getCategory().getDescription();

        // Seta os textviews
        coursesViewHolder.mDescription.setText(description);
        coursesViewHolder.mDateInterval.setText(dateInterval);
        coursesViewHolder.mCategory.setText(category);
        if (studentsNumber > 0) {
            coursesViewHolder.mStudentsNumber.setText("" + studentsNumber);
        } else {
            coursesViewHolder.mStudentsNumber.setText("N/A");
        }

        // Listener para o edit button
        coursesViewHolder.mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Ir para Activity de edição
            }
        });

        // Listener para o delete button
        coursesViewHolder.mDeleteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: chamar rota /delete da API
            }
        });
    }

    /**
     * Classe para definir o view holder com o layout de cada item do adapter
     */
    static class CoursesViewHolder extends RecyclerView.ViewHolder {

        ImageButton mDeleteImageButton;
        MaterialButton mEditButton;
        TextView mDescription;
        TextView mDateInterval;
        TextView mStudentsNumber;
        TextView mCategory;

        /**
         * Faz o binding dos elementos
         * @param itemView      Item do RecyclerView
         */
        CoursesViewHolder(View itemView) {
            super(itemView);
            mDeleteImageButton = itemView.findViewById(R.id.layout_card_view_ib_delete);
            mEditButton = itemView.findViewById(R.id.layout_card_view_edit_button);
            mDescription = itemView.findViewById(R.id.layout_card_view_tv_description_content);
            mDateInterval = itemView.findViewById(R.id.layout_card_view_tv_date_interval);
            mStudentsNumber = itemView.findViewById(R.id.layout_card_view_tv_students_number_content);
            mCategory = itemView.findViewById(R.id.layout_card_view_tv_category_content);
        }
    }


    @Override
    public int getItemCount() {
        if (mCourses != null) {
            return mCourses.size();
        } else {
            return 0;
        }
    }

    /**
     * Configura os dados do adapter
     * @param courses       Array de courses
     */
    public void setData( ArrayList<Course> courses) {
        mCourses = courses;
        notifyDataSetChanged();
    }

    /**
     * Retorna o texto pronto para colocar no cardview com o período do curso
     * @param startDate     Data de início
     * @param endDate       Data de final
     * @return
     */
    private String getDateIntervalString(Date startDate, Date endDate) {
        String formattedStart = new SimpleDateFormat("dd/MM/YYYY", Locale.getDefault()).format(startDate);
        String formattedEnd = new SimpleDateFormat("dd/MM/YYYY", Locale.getDefault()).format(endDate);

        return "De " + formattedStart + " a " + formattedEnd;
    }
}
