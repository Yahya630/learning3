package com.example.learning3

data class Question(
    val questionText: String? = null,
    val questionImage: Int? = null,
    val optionImages: List<Int>? = null,
    val optionWords: List<String>? = null,
    val optionLetters: List<String>? = null,
    val correctAnswer: Int? = null,
    val correctAnswerIndex: Int? = null,
    val soundResId: Int? = null
)