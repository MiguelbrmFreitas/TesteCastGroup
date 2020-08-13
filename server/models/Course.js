const mongoose = require('mongoose');

const CourseSchema = mongoose.Schema({
  description: {
    type: String,
    required: true
  },
  start_date: {
    type: Date,
    default: Date.now
  },
  end_date: {
    type: Date,
    default: Date.now
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