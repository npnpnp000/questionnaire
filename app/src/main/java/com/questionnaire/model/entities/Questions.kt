package com.questionnaire.model.entities

import com.google.gson.annotations.Expose


data class Question(
    @Expose
    val answers: List<String>,
    @Expose
    val open_question: String,
    @Expose
    val question: String,
    @Expose
    val required: Boolean,
    @Expose
    val type: String
)