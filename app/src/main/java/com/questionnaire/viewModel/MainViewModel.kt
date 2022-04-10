package com.questionnaire.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.questionnaire.model.Repository
import com.questionnaire.model.entities.Answer
import com.questionnaire.model.entities.Question
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URL

class MainViewModel(private val repository: Repository) : ViewModel() {

    val questionsLiveData = MutableLiveData<List<Question>>(listOf())
    val runValidateLiveData = MutableLiveData(false)
    var postsSuccessesLiveData = MutableLiveData<Boolean>()
    val answerList = arrayListOf<String>()

    init {
        viewModelScope.launch {
            try {
                questionsLiveData.value = repository.getQuestions() // get data to live data from repository
            }catch (e: Exception){
                Log.e("can't load data" ,e.stackTraceToString())
            }

        }
    }

     fun postAnswers(code: Int ) {

         viewModelScope.launch {
             try {
                 repository.postAnswers(code, answerList) // send post request to repository
                 postsSuccessesLiveData.value = true // triggered the dialog of success
             }catch (e :Exception){
                 postsSuccessesLiveData.value = false// triggered the dialog of fail
                 Log.e("postAnswersException" ,e.stackTraceToString())
             }
         }
    }
}