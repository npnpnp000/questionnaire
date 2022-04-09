package com.questionnaire.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.questionnaire.model.Repository


@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val repository: Repository): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when(modelClass) {
//            MainViewModel::class.java -> { MainViewModel(repository) as T}
//            SplashViewModel::class.java -> { SplashViewModel(repository) as T}
            else -> {throw Exception("Not Define the viewModel Class")}
        }


}