package com.library.kindle.service

import com.library.kindle.converters.AuthorMapper
import com.library.kindle.data.Book
import com.library.kindle.data.BookDTO
import com.library.kindle.converters.BookMapper
import com.library.kindle.data.Author
import com.library.kindle.data.AuthorDTO
import com.library.kindle.repository.AuthorRepository
import com.library.kindle.repository.BookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@Service("Book service")
class BookService() {

    @Autowired
    lateinit var repository: BookRepository

    @Autowired
    lateinit var bookMapper: BookMapper

    @Autowired
    lateinit var authorRepository: AuthorRepository

    @Autowired
    lateinit var authorService: AuthorService

    @Autowired
    lateinit var authorMapper: AuthorMapper

    fun getBook(page: Int, size: Int): ResponseEntity<Map<String,Any>> {
        val bookDtolist: MutableList<BookDTO> = mutableListOf()
        val bookList = repository.findAll()
        for(book: Book in bookList){
            bookDtolist.add(bookMapper.toDTO(book))
        }
        var response: MutableMap<String,Any> = HashMap()
        if(bookDtolist.size > 0){
            var paging: Pageable = PageRequest.of(page-1,size)
            val start: Int = paging.offset.toInt()
            val end: Int = Math.min(start + paging.pageSize , bookDtolist.size)
            var pageTuts: Page<BookDTO> = PageImpl<BookDTO>(bookDtolist.subList(start,end), paging, bookDtolist.size.toLong())
            response["books"] = pageTuts.content
            response["total Pages"] = pageTuts.totalPages
            response["total Element"] = pageTuts.totalElements
            return ResponseEntity(response,HttpStatus.OK)
        }
        response["message"] = "No book found"
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }


    fun insertBook(name: String, t: String, id: Int, page: Int, cat: ArrayList<String>, con: ByteArray): ResponseEntity<Map<String, Any>> {
        val book = Book()
        book.apply {
            isbn = UUID.randomUUID().toString()
            title = t
            pages = page.toLong()
            val exist: Boolean = authorRepository.existsById(id)
            val authordto: AuthorDTO = AuthorDTO()
            authordto.id = id
            authordto.author_name = name
            if (!exist)
            {
                var temp: ResponseEntity<Map<String,Any>> = authorService.insertAuthor(authordto)
            }
            author = authorMapper.toEntity(authordto)
            created = created
            category = cat
            content = con
        }
        repository.save(book)
        var response: MutableMap<String,Any> = mutableMapOf()
        response["message"] = "Book Inserted Successfully"
        return ResponseEntity(response, HttpStatus.OK)
    }


    fun deleteBook(id: String) : ResponseEntity<Map<String,Any>> {
        var response: MutableMap<String,Any> = mutableMapOf()

        val exist: Boolean = repository.existsById(id)
        if(exist){
            repository.deleteById(id)
            response["message"] = "Book Deleted Successfully"
            return ResponseEntity(response,HttpStatus.OK)
        }
        response["message"] = "Book Does Not Exist"
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)

    }

    fun findByTitle(title: String): ResponseEntity<Map<String,Any>>{
        val bookDtolist: MutableList<BookDTO> = mutableListOf()
        val bookList = repository.findByTitle(title)
        for(book: Book in bookList){
            bookDtolist.add(bookMapper.toDTO(book))
        }

        var response: MutableMap<String,Any> = HashMap()
        if(bookDtolist.size > 0){
            var paging: Pageable = PageRequest.of(0, bookDtolist.size)
            var pagetuts: Page<BookDTO> = PageImpl<BookDTO>(bookDtolist.subList(0,bookDtolist.size), paging, bookDtolist.size.toLong())

            response["title"] = pagetuts.content
            return ResponseEntity(response,HttpStatus.OK)
        }

        response["message"] = "Book Does Not Exist"
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)

    }

    fun findByCategory(cat: String, page: Int, size: Int) : ResponseEntity<Map<String,Any>>{
        var bookDTOList: MutableList<BookDTO> = mutableListOf()
        var books:List<Book> = repository.findAll().filter { it.category.contains(cat) }
        for(b: Book in books){
            println(b.title)
            println(b.author.author_name)
            bookDTOList.add(bookMapper.toDTO(b))
        }
        var response: MutableMap<String,Any> = HashMap()
        if(bookDTOList.size > 0){
            var paging: Pageable = PageRequest.of(page-1, size)
            var start: Int = paging.offset.toInt()
            var end: Int = Math.min(start + paging.pageSize, bookDTOList.size)
            var filter: Page<BookDTO> = PageImpl<BookDTO>(bookDTOList.subList(start, end), paging, bookDTOList.size.toLong())
            response["filter"] = filter.content
            response["total Elements"] = filter.totalElements
            response["total Pages"] = filter.totalPages
            return ResponseEntity(response, HttpStatus.ACCEPTED)
        }
        response["message"] = "Try searching with some other category"
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }
}



