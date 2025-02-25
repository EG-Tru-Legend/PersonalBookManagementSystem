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
    val coroutineScope = rememberCoroutineScope()
    val books = remember { mutableStateListOf<Book>() }
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var editingBook by remember { mutableStateOf<Book?>(null) }

    LaunchedEffect(Unit) {
        books.addAll(bookDao.getAllBooks())
    }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
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
                            title = title,
                            author = author,
                            genre = genre,
                            dateAdded = System.currentTimeMillis().toString(),
                            progress = 0
                        )
                        bookDao.insertBook(newBook)
                        books.clear()
                        books.addAll(bookDao.getAllBooks())
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
            items(books) { book ->
                BookCard(
                    book = book,
                    onEdit = { editingBook = book },
                    onDelete = {
                        coroutineScope.launch {
                            bookDao.deleteBookByTitle(book.title)
                            books.clear()
                            books.addAll(bookDao.getAllBooks())
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
                    bookDao.insertBook(updatedBook)
                    books.clear()
                    books.addAll(bookDao.getAllBooks())
                }
                editingBook = null
            }
        )
    }
}
