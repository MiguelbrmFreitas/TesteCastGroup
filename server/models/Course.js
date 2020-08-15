const mongoose = require('mongoose');

const CourseSchema = mongoose.Schema({
  description: {
    type: String,
    required: true
  },
  start_date: {
    type: Number, // Number para usar o padrão UNIX Timestamp
    default: 1597442400,
    required: true
  },
  end_date: {
    type: Number,
    default: 1597442400,
    required: true
  },
  students_per_class: {
    type: Number,
    required: false
  },
  category: {
    type: mongoose.Schema.Types.ObjectId,
    ref: 'category'
  },
});

module.exports = mongoose.model('course', CourseSchema);