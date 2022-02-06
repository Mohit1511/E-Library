package com.library.kindle.repository

import com.library.kindle.data.Author
import com.library.kindle.data.Book
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param

interface BookRepository: JpaRepository<Book, String> {
    fun findByTitle(title: String): Iterable<Book>
    fun findAllByAuthor(author: Author) : List<Book>

}