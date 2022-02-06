
package com.library.kindle.converters.impl

import com.library.kindle.converters.BookMapper
import com.library.kindle.converters.AuthorMapper
import com.library.kindle.data.Book
import com.library.kindle.data.BookDTO
import com.library.kindle.repository.AuthorRepository
import com.library.kindle.service.AuthorService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.lang.Math.random
import java.util.*
import kotlin.collections.ArrayList



@Component
class BookMapperImpl(
    val authorMapper: AuthorMapper,
) : BookMapper {

    @Autowired(required = true)
    lateinit var repository:AuthorRepository

    @Autowired(required = true)
    lateinit var service: AuthorService

    override fun toDTO(book: Book): BookDTO {
        if (book == null)
            return book

        val bookDto = BookDTO()
        bookDto.apply {
            isbn = book.isbn
            title = book.title
            pages = book.pages
            author = authorMapper.toDTO(book.author)
            created = book.created
            category = book.category
            content = book.content
        }
        return bookDto
    }

    override fun toEntity(bookDto: BookDTO, cat: ArrayList<String>, con: ByteArray): Book {
        val book = Book()
        book.apply {
            isbn = UUID.randomUUID().toString()
            title = bookDto.title
            pages = bookDto.pages
            val exist: Boolean = repository.existsById(bookDto.author.id)

            if (!exist)
            {
                var temp: ResponseEntity<Map<String,Any>> = service.insertAuthor(bookDto.author)
            }
            author = bookDto.author.let { authorMapper.toEntity(it)}!!
            created = bookDto.created
            category = cat
            content = con
        }
        return book
    }
}