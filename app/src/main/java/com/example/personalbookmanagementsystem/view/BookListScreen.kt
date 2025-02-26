package com.example.personalbookmanagementsystem.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.personalbookmanagementsystem.model.Book
import com.example.personalbookmanagementsystem.model.BookDao
import kotlinx.coroutines.launch

@Composable
fun BookListScreen(
    bookDao: BookDao,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedGenre by remember { mutableStateOf("") }
    var isSorted by remember { mutableStateOf(false) }
    var books by remember { mutableStateOf(listOf<Book>()) }
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var editingBook by remember { mutableStateOf<Book?>(null) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        books = bookDao.getAllBooks()
    }

    // Filter and sort logic
    val filteredBooks = remember {
        derivedStateOf {
            books.filter {
                it.title.contains(searchQuery, ignoreCase = true) ||
                        it.author.contains(searchQuery, ignoreCase = true)
            }.filter {
                selectedGenre.isEmpty() || it.genre.equals(selectedGenre, ignoreCase = true)
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

        // Filter by Genre
        TextField(
            value = selectedGenre,
            onValueChange = { selectedGenre = it },
            label = { Text("Filter by Genre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Sorting Button
        Button(
            onClick = { isSorted = !isSorted },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(if (isSorted) "Disable Sorting" else "Sort by Title")
        }

        // Add Book Fields
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
            label = { Text("Genre (Optional)") },
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
                        books = bookDao.getAllBooks()
                        snackbarHostState.showSnackbar("Book added: $title")
                        title = ""
                        author = ""
                        genre = ""
                    } else {
                        snackbarHostState.showSnackbar("Title and Author are required.")
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Book")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(sortedBooks.value) { book ->
                BookCard(
                    book = book,
                    onEdit = { editingBook = book },
                    onDelete = {
                        coroutineScope.launch {
                            bookDao.deleteBook(book)
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




