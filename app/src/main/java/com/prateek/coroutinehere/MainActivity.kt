package com.prateek.coroutinehere

import android.content.Intent
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.prateek.coroutinehere.example2.Example2Activity
import com.prateek.coroutinehere.flow.FlowActivity2
import com.prateek.coroutinehere.newPractice.CoroutineNewActivity
import com.prateek.coroutinehere.ui.theme.CoroutineHereTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoroutineHereTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        Button(onClick = {
                            startActivity(Intent(this@MainActivity, Example1Activity::class.java))
                        }) {
                            Text("Example1")
                        }
                        Spacer(Modifier.height(20.dp))

                        Button(onClick = {
                            startActivity(Intent(this@MainActivity, Example2Activity::class.java))
                        }) {
                            Text("Example2")
                        }

                        Spacer(Modifier.height(20.dp))

                        Button(onClick = {
                            startActivity(Intent(this@MainActivity, FlowActivity2::class.java))
                        }) {
                            Text("Flow")
                        }

                        Spacer(Modifier.height(20.dp))
                        Button(onClick = {
                            startActivity(
                                Intent(
                                    this@MainActivity, CoroutineNewActivity::class.java
                                )
                            )
                        }) {
                            Text("Example3")
                        }
                        Spacer(Modifier.height(20.dp))

                    }
                }
            }
        }
    }
}

