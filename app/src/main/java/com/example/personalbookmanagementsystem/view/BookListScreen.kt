package com.example.personalbookmanagementsystem.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.personalbookmanagementsystem.model.Book
import com.example.personalbookmanagementsystem.utils.EmailUtils
import com.example.personalbookmanagementsystem.viewmodel.BookViewModel
import kotlinx.coroutines.launch

@Composable
fun BookListScreen(
    viewModel: BookViewModel,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedGenre by viewModel.selectedGenre.collectAsState()
    val isSorted by viewModel.isSorted.collectAsState()
    val filteredBooks by viewModel.filteredBooks.collectAsState()
    val allBooks by viewModel.books.collectAsState()

    val context = LocalContext.current
    var editingBook by remember { mutableStateOf<Book?>(null) }
    var showFilterSortDialog by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    Column(modifier = modifier.padding(16.dp)) {
        TextField(
            value = searchQuery,
            onValueChange = { viewModel.setSearchQuery(it) },
            label = { Text("Search for a book") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Button(
                onClick = { showFilterSortDialog = true }
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Filter and Sort"
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Filter & Sort")
            }

            Button(
                onClick = {
                    EmailUtils.shareBookList(context, filteredBooks)
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = "Opening sharing options...",
                            duration = SnackbarDuration.Short
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Share Book List"
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Share List")
            }
        }

        if (selectedGenre != "All" || isSorted) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(
                    text = "Active filters:",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                if (selectedGenre != "All") {
                    AssistChip(
                        onClick = { },
                        label = { Text("Genre: $selectedGenre") },
                        modifier = Modifier.padding(bottom = 4.dp) // Adds spacing between filters
                    )
                }

                if (isSorted) {
                    AssistChip(
                        onClick = { },
                        label = { Text("Sorted by title") }
                    )
                }
            }
        }


        FilterSortDialog(
            showDialog = showFilterSortDialog,
            currentGenre = selectedGenre,
            isSorted = isSorted,
            onGenreSelected = { genre ->
                viewModel.setSelectedGenre(genre)
            },
            onSortToggled = {
                viewModel.toggleSort()
            },
            onDismiss = {
                showFilterSortDialog = false
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

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
                    onEmail = {
                        EmailUtils.shareBookDetails(context, book)
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Opening sharing options...",
                                duration = SnackbarDuration.Short
                            )
                        }
                    },
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