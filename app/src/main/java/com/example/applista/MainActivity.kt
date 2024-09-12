package com.example.applista

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.Alignment
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.applista.ui.theme.AppListTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppListTheme {
                TaskListScreen()
            }
        }
    }
}

@Composable
fun TaskListScreen() {
    var tasks by remember { mutableStateOf(listOf<Task>()) }
    var newTaskTitle by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        NewTaskInput(
            taskTitle = newTaskTitle,
            onTaskTitleChange = { newTaskTitle = it },
            onAddTask = {
                if (newTaskTitle.isNotEmpty()) {
                    tasks = tasks + Task(title = newTaskTitle, isCompleted = false)
                    newTaskTitle = ""
                }
            }
        )
        TaskList(
            tasks = tasks,
            onTaskCheckedChange = { task, isChecked ->
                tasks = tasks.map {
                    if (it == task) it.copy(isCompleted = isChecked) else it
                }
            },
            onTaskRemove = { task ->
                tasks = tasks - task
            }
        )
    }
}

@Composable
fun NewTaskInput(taskTitle: String, onTaskTitleChange: (String) -> Unit, onAddTask: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        TextField(
            value = taskTitle,
            onValueChange = onTaskTitleChange,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            placeholder = { Text("Enter task") }
        )
        Button(onClick = onAddTask) {
            Text("Add Task")
        }
    }
}

@Composable
fun TaskList(tasks: List<Task>, onTaskCheckedChange: (Task, Boolean) -> Unit, onTaskRemove: (Task) -> Unit) {
    LazyColumn {
        items(tasks) { task ->
            TaskRow(task, onTaskCheckedChange, onTaskRemove)
        }
    }
}

@Composable
fun TaskRow(task: Task, onTaskCheckedChange: (Task, Boolean) -> Unit, onTaskRemove: (Task) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { isChecked ->
                    onTaskCheckedChange(task, isChecked)
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = task.title,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f)
            )
        }
        IconButton(onClick = { onTaskRemove(task) }) {
            Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete")
        }
    }
}

data class Task(
    val title: String,
    val isCompleted: Boolean
)
