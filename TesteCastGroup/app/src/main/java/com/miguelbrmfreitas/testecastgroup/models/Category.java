package com.miguelbrmfreitas.testecastgroup.models;

/**
 * Classe que define a model de uma categoria
 */
public class Category
{
    private String mCode;
    private String mDescription;

    public Category(String code, String description) {
        mDescription = description;
        mCode = code;
    }

    /**
     * Getter para a descrição
     * @return      Retorna uma String com a descrição
     */
    public String getDescription() {
        return mDescription;
    }

    /**
     * Getter para o código
     * @return      Retorna uma String com o código
     */
    public String getCode() {
        return mCode;
    }
}
