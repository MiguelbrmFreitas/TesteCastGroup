const jwt = require('jsonwebtoken');
const config = require('config');

// Criado para o caso da aplicação crescer, mas não foi necessário utilizar
module.exports = function(req, res, next) {
    // Get token from header
    token = req.header('x-auth-token');

    if (!token) {
        return res.status(401).json({    msg: 'Sem token de indentificação.'   })
    }

    try {
        const decoded = jwt.verify(token, config.get('jwtSecret'));

        req.category = decoded.category;
        next();
    } catch(err) {
        return res.status(401).json({
            msg: 'Token inválido'
        })
    }
}