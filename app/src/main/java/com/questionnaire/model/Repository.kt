package com.questionnaire.model

import com.onlinecalculatorf.model.response.RequestWebService
import com.questionnaire.model.entities.Answer
import com.questionnaire.model.entities.Question
import retrofit2.Call
import retrofit2.Callback


class Repository() {

    private var webService: RequestWebService = RequestWebService()

    @Throws(Exception::class)
    suspend fun getQuestions(): List<Question> {
        return webService.getQuestions() // get data to  from web service
    }
    @Throws(Exception::class)
    suspend fun postAnswers(code: Int ,user_answers: List<String>): Answer {
        return webService.postAnswers(code,user_answers) // send post request to web service
    }

}