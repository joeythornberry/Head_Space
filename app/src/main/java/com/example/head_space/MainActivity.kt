package com.example.head_space

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.head_space.ui.theme.CreateNewThreadDialog
import com.example.head_space.ui.theme.Head_SpaceTheme
import java.io.File
import java.util.concurrent.TimeUnit

val WORK_TAG = "work_tag"
var sendNotifications = true
var notificationPeriod: Long = 30
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel("UNREAD_MESSAGES_CHANNEL_ID",
                "Periodic Notifications",
                NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = "Periodically reminds user to check messages"
            mNotificationManager.createNotificationChannel(channel)
    }

    val settings = File(filesDir,"settings.txt")
    if(settings.exists()) {
        settings.forEachLine {
            if (it.substringBefore(':') == "sendNotifications") {
                sendNotifications = if(it.substringAfter(':') == "true") true else false
            }
        }
    } else {
        sendNotifications = true
        val fileOutputStream =
            openFileOutput("settings.txt", ComponentActivity.MODE_PRIVATE)
        fileOutputStream.write("sendNotifications:true".toByteArray())
        scheduleOneTimeNotification(notificationPeriod,this)
    }

        setContent {
            Head_SpaceTheme {
                Head_SpaceApp()
            }
        }
    }

    @Composable
    fun Head_SpaceApp(
        modifier: Modifier = Modifier,
        viewModel: Head_SpaceViewModel = viewModel()) {
        val navController = rememberNavController()
        Column {
            NavigationBar(
                toSettingsScreen = { navController.navigate("settings") },
                toUserScreen = { navController.navigate("user") },
                user = viewModel.uiState.collectAsState().value.user
            )
            NavHost(
                navController = navController,
                startDestination = "user",
                modifier = Modifier
            ) {
                composable(route = "thread") {
                    val text = File(filesDir, viewModel.uiState.collectAsState().value.thread)
                    ThreadScreen(viewModel,
                        text = if (text.exists()) text.readText() else "",
                        saveJournal = {
                            val fileOutputStream = openFileOutput(viewModel.uiState.value.thread, MODE_PRIVATE)
                            fileOutputStream.write(it.toByteArray())
                        })
                }
                composable(route = "user") {
                    UserScreen(
                        users = viewModel.uiState.collectAsState().value.users,
                        selectUser = {
                            viewModel.setUser(it)
                            navController.navigate("choose_thread")
                        })
                }
                composable(route = "settings") {
                    SettingsScreen(saveSettings = {
                        val fileOutputStream =
                            openFileOutput("settings.txt", ComponentActivity.MODE_PRIVATE)
                        fileOutputStream.write(it.toByteArray())
                    })
                }
                composable(route = "choose_thread") {
                    val filenames = loadThreads(filesDir,)
                    if (viewModel.uiState.collectAsState().value.openCreateNewThreadDialog) {
                        CreateNewThreadDialog(
                            openOrCloseCreateNewThreadDialog = {viewModel.openOrCloseCreateNewThreadDialog()},
                            createNewThread = {
                                if (!(it in filenames)) {
                                    val fileOutputStream =
                                        openFileOutput(it, ComponentActivity.MODE_PRIVATE)
                                    fileOutputStream.write(it.toByteArray())
                                    viewModel.openOrCloseCreateNewThreadDialog()
                                } else {
                                    Toast.makeText(this@MainActivity, "Thread already exists", Toast.LENGTH_SHORT).show()
                                }
                            })
                    }
                    ChooseThreadScreen(
                        createNewThread = {
                            viewModel.openOrCloseCreateNewThreadDialog()
                    },  navigateToThread = {
                        viewModel.setThread(it)
                        navController.navigate("thread")
                                           },
                        filenames = filenames
                    )
                }
            }
        }
    }
}

fun loadThreads(filesDir: File): List<String> {
    val threads: MutableList<String> = mutableListOf()
    for (file in filesDir.listFiles()
        .filter { it.canRead() && it.isFile && it.name.endsWith(".thr") }!!) {
        threads.add(file.name)
    }
    return threads.toList()
}

fun scheduleOneTimeNotification(initialDelay: Long, context: Context) {
    Log.d("call schedule function","notifying")
    val work =
        OneTimeWorkRequestBuilder<OneTimeScheduleWorker>()
            .setInitialDelay(initialDelay, TimeUnit.MINUTES)
            .addTag(WORK_TAG)
            .build()

    WorkManager.getInstance(context).enqueue(work)
}

