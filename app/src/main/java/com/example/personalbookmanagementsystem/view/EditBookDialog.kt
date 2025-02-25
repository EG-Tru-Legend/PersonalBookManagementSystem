package com.example.personalbookmanagementsystem.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.personalbookmanagementsystem.model.Book

@Composable
fun EditBookDialog(book: Book, onDismiss: () -> Unit, onSave: (Book) -> Unit) {
    var title by remember { mutableStateOf(book.title) }
    var author by remember { mutableStateOf(book.author) }
    var genre by remember { mutableStateOf(book.genre ?: "") }
    var progress by remember { mutableStateOf(book.progress.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Book") },
        text = {
            Column {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Book Title") }
                )
                TextField(
                    value = author,
                    onValueChange = { author = it },
                    label = { Text("Author") }
                )
                TextField(
                    value = genre,
                    onValueChange = { genre = it },
                    label = { Text("Genre (Optional)") }
                )
                TextField(
                    value = progress,
                    onValueChange = { progress = it.filter { c -> c.isDigit() } },
                    label = { Text("Progress (%)") }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                if (title.isNotEmpty() && author.isNotEmpty()) {
                    onSave(
                        book.copy(
                            title = title,
                            author = author,
                            genre = genre,
                            progress = progress.toIntOrNull() ?: 0
                        )
                    )
                }
            }) { Text("Save") }
        },
        dismissButton = {
            Button(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
