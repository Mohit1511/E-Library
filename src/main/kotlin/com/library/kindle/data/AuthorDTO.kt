package com.library.kindle.data


data class AuthorDTO(
    var id: Int,
    var author_name: String,

    var book: List<BookDTO> = mutableListOf<BookDTO>()
) {
    constructor() : this(0, "")
}