package com.example.head_space.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CreateNewThreadDialog(openOrCloseCreateNewThreadDialog: () -> Unit,createNewThread: (String) -> Unit) {

    var name by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = {},
        title = {
            Text(text = "Name New Thread")
        },
        text = {
            Column() {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it }
                )
            }
        },
        buttons = {
            Row(
                modifier = Modifier.padding(all = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    modifier = Modifier,
                    onClick = {
                     createNewThread(name.trim() + ".thr")
                    }
                ) {
                    Text("Create Thread")
                }
                Button(
                    modifier = Modifier,
                    onClick = {
                        openOrCloseCreateNewThreadDialog()
                    }
                ) {
                    Text("Cancel")
                }
            }
        }
    )
}