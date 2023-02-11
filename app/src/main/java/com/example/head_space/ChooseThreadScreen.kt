package com.example.head_space

import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import java.io.File

@Composable
fun ChooseThreadScreen(createNewThread: () -> Unit, navigateToThread: (String) -> Unit, filenames: List<String>) {

    Column (modifier = Modifier.verticalScroll(rememberScrollState())) {
        Button(onClick = { createNewThread() }) {
            Text("New Thread")
        }
        for (filename in filenames) {
            Button(onClick = { navigateToThread(filename) }) {
                Text(filename)
            }
        }
    }
}
