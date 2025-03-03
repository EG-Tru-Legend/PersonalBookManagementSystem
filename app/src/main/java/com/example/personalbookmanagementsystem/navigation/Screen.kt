package com.example.personalbookmanagementsystem.navigation

import com.example.personalbookmanagementsystem.model.Book

sealed class Screen {
    object BookList : Screen()
    object AddBook : Screen()
    data class BookDetail(val book: Book) : Screen()
}