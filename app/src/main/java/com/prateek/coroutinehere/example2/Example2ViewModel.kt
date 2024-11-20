package com.prateek.coroutinehere.example2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class Example2ViewModel : ViewModel() {


    val liveData = MutableStateFlow(0)

    val timerFlow = flow<Int> {
        var count = 10
        emit(count)
        for (i in 0..count) {
            delay(1000L)
            count--
            emit(count)
            yield()
        }
    }

    init {
        viewModelScope.launch {
            collectTimer1()
            //tryOperators()
            checkSizeLimitingException()
            //checkTerminalOperator()
            checkBuffering()
        }
    }

    private suspend fun tryOperators() {
        (1..3).asFlow().map {
            delay(1000L)
            it * 2
        }.collect {
            printOnMain(it)
        }
    }

    private suspend fun collectTimer() = withContext(Dispatchers.IO) {
        timerFlow.collectLatest {
            //The collection can happen on any thread, it is not necessary they will happen on the same thread
            //Run it and u will see
            println("The collected timer is $it , CurrentThread:-${Thread.currentThread().name}")
        }
    }


    val sizeLimitingExceptionThrow: Flow<Int> = flow {
        try {
            emit(1)
            emit(2)
            println("This line will not execute")
            emit(3)
        } finally {
            println("Finally in numbers")
        }
    }

    private suspend fun checkSizeLimitingException() = withContext(Dispatchers.IO) {

        //Size limiting funtion will always cancel the flow by throwing exception so u can handle it easily by using catch/finally
        //In below it will print 1,2 and also "Finally in numbers"

        sizeLimitingExceptionThrow
            .take(2) // take only the first two
            .collect { value -> println(value) }
    }


    private suspend fun collectTimer1() = withContext(Dispatchers.IO) {
        withTimeoutOrNull(2500L) {
            timerFlow.collect {
                println("The collected timer is $it , CurrentThread:-${Thread.currentThread().name}")
            }
        }
    }

    private suspend fun checkTerminalOperator() = withContext(Dispatchers.IO) {
        //Terminal operators on flows are suspending functions that start a collection of the flow.
        val count: Int = (1..5).asFlow().map { it * 5 }.count()
        withContext(Dispatchers.Main.immediate) {
            printOnMain(count)
        }
    }

    private suspend fun checkBuffering() = withContext(Dispatchers.IO) {
        (1..5).asFlow().map {
            delay(1000L)
            println("Buffer value map Thread = ${Thread.currentThread().name}")
            it * 3
        }.buffer().collect {
            delay(1000L)
            println("Collected buffer value $it Thread = ${Thread.currentThread().name}")
        }
    }

    private fun printOnMain(value: Int) {
        liveData.value = value
    }
}