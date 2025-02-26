package com.example.personalbookmanagementsystem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.personalbookmanagementsystem.model.DatabaseInstance
import com.example.personalbookmanagementsystem.view.AddBookScreen
import com.example.personalbookmanagementsystem.ui.BookListScreen
import com.example.personalbookmanagementsystem.ui.theme.PersonalBookManagementSystemTheme
import kotlinx.coroutines.launch

enum class Screen {
    BOOK_LIST,
    ADD_BOOK
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(bookDao: com.example.personalbookmanagementsystem.model.BookDao) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var currentScreen by remember { mutableStateOf(Screen.BOOK_LIST) }
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                NavigationDrawerItem(
                    label = { Text("Book List") },
                    selected = currentScreen == Screen.BOOK_LIST,
                    onClick = {
                        currentScreen = Screen.BOOK_LIST
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                NavigationDrawerItem(
                    label = { Text("Add Book") },
                    selected = currentScreen == Screen.ADD_BOOK,
                    onClick = {
                        currentScreen = Screen.ADD_BOOK
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Personal Book Management") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Open drawer")
                        }
                    }
                )
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        ) { innerPadding ->
            when (currentScreen) {
                Screen.BOOK_LIST -> BookListScreen(
                    bookDao = bookDao,
                    snackbarHostState = snackbarHostState,
                    modifier = Modifier.padding(innerPadding)
                )
                Screen.ADD_BOOK -> AddBookScreen(
                    bookDao = bookDao,
                    snackbarHostState = snackbarHostState,
                    modifier = Modifier.padding(innerPadding),
                    onBookAdded = { currentScreen = Screen.BOOK_LIST }
                )
            }
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = DatabaseInstance.getDatabase(applicationContext)
        val bookDao = db.bookDao()

        setContent {
            PersonalBookManagementSystemTheme {
                // Calling MainScreen so the function is used and the UI is rendered.
                MainScreen(bookDao = bookDao)
            }
        }
    }
}
