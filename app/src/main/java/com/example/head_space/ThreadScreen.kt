package com.example.head_space

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ThreadScreen(viewModel: Head_SpaceViewModel, text: String, saveJournal: (String) -> Unit) {
    var journal by remember { mutableStateOf(text) }

    var scrollDown by remember { mutableStateOf(true) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column {
            DisplayThread(viewModel.uiState.collectAsState().value.thread, journal,scrollDown = scrollDown,beginScrollDown = {scrollState,coroutineScope ->
                coroutineScope.launch {
                    scrollState.animateScrollTo(1000000000)
                }
                scrollDown = false
            })

            TextInputBar(enterText = {text ->
                val calendar = Calendar.getInstance()
                val simpleDateFormat = SimpleDateFormat("HH:mm")
                val dateTime = simpleDateFormat.format(calendar.time).toString()
                var entry =" \n ["+dateTime+"] "+viewModel.uiState.value.user+": "+ text+"\n"
                journal += entry
                saveJournal(journal)
                scrollDown = true
            },
            beginScrollDown = {scrollState,coroutineScope ->
                coroutineScope.launch {
                    scrollState.animateScrollTo(1000000000)
                }
            })

        }
    }
}

@Composable
fun DisplayThread(name: String, body: String, scrollDown: Boolean, beginScrollDown: (ScrollState,CoroutineScope) -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    Column (Modifier.height(350.dp)){
        Text(text = name, color = Color.Blue)
        Column(Modifier.verticalScroll(scrollState)) {
            Text(text = body, Modifier.fillMaxWidth())
            if (scrollDown) {
                beginScrollDown(scrollState,coroutineScope)
            }
        }
    }
}

@Composable
fun TextInputBar(enterText: (String) -> Unit, beginScrollDown: (ScrollState, CoroutineScope) -> Unit) {
    var text by rememberSaveable { mutableStateOf("") }

    Row() {
        Column(modifier = Modifier
            .weight(5f)
            .height(100.dp)
            .verticalScroll(rememberScrollState())) {
            val coroutineScope = rememberCoroutineScope()
            val scrollState = rememberScrollState()
            TextField(value = text, onValueChange = {
                            text = it
                            beginScrollDown(scrollState,coroutineScope)
                                                    }
                , modifier = Modifier.fillMaxWidth())
        }
        Button(onClick = {
            enterText(text)
            text = ""
        }, modifier = Modifier.weight(1f)) {
            Text("send")
        }
    }
}