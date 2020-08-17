package com.miguelbrmfreitas.testecastgroup.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;
import com.miguelbrmfreitas.testecastgroup.R;
import com.miguelbrmfreitas.testecastgroup.components.ToastMaker;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * Classe que define o dialog com a ação de deletar um curso
 */
public class DeleteDialogFragment extends DialogFragment
{
    private MaterialButton mConfirmationButton;
    private MaterialButton mCancelButton;

    private String mCourseId;

    /**
     * Interface que define o listener para receber os eventos
     */
    public interface DeleteDialogListener {
        /**
         * Método de retorno do delete
         * @param courseId      ID do curso a ser (ou não) deletado
         * @param delete        true se for pra deletar, false caso contrário
         */
        void deleteCourse(String courseId, boolean delete);
    }

    /**
     * Construtor vazio
     */
    public DeleteDialogFragment() {

    }

    /**
     * Cria nova instância da classe
     * @param courseId        ID do curso a ser deletado
     * @return      Retorna nova instância
     */
    public static DeleteDialogFragment newInstance(String courseId) {
        DeleteDialogFragment frag = new DeleteDialogFragment();
        Bundle args = new Bundle();
        args.putString("course_id", courseId);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_delete_dialog, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Faz o binding das views
        mConfirmationButton = view.findViewById(R.id.fragment_delete_dialog_confirmation_button);
        mCancelButton = view.findViewById(R.id.fragment_delete_dialog_cancel_button);
        // Pega os argumentos do Bundle e seta o título
        mCourseId = getArguments().getString("course_id", "");
        getDialog().setTitle(getString( R.string.delete_warning));
        // Mostra a janela
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        //Callbacks
        mConfirmationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteDialogListener deleteDialogListener = (DeleteDialogListener) getActivity(); // Cria o objeto do listener
                deleteDialogListener.deleteCourse(mCourseId, true); // Manda deletar
                ToastMaker.showToast(getContext().getApplicationContext(), getString(R.string.delete_success));
                dismiss();
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteDialogListener deleteDialogListener = (DeleteDialogListener) getActivity(); // Cria o objeto do listener
                deleteDialogListener.deleteCourse(mCourseId, false); // Não deletar
                dismiss();
            }
        });
    }
}
