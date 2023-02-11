package com.example.head_space

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun UserScreen(users: List<String>, selectUser: (String) -> Unit) {
    Column {
        Text("pick your name =)", color = Color.Blue)
        UserButtonList(users = users, selectUser = { selectUser(it) })
    }
}

@Composable
fun UserButtonList(users: List<String>, selectUser: (String) -> Unit) {
    Column() {
        for(user in users) {
            Button(onClick = { selectUser(user) }) {
                Text(user)
            }
        }
    }
}