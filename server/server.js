const express = require('express');
const connectDB = require('./config/db');
const path = require('path');

const app = express();

// Conecta com o Mongo
connectDB();

// Inicializa o Middleware
app.use(express.json({ extended: false }));

// Routes
app.use('/api/categories', require('./routes/categories'));
app.use('/api/courses', require('./routes/courses'));
//app.use('/api/auth', require('./routes/auth'));

// Assets estáticos em produção
// if(process.env.NODE_ENV === 'production') {
//     app.use(express.static('client/build'));

//     app.get('*', (req, res) => 
//         res.sendFile(path.resolve(__dirname, 'client', 'build', 'index.html'))
//     );
// }

const PORT = process.env.PORT || 5001;

app.listen(PORT, () => console.log(`Servidor iniciou na porta ${PORT}`));