package com.example.personalbookmanagementsystem.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.personalbookmanagementsystem.model.Book
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.graphics.Color

@Composable
fun BookCard(
    book: Book,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onEmail: () -> Unit,
    onProgressChange: (Int) -> Unit
) {
    var pressed by remember { mutableStateOf(false) }
    // Use rememberUpdatedState to respond to book changes from outside
    val currentPage = rememberUpdatedState(book.currentPage)
    var sliderPosition by remember(book.currentPage) { mutableStateOf(book.currentPage.toFloat()) }

    // Calculate progress percentage when totalPages > 0
    val progressPercentage = if (book.totalPages > 0) {
        ((currentPage.value.toFloat() / book.totalPages) * 100).toInt().coerceIn(0, 100)
    } else {
        book.progress // Use stored progress if totalPages is not set
    }

    fun getProgressColor(progress: Int): Color {
        return when {
            progress < 30 -> Color(0xFFF28D8D)
            progress in 30..60 -> Color(0xFFB79DFF)
            else -> Color(0xFF76C7A0)
        }
    }
    val progressColor = getProgressColor(progressPercentage)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { pressed = !pressed },
        elevation = if (pressed) CardDefaults.cardElevation(defaultElevation = 8.dp) else CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    val formattedDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(book.dateAdded.toLong()))
                    Text(text = book.title, style = MaterialTheme.typography.titleLarge)
                    Text(text = book.author, style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Genre: ${book.genre}", style = MaterialTheme.typography.bodySmall)
                    Text(text = "Added: $formattedDate", style = MaterialTheme.typography.bodySmall)
                    if (book.totalPages > 0) {
                        Text(text = "Total Pages: ${book.totalPages}", style = MaterialTheme.typography.bodySmall)
                    }
                }
                Row {
                    IconButton(onClick = onEmail) {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Email Book Info"
                        )
                    }
                    IconButton(onClick = onEdit) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Book"
                        )
                    }
                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Book"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Show progress based on percentage
            Text(
                text = "Progress: ${progressPercentage}%",
                color = progressColor
            )

            if (book.totalPages > 0) {
                // Page counter with slider
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Pages read:",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Text(
                            text = "${currentPage.value} / ${book.totalPages}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = progressColor
                        )
                    }

                    Slider(
                        value = sliderPosition,
                        onValueChange = {
                            sliderPosition = it
                        },
                        onValueChangeFinished = {
                            val newPosition = sliderPosition.toInt()
                            // Only update if position has actually changed
                            if (newPosition != currentPage.value) {
                                // Calculate percentage and update
                                val newProgress = ((sliderPosition / book.totalPages) * 100).toInt().coerceIn(0, 100)
                                // Use the updateBookCurrentPage to ensure both current page and progress are updated
                                onProgressChange(newProgress)
                            }
                        },
                        valueRange = 0f..book.totalPages.toFloat(),
                        steps = if (book.totalPages > 100) 0 else book.totalPages - 1,
                        modifier = Modifier.fillMaxWidth(),
                        colors = SliderDefaults.colors(
                            thumbColor = progressColor,
                            activeTrackColor = progressColor.copy(alpha = 0.6f),
                            inactiveTrackColor = progressColor.copy(alpha = 0.2f)
                        )
                    )
                }
            } else {
                // Standard percentage slider for books without page count
                var sliderValue by remember(book.progress) { mutableStateOf(book.progress.toFloat()) }

                Slider(
                    value = sliderValue,
                    onValueChange = { newValue -> sliderValue = newValue },
                    onValueChangeFinished = { onProgressChange(sliderValue.toInt()) },
                    valueRange = 0f..100f,
                    modifier = Modifier.fillMaxWidth(),
                    colors = SliderDefaults.colors(
                        thumbColor = progressColor,
                        activeTrackColor = progressColor.copy(alpha = 0.6f),
                        inactiveTrackColor = progressColor.copy(alpha = 0.2f)
                    )
                )
            }
        }
    }
}