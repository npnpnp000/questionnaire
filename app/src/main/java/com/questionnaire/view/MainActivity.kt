package com.questionnaire.view

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.questionnaire.view.ui.theme.QuestionnaireTheme
import com.questionnaire.R
import com.questionnaire.application.MainApplication
import com.questionnaire.viewModel.MainViewModel
import com.questionnaire.viewModel.ViewModelFactory
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : ComponentActivity() {
    private lateinit var validateList : ArrayList<Boolean>
    private lateinit var appFactory: ViewModelFactory
    private lateinit var mainViewModel: MainViewModel
    private lateinit var runValidate : State<Boolean?>
    private lateinit var isSuccesses: State<Boolean?>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainApplication: MainApplication = application as MainApplication
        appFactory = ViewModelFactory(mainApplication.repository)

        setContent {
            QuestionnaireTheme {

                Scaffold(topBar = { AppBar() }) {

                    MainPage()
                }
            }
        }
    }


    @Composable
    fun AppBar() {
        TopAppBar(
            backgroundColor = MaterialTheme.colors.background,
            navigationIcon = {
                Image(
                    painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = null,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            },
            title = { Text(text = stringResource(R.string.app_name)) }
        )
    }

//   -------------------------- Main Page ------------------------------


    @Composable
    fun MainPage() {

        mainViewModel = viewModel(factory = appFactory)

        Surface(color = MaterialTheme.colors.background) {
            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Questions()
            }
        }
    }

    @Composable
    fun Questions() {
       val questionsList = mainViewModel.questionsLiveData.observeAsState()
        runValidate = mainViewModel.runValidateLiveData.observeAsState()

        validateList = arrayListOf()
        Column(
            Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .background(color = Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround

        ) {

            LazyColumn(){
                item{
                    Top()   // top tab

                    questionsList.value?.forEach { question ->
                        when(question.type){
                            "MultipleChoice" ->{
                                val index = questionsList.value?.indexOf(question) ?:0
                                mainViewModel.answerList.add(index , "")
                                validateList.add(index, false)
                                MultipleChoiceQuestion(questionNumber = index, title = question.question, answers = question.answers ,required =question.required)
                            }
                            "MultipleChoiceWhitOpenQuestion" ->{
                                val index = questionsList.value?.indexOf(question) ?:0
                                mainViewModel.answerList.add(index , "")
                                validateList.add(index, false)
                                MultipleChoiceWhitOpenQuestion(questionNumber = index, title = question.question,answers= question.answers, openQuestionName = question.open_question, required =question.required)
                            }
                            "OpenQuestion" ->{
                                val index = questionsList.value?.indexOf(question) ?:0
                                mainViewModel.answerList.add(index , "")
                                validateList.add(index, false)
                                OpenQuestion(questionNumber = index, title = question.question, required =question.required)
                            }
                            else -> {Log.e("Error", "There is no such question")}
                        }
                    }

                    SendDataButton()

                }
            }
        }
    }



    @Composable
    fun Top() { // top tab

        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            elevation = 20.dp,
            backgroundColor = MaterialTheme.colors.primaryVariant,
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .background(color = Color.White),
                horizontalAlignment = Alignment.Start,

                ) {


                Text(
                    modifier = Modifier.padding(10.dp),
                    text = stringResource(R.string.h1),
                    color = Color.Black,
                    style = MaterialTheme.typography.h3
                )
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = stringResource(R.string.h2),
                    color = Color.Black,
                    style = MaterialTheme.typography.h6
                )
                Spacer(
                    Modifier
                        .background(Color.LightGray)
                        .fillMaxWidth()
                        .height(1.dp)
                )
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = stringResource(R.string.require),
                    color = Color.Red
                )

            }
        }
    }
    @Composable
    fun OpenQuestion(questionNumber: Int, title: String, required: Boolean) {
        val selectedAnswer =
            remember { mutableStateOf("") }   // saved state of answer and recompose when this value changed
        mainViewModel.answerList[questionNumber] =
            selectedAnswer.value  // set the answer of the list of the answer
        // if required is true and answer is not empty: set the index of the question validate list to true else to false
        if(required == true){
            if(selectedAnswer.value != ""){
                validateList[questionNumber]= true
            }else validateList[questionNumber]= false
        }  else validateList[questionNumber]= true
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            elevation = 20.dp,
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(color = Color.White),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceEvenly
                //  the question title ui
            ) {
                Row(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = title,
                        // if validate check fails and validateList on this index false text color is red else black
                        color = if(runValidate.value == true && !validateList[questionNumber])Color.Red else Color.Black,
                        style = MaterialTheme.typography.h6
                    )
                    if (required) {  // if required is true add "*" to the title
                        Text(
                            text = " *",
                            color = Color.Red,
                            style = MaterialTheme.typography.h6
                        )
                    }
                }
                TextField(
                    modifier = Modifier
                        .padding(start = 16.dp,end = 16.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White),
                    value = selectedAnswer.value ,
                    label = { Text(text = stringResource(R.string.your_answer))},
                    onValueChange = { text ->
                        selectedAnswer.value = text
                    })
                Spacer(modifier = Modifier.size(16.dp))
            }
        }
    }

    @Composable
    fun MultipleChoiceWhitOpenQuestion(
        questionNumber: Int,
        title: String,
        answers: List<String>,
        openQuestionName: String,
        required: Boolean
    ) {
        val selectedAnswer =
            remember { mutableStateOf("") }   // saved state of answer and recompose when this value changed
        val isSelectedOpenQuestion = remember { mutableStateOf(false) }
        mainViewModel.answerList[questionNumber] =
            selectedAnswer.value  // set the answer of the list of the answer
        // if required is true and answer is not empty: set the index of the question validate list to true else to false
        if(required == true){
            if(selectedAnswer.value != ""){
                validateList[questionNumber]= true
            }else validateList[questionNumber]= false
        }  else validateList[questionNumber]= true
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            elevation = 20.dp,
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(color = Color.White),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceEvenly
                //  the question title ui
            ) {
                Row(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = title,
                        // if validate check fails and validateList on this index false text color is red else black
                        color = if(runValidate.value == true && !validateList[questionNumber])Color.Red else Color.Black,
                        style = MaterialTheme.typography.h6
                    )
                    if (required) {  // if required is true add "*" to the title
                        Text(
                            text = " *",
                            color = Color.Red,
                            style = MaterialTheme.typography.h6
                        )
                    }
                }
                // for each answer in the answers list add RadioButton with the answer (ui)
                answers.forEach { answer ->
                    Row(verticalAlignment = CenterVertically) {
                        RadioButton(
                            selected = selectedAnswer.value == answer && !isSelectedOpenQuestion.value,
                            onClick = {
                                selectedAnswer.value = answer
                                isSelectedOpenQuestion.value = false
                            })
                        Spacer(modifier = Modifier.size(16.dp))
                        Text(
                            text = answer,
                            style = MaterialTheme.typography.h6
                        )
                        Spacer(modifier = Modifier.size(16.dp))
                    }
                }

                Row(verticalAlignment = CenterVertically) {
                    RadioButton(selected = isSelectedOpenQuestion.value, onClick = {
                        selectedAnswer.value = ""
                        isSelectedOpenQuestion.value = true
                    })
                    Spacer(modifier = Modifier.size(16.dp))
                    Text(
                        text = openQuestionName,
                        style = MaterialTheme.typography.h6
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                    Column {
                        TextField(
                            modifier = Modifier
                                .padding(end = 16.dp),
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.White),
                            enabled = isSelectedOpenQuestion.value,
                            value = if (isSelectedOpenQuestion.value) selectedAnswer.value else "",
                            onValueChange = { text ->
                                selectedAnswer.value = text
                            })
                    }

                }
                Spacer(modifier = Modifier.size(16.dp))
            }

        }
    }

    @Composable
    fun MultipleChoiceQuestion(
        questionNumber: Int,
        title: String,
        answers: List<String>,
        required: Boolean
    ) {
        val selectedAnswer =
            remember { mutableStateOf("") }   // saved state of answer and recompose when this value changed
        mainViewModel.answerList[questionNumber] =
            selectedAnswer.value  // set the answer of the list of the answer
        // if required is true and answer is not empty: set the index of the question validate list to true else to false
          if(required == true){
            if(selectedAnswer.value != ""){
                validateList[questionNumber]= true
            }else validateList[questionNumber]= false
        }  else validateList[questionNumber]= true

        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            elevation = 20.dp,
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(color = Color.White),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceEvenly
                //  the question title ui
            ) {
                Row(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = title,
                        // if validate check fails and validateList on this index false text color is red else black
                        color = if(runValidate.value == true && !validateList[questionNumber])Color.Red else Color.Black,
                        style = MaterialTheme.typography.h6
                    )
                    if (required) {  // if required is true add "*" to the title
                        Text(
                            text = " *",
                            color = Color.Red,
                            style = MaterialTheme.typography.h6
                        )
                    }
                }
                // for each answer in the answers list add RadioButton with the answer (ui)
                answers.forEach { answer ->
                    Row(verticalAlignment = CenterVertically) {
                        RadioButton(selected = selectedAnswer.value == answer, onClick = {
                            selectedAnswer.value = answer
                        })
                        Spacer(modifier = Modifier.size(16.dp))
                        Text(
                            text = answer,
                            style = MaterialTheme.typography.h6
                        )
                        Spacer(modifier = Modifier.size(16.dp))
                    }

                }

            }

        }
    }

    @Composable
    private fun SendDataButton() { // set submit button (ui)
        Button(
            modifier = Modifier.padding(
                start = 16.dp,
                bottom = 32.dp
            ),
            onClick = { startValidate() }) {
            Text(text = stringResource(R.string.submit))
        }
        StartAlertDialog()
    }
    // function make the validate
    private fun startValidate() {
        if (mainViewModel.runValidateLiveData.value == true){ // reset the validate check
            mainViewModel.runValidateLiveData.value = false
        }
        validateList.forEach { // if item on validateList is false validate fail
            if (!it){
                mainViewModel.runValidateLiveData.value = true // if fail set runValidateLiveData to true to observing
                Toast.makeText(this@MainActivity, getString(R.string.toast_text), Toast.LENGTH_LONG).show()
                return@forEach
            }
        }
        if (mainViewModel.runValidateLiveData.value == false){ // if validate successes
            mainViewModel.postAnswers(System.currentTimeMillis().toInt()) // make post request and add currentTime like id

        }
    }
    @Composable
    fun StartAlertDialog() {
       val  isSuccesses = mainViewModel.postsSuccessesLiveData.observeAsState()
        if(isSuccesses.value == true){
            SubmitSuccessesAlertDialog()
        }else if (isSuccesses.value == false){
            SubmitFailAlertDialog()
        }
    }

    @Composable
    fun SubmitFailAlertDialog() {
        AlertDialog(onDismissRequest = {mainViewModel.postsSuccessesLiveData.value =null },
            title = { Text(text = stringResource(R.string.submit_fail)) },
            text = { Text(text = stringResource(R.string.submit_fail_txt)) },
            buttons = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            mainViewModel.postsSuccessesLiveData.value =null
                        }) {
                        Text(text = stringResource(R.string.ok))
                    }
                }
            })
    }

    @Composable
    fun SubmitSuccessesAlertDialog() {
        AlertDialog(onDismissRequest = { mainViewModel.postsSuccessesLiveData.value =null  },
            title = { Text(text = stringResource(R.string.submit_successes)) },
            text = { Text(text = stringResource(R.string.submit_successes_text)) },
            buttons = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            mainViewModel.postsSuccessesLiveData.value =null
                        }) {
                        Text(text = stringResource(R.string.ok))
                    }
                }
            })
    }

//  ------------------------------- Preview -----------------------------

    @Preview(showBackground = true)
    @Composable
    fun MainPagePreview() {
        QuestionnaireTheme {
            Scaffold(topBar = { AppBar() }) {
                // A surface container using the 'background' color from the theme
                MainPage()
            }
        }
    }
}