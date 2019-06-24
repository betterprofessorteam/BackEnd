package com.israel.betterprofessor.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Pageable
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket

// http://localhost:8080/swagger-ui.html
@Configuration
open class Swagger2Config {
    @Bean
    open fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false) // Allows only my exception responses
                .ignoredParameterTypes(Pageable::class.java) // allows only my paging parameter list
                .apiInfo(apiEndPointsInfo())
    }

    private fun apiEndPointsInfo(): ApiInfo {
        return ApiInfoBuilder().title("Better Professor App")
                .description("A starting application for developing Java Spring Back End Projects")
                .contact(Contact("Israel dela Cruz", "http://www.lambdaschool.com", "igdcruz42@gmail.com"))
                .license("MIT").licenseUrl("https://github.com/LambdaSchool/java-starthere/blob/master/LICENSE")
                .version("1.0.0").build()
    }
}
