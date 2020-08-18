package com.miguelbrmfreitas.testecastgroup.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.NumberPicker;

import com.google.android.material.button.MaterialButton;
import com.miguelbrmfreitas.testecastgroup.R;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class SearchDialogFragment extends DialogFragment
{
    private MaterialButton mConfirmationButton;
    private MaterialButton mCancelButton;
    private NumberPicker mCategoryPicker;

    private String[] mCategories;

    /**
     * Interface que define o listener para receber os eventos
     */
    public interface SearchDialogListener {
        /**
         * Método de retorno do delete
         * @param categoryId     ID da categoria usada para filtrar
         * @param search       true se for pra filtrar, false caso contrário
         */
        void searchCourses(String categoryId, boolean search);
    }

    /**
     * Construtor vazio
     */
    public SearchDialogFragment() {

    }

    /**
     * Cria nova instância da classe
     * @param categories       Array com string de categories
     * @return      Retorna nova instância
     */
    public static SearchDialogFragment newInstance(String[] categories) {
        SearchDialogFragment frag = new SearchDialogFragment();
        Bundle args = new Bundle();
        args.putStringArray("categories", categories);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_dialog, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Faz o binding das views
        mConfirmationButton = view.findViewById(R.id.fragment_search_dialog_confirmation_button);
        mCancelButton = view.findViewById(R.id.fragment_search_dialog_cancel_button);
        mCategoryPicker = view.findViewById(R.id.fragment_search_dialog_category_picker);
        // Pega os argumentos do Bundle
        mCategories = getArguments().getStringArray("categories");

        // Configura as categorias
        mCategoryPicker.setMinValue(1);
        mCategoryPicker.setMaxValue(5);
        mCategoryPicker.setDisplayedValues(mCategories);

        getDialog().setTitle(getString( R.string.search_dialog));
        // Mostra a janela
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        //Callbacks
        mConfirmationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = "" + mCategoryPicker.getValue();
                backToActivity(code, true);
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToActivity("", false);
            }
        });
    }

    private void backToActivity(String code, boolean search) {
        SearchDialogFragment.SearchDialogListener searchDialogListener = (SearchDialogFragment.SearchDialogListener) getActivity(); // Cria o objeto do listener
        searchDialogListener.searchCourses(code, search); // Pesquisa pela categoria
        dismiss();
    }
}
