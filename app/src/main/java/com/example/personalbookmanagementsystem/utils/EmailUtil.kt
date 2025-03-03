package com.example.personalbookmanagementsystem.utils

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.example.personalbookmanagementsystem.model.Book
import java.text.SimpleDateFormat
import java.util.*

object EmailUtils {
    // Function to share the details of a single book via email
    fun shareBookDetails(context: Context, book: Book) {
        val formattedDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            .format(Date(book.dateAdded.toLong()))

        val progressPercentage = if (book.totalPages > 0) {
            ((book.currentPage.toFloat() / book.totalPages) * 100).toInt().coerceIn(0, 100)
        } else {
            book.progress
        }

        // Subject and body of the email
        val subject = "Book Details: ${book.title}"
        val body = """
            |Book Details:
            |
            |Title: ${book.title}
            |Author: ${book.author}
            |Genre: ${book.genre}
            |Date Added: $formattedDate
            |${if (book.totalPages > 0) "Total Pages: ${book.totalPages}" else ""}
            |${if (book.totalPages > 0) "Current Page: ${book.currentPage}" else ""}
            |Progress: $progressPercentage%
            |
            |Shared from Personal Book Management System
        """.trimMargin()

        shareText(context, subject, body)
    }

    // Function to share a summary of all books via email
    fun shareBookList(context: Context, books: List<Book>) {
        val totalBooks = books.size
        val completedBooks = books.count { it.progress == 100 }

        val subject = "My Book List Summary"
        val bodyBuilder = StringBuilder()
        bodyBuilder.append("Book List Summary:\n\n")
        bodyBuilder.append("Total Books: $totalBooks\n")
        bodyBuilder.append("Completed Books: $completedBooks\n\n")
        bodyBuilder.append("Book List:\n\n")

        books.forEachIndexed { index, book ->
            val progressPercentage = if (book.totalPages > 0) {
                ((book.currentPage.toFloat() / book.totalPages) * 100).toInt().coerceIn(0, 100)
            } else {
                book.progress
            }

            bodyBuilder.append("${index + 1}. ${book.title}\n")
            bodyBuilder.append("   Author: ${book.author}\n")
            bodyBuilder.append("   Genre: ${book.genre}\n")
            bodyBuilder.append("   Progress: $progressPercentage%\n")
            if (index < books.size - 1) {
                bodyBuilder.append("\n")
            }
        }

        bodyBuilder.append("\n\nShared from Personal Book Management System")

        shareText(context, subject, bodyBuilder.toString())
    }

    // Helper function to send the text content via email
    private fun shareText(context: Context, subject: String, text: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, text)
        }

        try {
            startActivity(
                context,
                Intent.createChooser(shareIntent, "Share via"),
                null
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}