package com.example.personalbookmanagementsystem

import androidx.room.*

@Dao
interface BookDao {
    @Query("SELECT * FROM books")
    suspend fun getAllBooks(): List<Book>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: Book)

    @Query("DELETE FROM books WHERE title = :title")
    suspend fun deleteBookByTitle(title: String)
}
