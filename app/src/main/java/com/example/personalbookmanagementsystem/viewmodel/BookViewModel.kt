package com.example.personalbookmanagementsystem.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalbookmanagementsystem.model.Book
import com.example.personalbookmanagementsystem.model.BookDao
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class BookViewModel(private val bookDao: BookDao) : ViewModel() {

    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books: StateFlow<List<Book>> = _books.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedGenre = MutableStateFlow("All")
    val selectedGenre: StateFlow<String> = _selectedGenre.asStateFlow()

    private val _isSorted = MutableStateFlow(false)
    val isSorted: StateFlow<Boolean> = _isSorted.asStateFlow()

    val filteredBooks: StateFlow<List<Book>> = combine(
        _books, _searchQuery, _selectedGenre, _isSorted
    ) { books, query, genre, isSorted ->
        var filtered = books.filter { book ->
            (book.title.contains(query, ignoreCase = true) ||
                    book.author.contains(query, ignoreCase = true)) &&
                    (genre == "All" || book.genre.equals(genre, ignoreCase = true))
        }
        if (isSorted) {
            filtered = filtered.sortedBy { it.title }
        }
        filtered
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        refreshBooks()
    }

    fun refreshBooks() {
        viewModelScope.launch {
            _books.value = bookDao.getAllBooks()
        }
    }

    fun addBook(book: Book) {
        viewModelScope.launch {
            bookDao.insertBook(book)
            refreshBooks()
        }
    }

    fun updateBook(book: Book) {
        viewModelScope.launch {
            bookDao.updateBook(book)
            refreshBooks()
        }
    }

    fun deleteBook(book: Book) {
        viewModelScope.launch {
            bookDao.deleteBook(book)
            refreshBooks()
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedGenre(genre: String) {
        _selectedGenre.value = genre
    }

    fun toggleSort() {
        _isSorted.value = !_isSorted.value
    }
}
