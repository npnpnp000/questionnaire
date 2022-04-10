package com.onlinecalculatorf.model.response

import com.questionnaire.model.entities.Answer
import com.questionnaire.model.entities.Question
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST


class RequestWebService {
    private lateinit var api: QuestionsApi

    init {  // retrofit builder
        val retrofit = Retrofit.Builder()
            .baseUrl("http://localhost:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()


        api = retrofit.create(QuestionsApi::class.java)


    }
    @Throws(Exception::class)
    suspend fun postAnswers(code: Int ,user_answers: List<String>) :Answer{
        return api.postAnswers(code,user_answers)  // send to retrofit post request
    }
    @Throws(Exception::class)
    suspend fun getQuestions(): List<Question> {
        return api.getQuestions() // send to retrofit get request
    }

    interface QuestionsApi {
        // retrofit get request
        @Throws(Exception::class)
        @GET("/questions")
        suspend fun getQuestions(): List<Question>

        // retrofit post request
        @Throws(Exception::class)
        @FormUrlEncoded
        @POST("/answers")
       suspend fun postAnswers(
            @Field("code") code: Int,
            @Field("user_answers") user_answers: List<String>,
        ) :Answer
    }
}
   
    

