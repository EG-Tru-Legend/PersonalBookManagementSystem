package com.example.personalbookmanagementsystem.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.personalbookmanagementsystem.model.Book
import com.example.personalbookmanagementsystem.viewmodel.BookViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookScreen(
    viewModel: BookViewModel,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onBookAdded: () -> Unit = {}
) {
    // Variables for
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var selectedGenre by remember { mutableStateOf("") }
    var totalPages by remember { mutableStateOf("") }
    var genreExpanded by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

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
        "Fiction",
        "Science Fiction & Fantasy",
        "Thriller"
    )
    // Input fields for adding a new book
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

        TextField(
            value = totalPages,
            onValueChange = { totalPages = it.filter { c -> c.isDigit() } },
            label = { Text("Total Pages") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Button to submit the form and add the book
        Button(
            onClick = {
                coroutineScope.launch {
                    // Check if the title and author fields are filled
                    if (title.isNotEmpty() && author.isNotEmpty()) {
                        val newBook = Book(
                            id = 0,
                            title = title,
                            author = author,
                            genre = selectedGenre,
                            dateAdded = System.currentTimeMillis().toString(),
                            progress = 0,
                            totalPages = totalPages.toIntOrNull() ?: 0,
                            currentPage = 0
                        )
                        viewModel.addBook(newBook)
                        snackbarHostState.showSnackbar("Book added: $title")
                        title = ""
                        author = ""
                        selectedGenre = ""
                        totalPages = ""
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