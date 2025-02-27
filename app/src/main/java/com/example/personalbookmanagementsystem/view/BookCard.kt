package com.example.personalbookmanagementsystem.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
    onProgressChange: (Int) -> Unit
) {
    var pressed by remember { mutableStateOf(false) }

    fun getProgressColor(progress: Int): Color {
        return when {
            progress < 30 -> Color(0xFFF28D8D)
            progress in 30..60 -> Color(0xFFB79DFF)
            else -> Color(0xFF76C7A0)
        }
    }
    val progressColor = getProgressColor(book.progress)

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
                }
                Row {
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
            var sliderValue by remember { mutableStateOf(book.progress.toFloat()) }
            Text(
                text = "Progress: ${sliderValue.toInt()}%",
                color = progressColor
            )
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

