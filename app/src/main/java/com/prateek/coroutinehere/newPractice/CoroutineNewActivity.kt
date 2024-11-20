package com.prateek.coroutinehere.newPractice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels

class CoroutineNewActivity : ComponentActivity() {


    private val viewModel by viewModels<CoroutineNewViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        viewModel.launch()


    }
}