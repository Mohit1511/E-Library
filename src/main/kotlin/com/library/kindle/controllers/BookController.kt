package com.library.kindle.controllers

import com.library.kindle.data.BookDTO
import com.library.kindle.service.BookService
import io.swagger.annotations.ApiParam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.validation.Valid


@RestController
@RequestMapping("/books")
class BookController {

    @Autowired
    private lateinit var service: BookService


    @GetMapping(
        value=["/"]
    )
    fun getBook(
        @RequestParam(name = "page" , defaultValue = "1") page: Int,
        @RequestParam(name = "size", defaultValue = "4") size: Int
    ): ResponseEntity<Map<String, Any>> {
        return service.getBook(page, size)
    }


    @RequestMapping(
        method = [RequestMethod.POST],
        path = ["/insert"],
        consumes = ["multipart/form-data"]
    )

    @ResponseBody
    fun insertBook(
        @RequestParam(name = "Author Name") name: String,
        @RequestParam(name = "id") id: Int,
        @RequestParam(name = "No of Pages") pages: Int,
        @RequestParam(name = "Book Name") title: String,
        @RequestParam(name = "Genre") category: ArrayList<String>,
        @RequestParam(name = "Pdf") content: MultipartFile
    ) = service.insertBook(name, title, id, pages, category, content.bytes)


    @DeleteMapping(
        value = ["/{id}"]
    )

    fun deleteNote(
        @PathVariable(name = "id") id: String
    ) = service.deleteBook(id)


    @GetMapping(
        value = ["/searchByTitle/"]
    )
    fun searchBook(
        @RequestParam(name = "title") title: String,
    ) = service.findByTitle(title)

    @GetMapping(
        value = ["/category"]
    )
    fun searchCategory(
        @RequestParam(name = "category") category: String,
        @RequestParam(name = "page" , defaultValue = "1") page: Int,
        @RequestParam(name = "size", defaultValue = "4") size: Int
    ) = service.findByCategory(category, page, size)

}