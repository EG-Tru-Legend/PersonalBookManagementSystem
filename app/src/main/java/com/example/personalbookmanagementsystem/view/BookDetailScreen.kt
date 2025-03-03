package com.example.personalbookmanagementsystem.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.personalbookmanagementsystem.model.Book
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(
    book: Book,
    onNavigateBack: () -> Unit,
    onEdit: (Book) -> Unit,
    onDelete: (Book) -> Unit,
    onEmail: (Book) -> Unit,
    onProgressChange: (Book, Int) -> Unit
) {
    val currentPage = rememberUpdatedState(book.currentPage)
    var sliderPosition by remember(book.currentPage) { mutableStateOf(book.currentPage.toFloat()) }

    val progressPercentage = if (book.totalPages > 0) {
        ((currentPage.value.toFloat() / book.totalPages) * 100).toInt().coerceIn(0, 100)
    } else {
        book.progress
    }

    fun getProgressColor(progress: Int): Color {
        return when {
            progress < 30 -> Color(0xFFF28D8D)
            progress in 30..60 -> Color(0xFFB79DFF)
            else -> Color(0xFF76C7A0)
        }
    }
    val progressColor = getProgressColor(progressPercentage)

    BackHandler(onBack = onNavigateBack)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Book Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { onEmail(book) }) {
                        Icon(Icons.Default.Email, contentDescription = "Email Book")
                    }
                    IconButton(onClick = { onEdit(book) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Book")
                    }
                    IconButton(onClick = { onDelete(book) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete Book")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                text = book.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "By ${book.author}",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surfaceVariant,
                tonalElevation = 1.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    val formattedDate = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                        .format(Date(book.dateAdded.toLong()))

                    book.genre?.let { DetailRow("Genre", it) }
                    DetailRow("Date Added", formattedDate)
                    if (book.totalPages > 0) {
                        DetailRow("Total Pages", book.totalPages.toString())
                    }
                    DetailRow("Current Progress", "$progressPercentage%")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Reading Progress",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            LinearProgressIndicator(
                progress = progressPercentage / 100f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp),
                color = progressColor,
                trackColor = progressColor.copy(alpha = 0.2f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (book.totalPages > 0) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Pages read:",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Text(
                        text = "${currentPage.value} / ${book.totalPages}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = progressColor,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Slider(
                    value = sliderPosition,
                    onValueChange = { sliderPosition = it },
                    onValueChangeFinished = {
                        val newPosition = sliderPosition.toInt()
                        if (newPosition != currentPage.value) {
                            val newProgress = ((sliderPosition / book.totalPages) * 100).toInt().coerceIn(0, 100)
                            onProgressChange(book, newProgress)
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
            } else {
                var sliderValue by remember(book.progress) { mutableStateOf(book.progress.toFloat()) }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Update your progress:",
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                Slider(
                    value = sliderValue,
                    onValueChange = { newValue -> sliderValue = newValue },
                    onValueChangeFinished = { onProgressChange(book, sliderValue.toInt()) },
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

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun BackHandler(onBack: () -> Unit) {
    androidx.activity.compose.BackHandler {
        onBack()
    }
}