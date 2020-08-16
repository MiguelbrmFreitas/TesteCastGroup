const express = require('express');
const router = express.Router();
const {check, validationResult} = require('express-validator');

const Category = require('../models/Category');
const Course = require('../models/Course');


// Erro caso a data de início seja maior ou igual à de final
const startDateGteError = [
    {
        "msg": "A data de início deve ser menor que a data de final.",
        "param": "start_date",
        "location": "body"
    }
]

// Erro caso a data de início seja menor ou igual à data atual
const startDateLteError = [
    {
        "msg": "A data de início deve ser maior que a data atual.",
        "param": "start_date",
        "location": "body"
    }
]

// Erro caso já exista um curso cadastrado no intervalo dado
const intervalErrors = [
    {
        "msg": "Existe(m) curso(s) planejados(s) dentro do período informado.",
        "param": "start_date",
        "location": "body"
    }
]

// Erro caso o número de alunos seja igual ou menor que zero
const studentsNumberError = [
    {
        "msg": "O curso deve ter um aluno ou mais.",
        "param": "students_per_class",
        "location": "body"
    }
]

// @route   GET    api/courses
// @desc    Get de cursos. Sem parâmetro retorna todos, com parâmetro category (mandando o ID da categoria) filtra por categoria
// @access  Public
router.get('/', async (req, res) => {
    try {
        // Pega o parâmetro category
        const category = req.query.category;
        
        // Cria a query se o parâmetro existir
        let query = category? {'category': category} : {};

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

    // Valida se a data de início não é maior ou igual à data de final
    if (start_date >= end_date) {
        return res.status(400).json({ errors: startDateGteError });
    } else if (start_date < ( Math.floor(Date.now() / 1000)) ) { // Verifica se a data de início é maior do que a data atual
        console.log(( Math.floor(Date.now() / 1000)), start_date )
        return res.status(400).json({ errors: startDateLteError });
    }

    // Verifica se o atributo students_per_class foi inserido e se é menor ou igual a zero
    if (students_per_class && students_per_class <= 0) {
        return res.status(400).json({ errors: studentsNumberError });
    }

    // Verifica se já não há um curso incluso neste intervalo
    const course = await Course.findOne({
        $and:[
            {start_date:{$gte: start_date}},
            {end_date:{$lte: end_date}}
        ]
    })
    
    if (course) {
        console.log(course)
        return res.status(400).json({ errors: intervalErrors });
    }

    try {
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
    console.log(req.body)

    //TODO: Validação das regras de negócio

    // Cria o objeto do curso apenas com os campos enviados
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

        // Verifica se datas de início e final existem e se a data de início é maior ou igual à data de final
        if (start_date && end_date && start_date >= end_date) {
            return res.status(400).json({ errors: startDateGteError });
        } else if (start_date < Date.now()) { // Verifica se a data de início é maior do que a data atual
            return res.status(400).json({ errors: startDateLteError });
        }

        // Atualiza no banco de dados
        course = await Course.findByIdAndUpdate({ _id: req.params.id }, courseFields,
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

        // Deleta do banco de dados
        await Course.findByIdAndRemove(req.params.id);

        res.json({msg: `Curso ${course.description} deletado`});
    } catch(err) {
        console.error(err.message);
        res.status(500).send('Erro do Servidor')
    }
});

module.exports = router;