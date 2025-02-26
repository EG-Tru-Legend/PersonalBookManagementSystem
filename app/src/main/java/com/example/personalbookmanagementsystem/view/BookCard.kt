package com.example.personalbookmanagementsystem.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.personalbookmanagementsystem.model.Book

@Composable
fun BookCard(
    book: Book,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onProgressChange: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    // Title on the first line
                    Text(text = book.title, style = MaterialTheme.typography.titleLarge)
                    // Author on the next line
                    Text(text = book.author, style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Genre: ${book.genre}", style = MaterialTheme.typography.bodySmall)
                }
                Row {
                    IconButton(onClick = onEdit) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Book"
                        )
                    }
                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Book"
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            // Initialize slider with the book's current progress value
            var sliderValue by remember { mutableStateOf(book.progress.toFloat()) }
            Text(text = "Progress: ${sliderValue.toInt()}%")
            Slider(
                value = sliderValue,
                onValueChange = { newValue ->
                    sliderValue = newValue
                },
                onValueChangeFinished = {
                    onProgressChange(sliderValue.toInt())
                },
                valueRange = 0f..100f,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
