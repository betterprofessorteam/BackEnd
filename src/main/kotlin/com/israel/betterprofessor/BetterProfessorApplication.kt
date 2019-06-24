package com.israel.betterprofessor

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import springfox.documentation.swagger2.annotations.EnableSwagger2

@SpringBootApplication
@EnableScheduling
@EnableSwagger2
class BetterProfessorApplication

fun main(args: Array<String>) {
    runApplication<BetterProfessorApplication>(*args)
}
