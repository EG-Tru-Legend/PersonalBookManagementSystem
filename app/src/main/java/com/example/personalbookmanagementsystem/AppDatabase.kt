package com.example.personalbookmanagementsystem

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.personalbookmanagementsystem.Book
import com.example.personalbookmanagementsystem.BookDao

@Database(entities = [Book::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
}
