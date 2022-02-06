package com.library.kindle.service

import com.library.kindle.data.AuthorDTO
import com.library.kindle.data.Book
import com.library.kindle.converters.AuthorMapper
import com.library.kindle.converters.BookMapper
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


@Service
class AuthorService(){
    @Autowired(required = true)
    private lateinit var authorRepository: AuthorRepository

    @Autowired(required = true)
    private lateinit var bookRepository: BookRepository

    @Autowired(required = true)
    private lateinit var authorMapper: AuthorMapper

    @Autowired(required = true)
    private lateinit var bookMapper: BookMapper

    fun getAllAuthors(page: Int, size: Int) : ResponseEntity<Map<String, Any>>{
        val authordtolist = mutableListOf<AuthorDTO>()
        authorRepository.findAll().forEach(){
            authordtolist.add(authorMapper.toDTO(it))
        }
        var response: MutableMap<String, Any> = HashMap()
        if(authordtolist.size > 0){
            var paging: Pageable = PageRequest.of(page-1, size)
            val start: Int = paging.offset.toInt()
            val end: Int = Math.min(start + paging.pageSize, authordtolist.size)
            var pageTuts: Page<AuthorDTO> = PageImpl<AuthorDTO>(authordtolist.subList(start, end), paging, authordtolist.size.toLong())
            response["authors"] = pageTuts.content
            response["total Pages"] = pageTuts.totalPages
            response["total Elements"] = pageTuts.totalElements
            return ResponseEntity(response,HttpStatus.OK)
        }
        response["message"] = "No Author Exist"
        return ResponseEntity(response,HttpStatus.BAD_REQUEST)

    }

    fun insertAuthor(authorDTO: AuthorDTO): ResponseEntity<Map<String,Any>> {
        var response: MutableMap<String,Any> = mutableMapOf()
        val author = authorMapper.toEntity(authorDTO)
        authorRepository.save(author)
        response["message"] = "Author inserted successfully"
        return ResponseEntity(response,HttpStatus.OK)
    }

    fun findByAuthor(author_name: String, page: Int, size: Int): ResponseEntity<Map<String, Any>>? {
        var response: MutableMap<String, Any> = HashMap()
        return try {
            var authorDtoList: MutableList<String> = mutableListOf()
            var paging: Pageable = PageRequest.of(page - 1, size)
            var author = authorRepository.findAuthor(author_name)
            var books = bookRepository.findAllByAuthor(author)
            for(book: Book in books){
                authorDtoList.add(book.title)
            }
            val start: Int = paging.getOffset().toInt()
            val end: Int = Math.min(start + paging.getPageSize(), authorDtoList.size)
            val page: Page<String> = PageImpl<String>(authorDtoList.subList(start,end), paging, authorDtoList.size.toLong())
            response["books"] = page.content
            response["totalItems"] = page.totalElements
            response["totalPages"] = page.totalPages
            ResponseEntity(response, HttpStatus.OK)
        } catch (e: Exception) {
            response["message"] = "Author Does Not Exist"
            ResponseEntity(response, HttpStatus.BAD_REQUEST)
        }
    }

}