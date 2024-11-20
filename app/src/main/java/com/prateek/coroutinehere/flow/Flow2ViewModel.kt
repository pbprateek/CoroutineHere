package com.prateek.coroutinehere.flow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch

class Flow2ViewModel : ViewModel() {

    val liveData = MutableStateFlow(0)

    init {
        viewModelScope.launch {
            //combineFlowZip()
            //combineFlowCombine()
//            normalFlow().collect {
//                delay(2000)
//                println("PRATEEK outside Flow $it")
//            }

            val time = System.currentTimeMillis()
            normalFlow().collect {
                delay(500)
                println("PRATEEK outside Flow $it")
            }
            println("PRATEEK Total Time = ${System.currentTimeMillis() - time}")
        }
    }

    //Channel Flow
    private fun channelFlow() = channelFlow<Int> {
        repeat(5) {
            delay(200)
            println("PRATEEK inside Flow $it")
            send(it)
        }
    }

    //Normal Flow
    private fun normalFlow() = flow<Int> {
        repeat(5) {
            delay(200)
            println("PRATEEK inside Flow $it")
            emit(it)
        }
    }.buffer(10, onBufferOverflow = BufferOverflow.DROP_OLDEST)


    //Combine

    private suspend fun combineFlowZip() {
        val startTime = System.currentTimeMillis() // remember the start time
        val nums = (1..3).asFlow().onEach { delay(300) } // numbers 1..3 every 300 ms
        val strs = flowOf("one", "two", "three").onEach { delay(400) } // strings every 400 ms
        nums.zip(strs) { a, b -> "$a -> $b" } // compose a single string with "zip"
            .collect { value -> // collect and print
                println("$value at ${System.currentTimeMillis() - startTime} ms from start")
            }
    }

    private suspend fun combineFlowCombine() {

        //With Combine it prints on every single emission from either side so quite unexpected result
        val nums = (1..3).asFlow().onEach { delay(300) } // numbers 1..3 every 300 ms
        val strs = flowOf("one", "two", "three").onEach { delay(400) } // strings every 400 ms
        val startTime = System.currentTimeMillis() // remember the start time
        nums.combine(strs) { a, b -> "$a -> $b" } // compose a single string with "zip"
            .collect { value -> // collect and print
                println("$value at ${System.currentTimeMillis() - startTime} ms from start")
            }
    }

    fun updateText(int: Int) {
        liveData.value = int
    }

}