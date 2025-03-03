package com.example.personalbookmanagementsystem.utils

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FilterSortDialog(
    showDialog: Boolean,
    currentGenre: String,
    isSorted: Boolean,
    onGenreSelected: (String) -> Unit,
    onSortToggled: () -> Unit,
    onDismiss: () -> Unit
) {
    val genres = listOf(
        "All",
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

    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Filter & Sort") },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    // Sort option
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Sort by title",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(1f)
                        )
                        Switch(
                            checked = isSorted,
                            onCheckedChange = { onSortToggled() }
                        )
                    }

                    Divider()
                    Spacer(modifier = Modifier.height(16.dp))

                    // Genre filter section
                    Text(
                        text = "Filter by genre",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 320.dp)
                    ) {
                        items(genres) { genre ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .selectable(
                                        selected = currentGenre == genre,
                                        onClick = { onGenreSelected(genre) }
                                    )
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = currentGenre == genre,
                                    onClick = { onGenreSelected(genre) }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(genre)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    Text("Done")
                }
            }
        )
    }
}