package com.example.personalbookmanagementsystem.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val author: String,
    val genre: String?,
    val dateAdded: String,
    val progress: Int,
    val totalPages: Int = 0,
    val currentPage: Int = 0
)