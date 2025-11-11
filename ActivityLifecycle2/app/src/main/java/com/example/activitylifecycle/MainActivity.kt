package com.example.activitylifecycle

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {

    private val TAG = "ActivityLifecycle"
    private val lifecycleLogs = mutableStateListOf<String>()

    private fun addLog(message: String) {
        Log.d(TAG, message)
        lifecycleLogs.add(message)
        if (lifecycleLogs.size > 20) {
            lifecycleLogs.removeAt(0)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addLog(" onCreate вызван")

        savedInstanceState?.getStringArrayList("lifecycle_logs")?.let {
            lifecycleLogs.clear()
            lifecycleLogs.addAll(it)
        }

        setContent {
            // Используем тему из themes.xml
            LifecycleDemoScreen(logs = lifecycleLogs)
        }
    }

    override fun onStart() {
        super.onStart()
        addLog(" onStart вызван")
    }

    override fun onResume() {
        super.onResume()
        addLog(" onResume вызван")
    }

    override fun onPause() {
        super.onPause()
        addLog(" onPause вызван")
    }

    override fun onStop() {
        super.onStop()
        addLog(" onStop вызван")
    }

    override fun onRestart() {
        super.onRestart()
        addLog(" onRestart вызван")
    }

    override fun onDestroy() {
        super.onDestroy()
        addLog(" onDestroy вызван")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        addLog(" onSaveInstanceState вызван")
        outState.putStringArrayList("lifecycle_logs", ArrayList(lifecycleLogs))
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        addLog(" onRestoreInstanceState вызван")
    }
}

@Composable
fun LifecycleDemoScreen(logs: List<String>) {
    // Используем MaterialTheme без кастомных настроек
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Activity Lifecycle Demo",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Журнал событий жизненного цикла:",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        if (logs.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Журнал пуст...\n\nВыполните действия из инструкции",
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                        } else {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState())
                            ) {
                                logs.forEach { log ->
                                    Text(
                                        text = log,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Инструкция:",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Text(text = "1. Пуск")
                        Text(text = "2. Home")
                        Text(text = "3. Развернуть")
                        Text(text = "4. Повернуть")
                    }
                }
            }
        }
    }
}