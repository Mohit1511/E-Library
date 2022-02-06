package com.library.kindle.data

import org.hibernate.annotations.CreationTimestamp
import java.util.*
import javax.persistence.Lob


class BookDTO {

    var isbn: String = ""
    var title: String = ""

    var pages: Long = 0

    var author: AuthorDTO = AuthorDTO()
    @Lob
    var content: ByteArray = byteArrayOf()
    var category: ArrayList<String> = arrayListOf()
    var created: Date = Date()
}