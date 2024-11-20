package com.prateek.coroutinehere.newPractice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CoroutineNewViewModel : ViewModel() {

    fun launch() {
        viewModelScope.launch {
            abc()
        }
    }


    private suspend fun abc() {
        println("Before Line")
        suspendCoroutine<Unit> { continuation ->

            //This Block is called just before suspension
            println("Again Before")

            //If we don't call below func, it will never resume and will remain suspended
            continuation.resume(Unit)
        }
        println("After Line")
    }

    private val executor = Executors.newSingleThreadScheduledExecutor {
        Thread(it, "scheduler").apply { isDaemon = true }
    }

    suspend fun main() {
        println("Before")
        suspendCoroutine<Unit> { continuation ->
            executor.schedule({
                continuation.resume(Unit)
            }, 1000, TimeUnit.MILLISECONDS)
        }
        println("After")
    }

    suspend fun main1(): Unit = coroutineScope {
        val job = Job()
        val job1 = launch(job) { // the new job replaces one from parent
            delay(1000)
            println("Text 1")
        }
        launch(job) { // the new job replaces one from parent
            delay(2000)
            println("Text 2")
        }
        job.join() // Here we will await forever
        println("Will not be printed")
    }

    suspend fun main2(): Unit = coroutineScope {
        val channel = Channel<Int>()
        launch {
            repeat(5) { index ->
                println("Producing next one")
                delay(1000)
                channel.send(index * 2)
            }
            channel.close()
        }
        launch {
            channel.consumeEach {
                println(it)
            }
        }
    }

    // A channel of number from 1 to 3
    fun CoroutineScope.doVeryHeavyTask(): ReceiveChannel<Int> = produce(capacity = 1) {
        repeat(3) { num ->
            send(num + 1)
        }
    }

    fun CoroutineScope.postProcessResult(numbers: ReceiveChannel<Int>) =
        produce(capacity = Channel.UNLIMITED) {
            for (num in numbers) {
                send(num * num)
            }
        }

    suspend fun main5() = coroutineScope {
        val numbers = doVeryHeavyTask()
        val squared = postProcessResult(numbers)
        for (num in squared) {
            println(num)
        }
    }

    fun CoroutineScope.produceNumbers() = produce {
        repeat(10) {
            delay(100)
            send(it)
        }
    }

    suspend fun main6(): Unit = coroutineScope {
        val channel = produceNumbers()
        repeat(3) { id ->
            delay(10)
            launch {
                for (msg in channel) {
                    println("#$id received $msg")
                }
            }
        }
    }

}