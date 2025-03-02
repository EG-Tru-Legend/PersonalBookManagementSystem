package com.example.personalbookmanagementsystem.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Book::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
}
