package com.library.kindle.controllers

import com.library.kindle.data.AuthorDTO
import com.library.kindle.data.BookDTO
import com.library.kindle.service.AuthorService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/author")
class AuthorController {
    @Autowired
    private lateinit var authorService: AuthorService

    @GetMapping(
        value = ["/"]
    )
    fun getAllAuthors(
        @RequestParam(name = "page", defaultValue = "1") page:Int,
        @RequestParam(name = "size", defaultValue = "4") size: Int
    ) = authorService.getAllAuthors(page,size)

    @PostMapping(
        value=["/insert"]
    )

    fun insertAuthor(
        @RequestBody author: AuthorDTO
    ) = authorService.insertAuthor(author)


    @GetMapping(
        value = ["/searchByName"]
    )

    fun searchAuthor(
        @RequestParam(name = "author_name") author_name: String,
        @RequestParam(name = "page", defaultValue = "1") page: Int,
        @RequestParam(name = "size", defaultValue = "4") size: Int,
    ): ResponseEntity<Map<String, Any>>? = authorService.findByAuthor(author_name, page, size)

}