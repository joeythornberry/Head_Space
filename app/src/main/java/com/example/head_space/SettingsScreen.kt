package com.example.head_space

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

@Composable
fun SettingsScreen(saveSettings: (String) -> Unit) {
    Column {
        var mSendNotifications by rememberSaveable { mutableStateOf(sendNotifications) }
        val context = LocalContext.current
        Text("settings", color = Color.Blue)
        Row {
            Switch(
                checked = mSendNotifications,
                onCheckedChange = {
                    sendNotifications = it
                    mSendNotifications = it
                    var settings = ""
                    settings += "\nsendNotifications:" + sendNotifications
                    saveSettings(settings)
                })
            Text("Periodic Notifications")
        }
    }
}