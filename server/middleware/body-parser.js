const jwt = require('jsonwebtoken');
const config = require('config');

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