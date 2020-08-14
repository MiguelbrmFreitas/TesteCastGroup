const express = require('express');
const router = express.Router();
const {check, validationResult} = require('express-validator');

const Category = require('../models/Category');
const Course = require('../models/Course');

// @route   GET    api/courses
// @desc    Get de cursos podendo filtrar por categoria
// @access  Public
router.get('/', async (req, res) => {
    try {
        // Pega o parâmetro category
        const category = req.query.category;
        
        // Cria a query se o parâmetro existir
        let query;
        if (category) {
            query = category;
        } else {
            query = {}
        }

        // Procura no banco de dados e faz o populate no campo de categoria
        const courses = await Course.find(query).populate('category').sort({start_date: -1});

        // Retorna os cursos encontrados
        res.json(courses);
    } catch (err) {
        console.error(err.message);
        res.status(500).send('Erro no Servidor');
    }
});

// @route   POST    api/courses
// @desc    Adicionar novo curso
// @access  Public
router.post('/', [
    [
        check('description', 'É necessário colocar uma descrição').not().isEmpty(),
        check('start_date', 'É necessário colocar uma data de início').not().isEmpty(),
        check('end_date', 'É necessário colocar uma data de final').not().isEmpty(),
        check('category', 'É necessário colocar uma categoria').not().isEmpty(),
    ]
], 
async (req, res) => {
    const errors = validationResult(req);
    if (!errors.isEmpty()) {
        return res.status(400).json({ errors: errors.array() })
    }

    const { description, start_date, end_date, students_per_class, category } = req.body;

    console.log(category)

    // Valida se a data de início é menor que a data de final
    if (start_date >= end_date) {
        // Cria um erro
        const dateErrors = [
            {
                "msg": "A data de início deve ser menor que a data de final.",
                "param": "start_date",
                "location": "body"
            }
        ]

        // Retorna o erro com status 400
        return res.status(400).json({ errors: dateErrors });
    }

    try {
        // Cria a categoria 
        // let populatedCategory = await Category.findById(category).populate('category').exec((err, c) => {
        //     if (err) console.error(err)
        // });

        //console.log(populatedCategory)

        // Cria o novo curso
        const newCourse = new Course({
            description,
            start_date,
            end_date,
            students_per_class,
            category: category,
        })

        const course = await newCourse.save();

        res.json(course);
    } catch (err) {
        console.error(err.message);
        res.status(500).send('Erro do Servidor')
    }
});

// @route   PUT    api/courses/:id
// @desc    Update course
// @access  Public
router.put('/:id', async (req, res) => {
    const { description, start_date, end_date, students_per_class } = req.body;

    //TODO: Validação das regras de negócio

    // Cria o objeto do curso
    const courseFields = {};
    if (description) courseFields.description = description;
    if (start_date) courseFields.start_date = start_date;
    if (end_date) courseFields.end_date = end_date;
    if (students_per_class) courseFields.students_per_class = students_per_class;

    try {
        let course = await Course.findById(req.params.id);

        if (!course) {
            res.status(404).json({msg: 'Curso não encontrado'});
        }

        // Garantir que um curso pertença a uma categoria
        if (course.category.toString() !== req.category.id) {
            return res.status(401).json({ msg: 'Sem Autorização'} );
        }

        course = await Course.findByIdAndUpdate(req.params.id, 
            {
                $set: courseFields
            },
            {
                new: true
            });

        res.json(course);
    } catch(err) {
        console.error(err.message);
        res.status(500).send('Erro do Servidor')
    }
});

// @route   DELETE    api/courses/:id
// @desc    Delete course
// @access  Public
router.delete('/:id', async (req, res) => {
    try {
        let course = await Course.findById(req.params.id);

        if (!course) {
            res.status(404).json({msg: 'Curso não encontrado'});
        }

        // Garantir que um curso pertença a uma categoria
        if (course.category.toString() !== req.category.id) {
            return res.status(401).json({ msg: 'Sem Autorização'} );
        }

        await Course.findByIdAndRemove(req.params.id);

        console.log(course)

        res.json({msg: `Course ${course.description} deleted`});
    } catch(err) {
        console.error(err.message);
        res.status(500).send('Erro do Servidor')
    }
});

module.exports = router;