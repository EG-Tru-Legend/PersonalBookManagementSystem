package com.example.personalbookmanagementsystem.utils

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.example.personalbookmanagementsystem.model.Book
import java.text.SimpleDateFormat
import java.util.*

object EmailUtils {

    /**
     * Send an email with details about a specific book
     */
    fun sendBookDetailsEmail(context: Context, recipient: String, book: Book) {
        val subject = "Book Details: ${book.title}"

        val formattedDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            .format(Date(book.dateAdded.toLong()))

        val progressPercentage = if (book.totalPages > 0) {
            ((book.currentPage.toFloat() / book.totalPages) * 100).toInt().coerceIn(0, 100)
        } else {
            book.progress
        }

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
            |Sent from Personal Book Management System
        """.trimMargin()

        sendEmail(context, recipient, subject, body)
    }

    /**
     * Send an email with a summary of multiple books
     */
    fun sendBookListEmail(context: Context, recipient: String, books: List<Book>) {
        val subject = "My Book List Summary"

        val totalBooks = books.size
        val completedBooks = books.count { it.progress == 100 }

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

        bodyBuilder.append("\n\nSent from Personal Book Management System")

        sendEmail(context, recipient, subject, bodyBuilder.toString())
    }

    /**
     * Generic method to send an email using Android intent system
     */
    private fun sendEmail(context: Context, recipient: String, subject: String, body: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
        }

        try {
            startActivity(context, Intent.createChooser(intent, "Send email using..."), null)
        } catch (e: Exception) {
            // Handle exception (could show a Toast or Snackbar here)
            e.printStackTrace()
        }
    }
}