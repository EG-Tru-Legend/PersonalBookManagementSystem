package com.example.personalbookmanagementsystem.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.personalbookmanagementsystem.ui.AddBookScreen
import com.example.personalbookmanagementsystem.ui.BookDetailScreen
import com.example.personalbookmanagementsystem.ui.BookListScreen
import com.example.personalbookmanagementsystem.viewmodel.BookViewModel
import kotlinx.coroutines.launch

@Composable
fun AppNavigation(
    bookViewModel: BookViewModel,
    snackbarHostState: SnackbarHostState,
    currentScreen: Screen,
    onScreenChange: (Screen) -> Unit,
    onOpenDrawer: () -> Unit
) {
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            AppTopBar(
                currentScreen = currentScreen,
                onNavigateBack = {
                    if (currentScreen is Screen.BookDetail) {
                        onScreenChange(Screen.BookList)
                    }
                },
                onOpenDrawer = onOpenDrawer
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        when (val screen = currentScreen) {
            is Screen.BookList -> BookListScreen(
                viewModel = bookViewModel,
                snackbarHostState = snackbarHostState,
                onNavigateToBookDetail = { book ->
                    onScreenChange(Screen.BookDetail(book))
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
                    onScreenChange(Screen.BookList)
                },
                onEdit = { book ->
                },
                onDelete = { book ->
                    bookViewModel.deleteBook(book)
                    onScreenChange(Screen.BookList)
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = "Book deleted successfully",
                            duration = SnackbarDuration.Short
                        )
                    }
                },
                onEmail = { book ->
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