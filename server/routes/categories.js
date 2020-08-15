const express = require('express');
const router = express.Router();
const {check, validationResult} = require('express-validator');

const Category = require('../models/Category');

// @route   GET    api/categories
// @desc    Get de todas as categorias
// @access  Public
router.get('/', async (req, res) => {
    try {
        // Pega todas as categorias do banco de dados
        const categories = await Category.find().sort({start_date: -1});

        // Retorna as categorias encontradas
        res.json(categories);
    } catch (err) {
        console.error(err.message);
        res.status(500).send('Erro no Servidor');
    }
});

// @route   POST    api/categories
// @desc    Register a category
// @access  Public
router.post('/', async (req, res) => {
    const errors = validationResult(req);
    if (!errors.isEmpty()) {
        console.log(errors)
        return res.status(400).json({ errors: errors.array() })
    }

    const { code, description } = req.body;

    try {
        // Busca a categoria por código
        let category = await Category.findOne({ code: code});

        // Se já existir, retorna um erro e não inclui no banco
        if (category) {
            return res.status(400).json({ msg: 'Categoria já existe' });
        }

        // Cria a categoria com código e descrição
        category = new Category({
            code,
            description
        });

        // Salva no banco e espera a operação acabar
        await category.save();

        // Payload de resposta
        const payload = {
            type: 'success'
        }

        // Envia a resposta para o servidor
        res.json(payload)

    } catch (err) {
        console.error(err.message);
        res.status(500).send('Erro no Servidor');
    }
});

module.exports = router;