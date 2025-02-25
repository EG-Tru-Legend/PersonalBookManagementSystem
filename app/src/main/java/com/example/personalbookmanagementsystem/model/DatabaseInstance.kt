package com.example.personalbookmanagementsystem.model

import android.content.Context
import androidx.room.Room

object DatabaseInstance {
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "book-database"
            ).build()
        }
        return INSTANCE!!
    }
}
