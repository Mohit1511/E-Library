package com.library.kindle

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import springfox.documentation.swagger2.annotations.EnableSwagger2

@EnableSwagger2
@OpenAPIDefinition
@SpringBootApplication
class KindleApplication

fun main(args: Array<String>) {
	runApplication<KindleApplication>(*args)
}
