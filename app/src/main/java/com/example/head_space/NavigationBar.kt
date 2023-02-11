package com.example.head_space

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun NavigationBar(
    toSettingsScreen: () -> Unit,
    toUserScreen: () -> Unit,
    user: String
) {
    Row () {
        Text(user)
        Button(onClick = {toSettingsScreen()}) {
            Text("Settings")
        }
        Button(onClick = {toUserScreen()}) {
            Text("Users")
        }
    }
}