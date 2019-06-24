package com.israel.betterprofessor

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.servlet.DispatcherServlet
import springfox.documentation.swagger2.annotations.EnableSwagger2

@SpringBootApplication
@EnableScheduling
@EnableSwagger2
class BetterProfessorApplication

fun main(args: Array<String>) {
    val ctx = runApplication<BetterProfessorApplication>(*args)

    val dispatcherServlet = ctx.getBean("dispatcherServlet") as DispatcherServlet
    dispatcherServlet.setThrowExceptionIfNoHandlerFound(true)
}
