package com.example.personalbookmanagementsystem.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey val title: String, // Book title
    val author: String, // Author of the book
    val genre: String?, // Genre of the book (optional)
    val dateAdded: String, // Date the book was added
    val progress: Int // Reading progress in percentage
)