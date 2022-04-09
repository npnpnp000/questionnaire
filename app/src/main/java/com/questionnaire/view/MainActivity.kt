package com.questionnaire.view

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
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
import com.questionnaire.view.ui.theme.QuestionnaireTheme
import com.questionnaire.R



class MainActivity : ComponentActivity() {
    var isValidate = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        Surface(color = MaterialTheme.colors.background) {
            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Top()   // top tab
                Questions()
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
    fun Questions() {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .background(color = Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround

        ) {

            MultipleChoiceQuestion("number", listOf("1", "2", "3"), true)

        }
    }

    @Composable
    fun MultipleChoiceQuestion(title: String, answers: List<String>, required: Boolean) {
        val selectedAnswer = remember { mutableStateOf("") }
        if (selectedAnswer.value == ""){
            isValidate = false
        }else{
            isValidate = true
        }
        Log.e("isValidate", isValidate.toString()+ " answer: ${selectedAnswer.value}")
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

            ) {
                Row(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = title,
                        color = Color.Black,
                        style = MaterialTheme.typography.h5
                    )
                    if (required) {
                        Text(
                            text = " *",
                            color = Color.Red,
                            style = MaterialTheme.typography.h6
                        )
                    }
                }

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