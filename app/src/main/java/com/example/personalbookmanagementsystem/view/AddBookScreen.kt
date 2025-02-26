package com.example.personalbookmanagementsystem.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.personalbookmanagementsystem.model.Book
import com.example.personalbookmanagementsystem.model.BookDao
import kotlinx.coroutines.launch

@Composable
fun AddBookScreen(
    bookDao: BookDao,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onBookAdded: () -> Unit = {} // Optional callback to navigate after adding a book
) {
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = modifier.padding(16.dp)) {
        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Book Title") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = author,
            onValueChange = { author = it },
            label = { Text("Author") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = genre,
            onValueChange = { genre = it },
            label = { Text("Genre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    if (title.isNotEmpty() && author.isNotEmpty()) {
                        val newBook = Book(
                            id = 0,
                            title = title,
                            author = author,
                            genre = genre,
                            dateAdded = System.currentTimeMillis().toString(),
                            progress = 0
                        )
                        bookDao.insertBook(newBook)
                        snackbarHostState.showSnackbar("Book added: $title")
                        // Reset the fields
                        title = ""
                        author = ""
                        genre = ""
                        // Optionally switch back to the list screen
                        onBookAdded()
                    } else {
                        snackbarHostState.showSnackbar("Title and Author are required.")
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Book")
        }
    }
}
