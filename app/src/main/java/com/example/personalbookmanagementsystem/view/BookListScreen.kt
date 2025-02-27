package com.example.personalbookmanagementsystem.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.personalbookmanagementsystem.model.Book
import com.example.personalbookmanagementsystem.model.BookDao
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookListScreen(
    bookDao: BookDao,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var isSorted by remember { mutableStateOf(false) }
    var books by remember { mutableStateOf(listOf<Book>()) }
    var editingBook by remember { mutableStateOf<Book?>(null) }

    // States for genre filtering
    var selectedGenre by remember { mutableStateOf("All") }
    var filterExpanded by remember { mutableStateOf(false) }

    // Predefined genres (alphabetically sorted with "All" as the first option)
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

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        books = bookDao.getAllBooks()
    }

    // Filter by search query and genre (if selectedGenre is not "All")
    val filteredBooks = remember {
        derivedStateOf {
            books.filter { book ->
                (book.title.contains(searchQuery, ignoreCase = true) ||
                        book.author.contains(searchQuery, ignoreCase = true)) &&
                        (selectedGenre == "All" || book.genre.equals(selectedGenre, ignoreCase = true))
            }
        }
    }

    val sortedBooks = remember {
        derivedStateOf {
            if (isSorted) filteredBooks.value.sortedBy { it.title }
            else filteredBooks.value
        }
    }

    Column(modifier = modifier.padding(16.dp)) {
        // Search Field
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search for a book") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        var showGenreDialog by remember { mutableStateOf(false) }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Filter Button
            Button(onClick = { showGenreDialog = true }) {
                Text("Filter: $selectedGenre")
            }

            if (showGenreDialog) {
                AlertDialog(
                    onDismissRequest = { showGenreDialog = false },
                    confirmButton = {},
                    title = { Text("Select Genre") },
                    text = {
                        Column {
                            genres.forEach { genreOption ->
                                TextButton(
                                    onClick = {
                                        selectedGenre = genreOption
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

            // Sort Button
            Button(onClick = { isSorted = !isSorted }) {
                Text(if (isSorted) "Disable Sorting" else "Sort by Title")
            }
        }


        Spacer(modifier = Modifier.height(16.dp))

        // Track progress stats
        val totalBooks = books.size
        val completedBooks = books.count { it.progress == 100 }

        Text(
            text = "Books Read: $completedBooks / $totalBooks",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(vertical = 8.dp)
        )


        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(sortedBooks.value) { book ->
                BookCard(
                    book = book,
                    onEdit = { editingBook = book },
                    onDelete = {
                        coroutineScope.launch {
                            bookDao.deleteBook(book)
                            books = bookDao.getAllBooks()
                            snackbarHostState.showSnackbar("Deleted: ${book.title}")
                        }
                    },
                    onProgressChange = { newProgress ->
                        coroutineScope.launch {
                            bookDao.updateBook(book.copy(progress = newProgress))
                            books = bookDao.getAllBooks()
                        }
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
                coroutineScope.launch {
                    bookDao.updateBook(updatedBook)
                    books = bookDao.getAllBooks()
                }
                editingBook = null
            }
        )
    }
}
