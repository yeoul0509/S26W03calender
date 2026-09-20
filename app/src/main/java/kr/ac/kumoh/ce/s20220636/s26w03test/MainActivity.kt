package kr.ac.kumoh.ce.s20220636.s26w03test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.ac.kumoh.ce.s20220636.s26w03test.ui.theme.S26W03testTheme


data class TodoItem(
    val id: Int,
    val text: String,
    val isDone: Boolean = false
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            S26W03testTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            var textInput by remember { mutableStateOf("") }

            var todoList by remember { mutableStateOf(listOf<TodoItem>()) }
            var nextId by remember { mutableStateOf(1) }

            val completedCount = todoList.count { it.isDone }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                Text(
                    text = "완료 현황: $completedCount / ${todoList.size}",
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = textInput,
                        onValueChange = { textInput = it },
                        label = { Text("할 일 입력") },
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            if (textInput.isNotBlank()) {
                                todoList = todoList + TodoItem(id = nextId++, text = textInput)
                                textInput = ""
                            }
                        }
                    ) {
                        Text("추가")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(todoList, key = { it.id }) { item ->
                        TodoRow(
                            item = item,
                            onToggleDone = { targetId ->
                                todoList = todoList.map {
                                    if (it.id == targetId) it.copy(isDone = !it.isDone) else it
                                }
                            },
                            onDelete = { targetId ->
                                todoList = todoList.filter { it.id != targetId }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TodoRow(
    item: TodoItem,
    onToggleDone: (Int) -> Unit,
    onDelete: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggleDone(item.id) },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = item.isDone,
                    onCheckedChange = { onToggleDone(item.id) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = item.text,
                    textDecoration = if (item.isDone) TextDecoration.LineThrough else TextDecoration.None
                )
            }

            TextButton(onClick = { onDelete(item.id) }) {
                Text("삭제", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
