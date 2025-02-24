package com.example.personalbookmanagementsystem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.personalbookmanagementsystem.Book
import com.example.personalbookmanagementsystem.BookDao
import com.example.personalbookmanagementsystem.ui.theme.PersonalBookManagementSystemTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = DatabaseInstance.getDatabase(applicationContext)
        val bookDao = db.bookDao()

        setContent {
            PersonalBookManagementSystemTheme {
                val snackbarHostState = remember { SnackbarHostState() }

                Scaffold(
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    BookListScreen(
                        bookDao = bookDao,
                        snackbarHostState = snackbarHostState,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

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
                        val newBook = Book(title = title, author = author, genre = genre, dateAdded = System.currentTimeMillis()
                            .toString(), progress = 0)
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
}

@Composable
fun BookCard(book: Book, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = "${book.title} by ${book.author}")
                Text(text = "Genre: ${book.genre}")
            }
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Book"
                )
            }
        }
    }
}