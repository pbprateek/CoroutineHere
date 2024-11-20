package com.prateek.coroutinehere

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.prateek.coroutinehere.ui.theme.CoroutineHereTheme
import kotlinx.coroutines.*

class Example1Activity : ComponentActivity() {

    private val RESULT_1 = "Result #1"


    private val textState = mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            CoroutineHereTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        Text(textState.value)
                        Button(onClick = {
                            CoroutineScope(Dispatchers.IO).launch {
                                fakeApiRequest()
                            }

                        }) {
                            Text("ClickMe")
                        }
                        Spacer(Modifier.height(20.dp))
                    }
                }
            }

        }
    }

    //Scoping is like grouping bunch of jobs together , if one fails and u can cancel rest and all

    //Coroutine is like a job ,doing some work inside the same thread executing all at the same time
    // (Verify this bcz seems wrong)

    //suspen just means that it is a Coroutine function,means it can be called in a Coroutine
    private suspend fun getResultFromApi(): String {
        logThread("getResultFromApi")
        delay(1000)  //This delay will just delay this couroutine,not the entire thread
        return RESULT_1
    }

    private suspend fun fakeApiRequest() {
        val result = getResultFromApi()
        //binding.textView.text = result  This will crash the app
        setTextOnMainThread(result)

        //This is how easy it is to make two network calls one by one
        val result1 = getResultFromApi()
        setTextOnMainThread(result1)
    }

    private suspend fun setTextOnMainThread(input: String) {
        withContext(Dispatchers.Main) {
            logThread("Print on Main")
            setNewText(input)
        }
        //Again from here onward it is in IO thread,Only the above block in is MainThread so keep
        //  switching
    }

    private fun setNewText(input: String) {
        textState.value += "\n$input"
    }


    private fun logThread(methodName: String) {
        println("debug: $methodName : ${Thread.currentThread().name}")
    }
}
