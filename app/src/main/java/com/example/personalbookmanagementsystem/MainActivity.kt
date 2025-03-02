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
import com.example.personalbookmanagementsystem.model.DatabaseInstance
import com.example.personalbookmanagementsystem.ui.AddBookScreen
import com.example.personalbookmanagementsystem.ui.BookListScreen
import com.example.personalbookmanagementsystem.ui.theme.PersonalBookManagementSystemTheme
import com.example.personalbookmanagementsystem.viewmodel.BookViewModel
import kotlinx.coroutines.launch

enum class Screen {
    BOOK_LIST, ADD_BOOK
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
                var currentScreen by remember { mutableStateOf(Screen.BOOK_LIST) }
                val scope = rememberCoroutineScope()
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

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
                                    IconButton(onClick = {
                                        scope.launch { drawerState.open() }
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Menu,
                                            contentDescription = "Open Drawer"
                                        )
                                    }
                                }
                            )
                        },
                        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                    ) { innerPadding ->
                        when (currentScreen) {
                            Screen.BOOK_LIST -> BookListScreen(
                                viewModel = bookViewModel,
                                snackbarHostState = snackbarHostState,
                                modifier = Modifier.padding(innerPadding)
                            )
                            Screen.ADD_BOOK -> AddBookScreen(
                                viewModel = bookViewModel,
                                snackbarHostState = snackbarHostState,
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                    }
                }
            }
        }
    }
}