package com.example.personalbookmanagementsystem.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.personalbookmanagementsystem.model.Book
import com.example.personalbookmanagementsystem.viewmodel.BookViewModel
import kotlinx.coroutines.launch

@Composable
fun BookListScreen(
    viewModel: BookViewModel,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    // Collect state from the ViewModel
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedGenre by viewModel.selectedGenre.collectAsState()
    val isSorted by viewModel.isSorted.collectAsState()
    val filteredBooks by viewModel.filteredBooks.collectAsState()
    val allBooks by viewModel.books.collectAsState()

    var editingBook by remember { mutableStateOf<Book?>(null) }
    var showGenreDialog by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    Column(modifier = modifier.padding(16.dp)) {
        // Search Field
        TextField(
            value = searchQuery,
            onValueChange = { viewModel.setSearchQuery(it) },
            label = { Text("Search for a book") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Row with Filter button and Sort toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { showGenreDialog = true }) {
                Text("Filter: $selectedGenre")
            }
            Button(onClick = { viewModel.toggleSort() }) {
                Text(if (isSorted) "Disable Sorting" else "Sort by Title")
            }
        }

        if (showGenreDialog) {
            // Genre selection dialog
            AlertDialog(
                onDismissRequest = { showGenreDialog = false },
                confirmButton = {},
                title = { Text("Select Genre") },
                text = {
                    Column {
                        // Predefined genres (including "All")
                        val genres = listOf(
                            "All",
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
                        genres.forEach { genreOption ->
                            TextButton(
                                onClick = {
                                    viewModel.setSelectedGenre(genreOption)
                                    showGenreDialog = false
                                }
                            ) {
                                Text(genreOption)
                            }
                        }
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display overall progress information
        val totalBooks = allBooks.size
        val completedBooks = allBooks.count { it.progress == 100 }
        Text(
            text = "Books Read: $completedBooks / $totalBooks",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(filteredBooks, key = { it.id }) { book ->
                BookCard(
                    book = book,
                    onEdit = { editingBook = book },
                    onDelete = { viewModel.deleteBook(book) },
                    onProgressChange = { newProgress ->
                        viewModel.updateBookProgress(book, newProgress)
                    }
                )
                HorizontalDivider()
            }
        }
    }

    editingBook?.let { book ->
        EditBookDialog(
            book = book,
            onDismiss = { editingBook = null },
            onSave = { updatedBook ->
                viewModel.updateBook(updatedBook)
                editingBook = null
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Book updated successfully",
                        duration = SnackbarDuration.Short
                    )
                }
            }
        )
    }
}
