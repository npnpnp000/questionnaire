package com.questionnaire.application

import android.app.Application
import com.questionnaire.model.Repository

class MainApplication : Application() {
    val repository by lazy { Repository() }
}