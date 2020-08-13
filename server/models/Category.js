const mongoose = require('mongoose');

const CategorySchema = mongoose.Schema({

    // user: {
    //     type: mongoose.Schema.Types.ObjectId,
    //     ref: 'users'
    // },
    code: {
        type: String,
        required: true
    },
    description: {
        type: String,
        required: true
    }
});

module.exports = mongoose.model('category', CategorySchema);