package com.example.personalbookmanagementsystem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.personalbookmanagementsystem.model.Book
import com.example.personalbookmanagementsystem.model.DatabaseInstance
import com.example.personalbookmanagementsystem.ui.AddBookScreen
import com.example.personalbookmanagementsystem.ui.BookDetailScreen
import com.example.personalbookmanagementsystem.ui.BookListScreen
import com.example.personalbookmanagementsystem.ui.theme.PersonalBookManagementSystemTheme
import com.example.personalbookmanagementsystem.viewmodel.BookViewModel
import kotlinx.coroutines.launch

sealed class Screen {
    object BookList : Screen()
    object AddBook : Screen()
    data class BookDetail(val book: Book) : Screen()
}

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = DatabaseInstance.getDatabase(applicationContext)
        val bookDao = db.bookDao()

        setContent {
            PersonalBookManagementSystemTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                val bookViewModel: BookViewModel = viewModel(
                    factory = BookViewModel.Factory(bookDao)
                )
                var currentScreen by remember { mutableStateOf<Screen>(Screen.BookList) }
                val scope = rememberCoroutineScope()
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet {
                            NavigationDrawerItem(
                                label = { Text("Library") },
                                selected = currentScreen is Screen.BookList,
                                onClick = {
                                    currentScreen = Screen.BookList
                                    scope.launch { drawerState.close() }
                                },
                                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                            )
                            NavigationDrawerItem(
                                label = { Text("Add Book") },
                                selected = currentScreen is Screen.AddBook,
                                onClick = {
                                    currentScreen = Screen.AddBook
                                    scope.launch { drawerState.close() }
                                },
                                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                            )
                        }
                    },
                    gesturesEnabled = currentScreen !is Screen.BookDetail
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text(
                                        when(currentScreen) {
                                            is Screen.BookList -> "Personal Book Management"
                                            is Screen.AddBook -> "Add New Book"
                                            is Screen.BookDetail -> "Book Details"
                                        }
                                    )
                                },
                                navigationIcon = {
                                    if (currentScreen !is Screen.BookDetail) {
                                        IconButton(onClick = {
                                            scope.launch { drawerState.open() }
                                        }) {
                                            Icon(
                                                imageVector = Icons.Default.Menu,
                                                contentDescription = "Open Drawer"
                                            )
                                        }
                                    }
                                }
                            )
                        },
                        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                    ) { innerPadding ->
                        when (val screen = currentScreen) {
                            is Screen.BookList -> BookListScreen(
                                viewModel = bookViewModel,
                                snackbarHostState = snackbarHostState,
                                onNavigateToBookDetail = { book ->
                                    currentScreen = Screen.BookDetail(book)
                                },
                                modifier = Modifier.padding(innerPadding)
                            )
                            is Screen.AddBook -> AddBookScreen(
                                viewModel = bookViewModel,
                                snackbarHostState = snackbarHostState,
                                modifier = Modifier.padding(innerPadding)
                            )
                            is Screen.BookDetail -> BookDetailScreen(
                                book = screen.book,
                                onNavigateBack = {
                                    currentScreen = Screen.BookList
                                },
                                onEdit = { book ->
                                    // Implement edit functionality
                                    // This would typically show an edit dialog
                                    // You can reuse your EditBookDialog here
                                },
                                onDelete = { book ->
                                    bookViewModel.deleteBook(book)
                                    currentScreen = Screen.BookList
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = "Book deleted successfully",
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                },
                                onEmail = { book ->
                                    // Implement email functionality using EmailUtils
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = "Opening sharing options...",
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                },
                                onProgressChange = { book, progress ->
                                    bookViewModel.updateBookProgress(book, progress)
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = "Progress updated successfully",
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}