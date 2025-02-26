package com.example.personalbookmanagementsystem.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.personalbookmanagementsystem.model.Book
import com.example.personalbookmanagementsystem.model.BookDao
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookScreen(
    bookDao: BookDao,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onBookAdded: () -> Unit = {}
) {
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var selectedGenre by remember { mutableStateOf("") }
    var genreExpanded by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // Predefined genres sorted alphabetically
    val genres = listOf(
        "Academic Papers",
        "Action Adventure",
        "Comic",
        "Fantasy",
        "Historical",
        "Horror",
        "Manga",
        "Mystery",
        "Paranormal",
        "Romance",
        "Science Fiction",
        "Science Fiction & Fantasy",
        "Thriller"
    )

    Column(modifier = modifier.padding(16.dp)) {
        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Book Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = author,
            onValueChange = { author = it },
            label = { Text("Author") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Genre Dropdown using Material 3's ExposedDropdownMenuBox
        ExposedDropdownMenuBox(
            expanded = genreExpanded,
            onExpandedChange = { genreExpanded = !genreExpanded }
        ) {
            TextField(
                value = selectedGenre,
                onValueChange = {},
                readOnly = true,
                label = { Text("Genre") },
                trailingIcon = { TrailingIcon(expanded = genreExpanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = genreExpanded,
                onDismissRequest = { genreExpanded = false }
            ) {
                genres.forEach { genreOption ->
                    DropdownMenuItem(
                        text = { Text(text = genreOption) },
                        onClick = {
                            selectedGenre = genreOption
                            genreExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    if (title.isNotEmpty() && author.isNotEmpty()) {
                        val newBook = Book(
                            id = 0,
                            title = title,
                            author = author,
                            genre = selectedGenre,
                            dateAdded = System.currentTimeMillis().toString(),
                            progress = 0
                        )
                        bookDao.insertBook(newBook)
                        snackbarHostState.showSnackbar("Book added: $title")
                        title = ""
                        author = ""
                        selectedGenre = ""
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
