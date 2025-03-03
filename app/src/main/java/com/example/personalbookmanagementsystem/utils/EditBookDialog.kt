package com.example.personalbookmanagementsystem.utils

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.personalbookmanagementsystem.model.Book

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBookDialog(
    book: Book,
    onDismiss: () -> Unit,
    onSave: (Book) -> Unit
) {
    var title by remember { mutableStateOf(book.title) }
    var author by remember { mutableStateOf(book.author) }
    var selectedGenre by remember { mutableStateOf(book.genre ?: "") }
    var totalPages by remember { mutableStateOf(book.totalPages.toString()) }
    var currentPage by remember { mutableStateOf(book.currentPage.toString()) }
    var progress by remember { mutableStateOf(book.progress.toString()) }
    var genreExpanded by remember { mutableStateOf(false) }

    val genres = listOf(
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

    val calculatedProgress = if (totalPages.toIntOrNull() ?: 0 > 0 && currentPage.toIntOrNull() ?: 0 > 0) {
        ((currentPage.toIntOrNull() ?: 0).toFloat() / (totalPages.toIntOrNull() ?: 1) * 100).toInt().coerceIn(0, 100)
    } else {
        progress.toIntOrNull() ?: 0
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Book") },
        text = {
            Column(modifier = Modifier.padding(8.dp)) {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Book Title") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = author,
                    onValueChange = { author = it },
                    label = { Text("Author") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                ExposedDropdownMenuBox(
                    expanded = genreExpanded,
                    onExpandedChange = { genreExpanded = !genreExpanded }
                ) {
                    TextField(
                        value = selectedGenre,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Genre") },
                        trailingIcon = { TrailingIcon(expanded = genreExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = genreExpanded,
                        onDismissRequest = { genreExpanded = false }
                    ) {
                        genres.forEach { genreOption ->
                            DropdownMenuItem(
                                text = { Text(text = genreOption) },
                                onClick = {
                                    selectedGenre = genreOption
                                    genreExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = totalPages,
                    onValueChange = {
                        totalPages = it.filter { c -> c.isDigit() }
                        val totalPagesInt = totalPages.toIntOrNull() ?: 0
                        val currentPageInt = currentPage.toIntOrNull() ?: 0
                        if (currentPageInt > totalPagesInt && totalPagesInt > 0) {
                            currentPage = totalPages
                        }
                    },
                    label = { Text("Total Pages") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (totalPages.toIntOrNull() ?: 0 > 0) {
                    TextField(
                        value = currentPage,
                        onValueChange = { newValue ->
                            val filtered = newValue.filter { c -> c.isDigit() }
                            val newPageNum = filtered.toIntOrNull() ?: 0
                            val maxPage = totalPages.toIntOrNull() ?: 0
                            currentPage = when {
                                newPageNum > maxPage && maxPage > 0 -> maxPage.toString()
                                else -> filtered
                            }
                        },
                        label = { Text("Current Page") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Progress will be calculated as: $calculatedProgress%",
                        style = MaterialTheme.typography.bodySmall
                    )
                } else {
                    TextField(
                        value = progress,
                        onValueChange = { newValue ->
                            val filtered = newValue.filter { c -> c.isDigit() }
                            val progressNum = filtered.toIntOrNull() ?: 0
                            progress = when {
                                progressNum > 100 -> "100"
                                else -> filtered
                            }
                        },
                        label = { Text("Progress (%)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                if (title.isNotEmpty() && author.isNotEmpty()) {
                    val totalPagesInt = totalPages.toIntOrNull() ?: 0
                    val currentPageInt = currentPage.toIntOrNull() ?: 0

                    val finalProgress = if (totalPagesInt > 0) {
                        ((currentPageInt.toFloat() / totalPagesInt) * 100).toInt().coerceIn(0, 100)
                    } else {
                        progress.toIntOrNull() ?: 0
                    }

                    onSave(
                        book.copy(
                            title = title,
                            author = author,
                            genre = selectedGenre,
                            progress = finalProgress,
                            totalPages = totalPagesInt,
                            currentPage = currentPageInt
                        )
                    )
                }
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) { Text("Cancel") }
        }
    )
}