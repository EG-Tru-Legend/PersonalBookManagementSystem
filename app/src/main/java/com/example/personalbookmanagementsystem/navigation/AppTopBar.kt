package com.example.personalbookmanagementsystem.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    currentScreen: Screen,
    onNavigateBack: () -> Unit,
    onOpenDrawer: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                when(currentScreen) {
                    is Screen.BookList -> "My Book Library"
                    is Screen.AddBook -> "Add New Book"
                    is Screen.BookDetail -> "Book Details"
                }
            )
        },
        navigationIcon = {
            if (currentScreen is Screen.BookDetail) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            } else {
                IconButton(onClick = onOpenDrawer) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Open Drawer"
                    )
                }
            }
        }
    )
}