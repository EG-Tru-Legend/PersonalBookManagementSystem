package com.example.personalbookmanagementsystem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.personalbookmanagementsystem.model.DatabaseInstance
import com.example.personalbookmanagementsystem.navigation.AppNavigation
import com.example.personalbookmanagementsystem.navigation.AppNavigationDrawer
import com.example.personalbookmanagementsystem.navigation.Screen
import com.example.personalbookmanagementsystem.ui.theme.PersonalBookManagementSystemTheme
import com.example.personalbookmanagementsystem.viewmodel.BookViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import kotlinx.coroutines.launch

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

                // Create drawer state here so it can be shared
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()

                AppNavigationDrawer(
                    currentScreen = currentScreen,
                    onScreenChange = { screen ->
                        currentScreen = screen
                    },
                    drawerState = drawerState
                ) {
                    AppNavigation(
                        bookViewModel = bookViewModel,
                        snackbarHostState = snackbarHostState,
                        currentScreen = currentScreen,
                        onScreenChange = { screen ->
                            currentScreen = screen
                        },
                        onOpenDrawer = {
                            scope.launch { drawerState.open() }
                        }
                    )
                }
            }
        }
    }
}