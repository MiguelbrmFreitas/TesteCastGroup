package com.miguelbrmfreitas.testecastgroup.models;

import java.util.Date;

/**
 * Classe que define a model de um curso
 */
public class Course
{
    private String _id;
    private String mDescription;
    private Date mStartDate;
    private Date mEndDate;
    private Category mCategory;
    private int mStudentsPerClass;

    // Diferentes tipos de construtores, pois o atributo mStudentsPerClass é variável
    /**
     * Constrói um novo objeto Course
     * @param description       Descrição do curso
     * @param startDate         Data de início
     * @param endDate           Data de final
     * @param category          Categoria do curso
     */
    public Course( String description, Date startDate, Date endDate, Category category) {
        this(description, startDate, endDate, category, 0);
    }

    /**
     * Constrói um novo objeto Course
     * @param description       Descrição do curso
     * @param startDate         Data de início
     * @param endDate           Data de final
     * @param category          Categoria do curso
     * @param studentsPerClass  Número de alunos
     */
    public Course(String description, Date startDate, Date endDate, Category category, int studentsPerClass) {
        mDescription = description;
        mStartDate = startDate;
        mEndDate = endDate;
        mCategory = category;
        mStudentsPerClass = studentsPerClass;
    }

    /**
     * Getter para o ID
     * @return      Retorna uma String com o ID
     */
    public String getId() {
        return _id;
    }

    /**
     * Setter para o ID
     */
    public void setId(String mId) {
        this._id = mId;
    }

    /**
     * Getter para a descrição
     * @return      Retorna uma String com a descrição
     */
    public String getDescription() {
        return mDescription;
    }

    /**
     * Setter para a descrição
     * @param mDescription      String com a nova descrição
     */
    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    /**
     * Getter para a data de início
     * @return      Retorna um objeto Date com a data de início
     */
    public Date getStartDate() {
        return mStartDate;
    }

    /**
     * Setter para a data de início
     * @param mStartDate       Objeto Date com a nova data de início
     */
    public void setStartDate(Date mStartDate) {
        this.mStartDate = mStartDate;
    }

    /**
     * Getter para a data de final
     * @return      Retorna um objeto Date com a data de final
     */
    public Date getEndDate() {
        return mEndDate;
    }

    /**
     * Setter para a data de final
     * @param mEndDate      Objeto Date com a nova data de final
     */
    public void setEndDate(Date mEndDate) {
        this.mEndDate = mEndDate;
    }

    /**
     * Getter para a categoria
     * @return      Retorna um objeto Category
     */
    public Category getCategory() {
        return mCategory;
    }

    /**
     * Setter para a categoria
     * @param mCategory     Objeto Category com a nova categoria
     */
    public void setCategory(Category mCategory) {
        this.mCategory = mCategory;
    }

    /**
     * Getter para o atributo de número de alunos
     * @return      Retorna o número de alunos por curso
     */
    public int getStudentsPerClass() {
        return mStudentsPerClass;
    }

    /**
     * Setter para o atributo de número de alunos por curso
     * @param mStudentsPerClass     Novo número de alunos
     */
    public void setStudentsPerClass(int mStudentsPerClass) {
        this.mStudentsPerClass = mStudentsPerClass;
    }
}
