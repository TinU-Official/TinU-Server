package com.tinuproject.tinu.swagger.config

import com.tinuproject.tinu.swagger.annotation.SwaggerExceptionResponses
import com.tinuproject.tinu.swagger.adaptor.SwaggerExceptionResponseAdaptor
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springdoc.core.customizers.OperationCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableAspectJAutoProxy
class SwaggerConfig(val swaggerExceptionResponseAdaptor: SwaggerExceptionResponseAdaptor) :WebMvcConfigurer{


    val jwt = "JWT"
    @Bean
    fun openAPI(): OpenAPI {
        val securityRequirement = SecurityRequirement().addList(jwt)
        val components = Components().addSecuritySchemes(
            jwt, jwtComponent()
        )
        val openAPI = OpenAPI()
            .components(Components())
            .info(apiInfo())
            .addSecurityItem(securityRequirement)
            .components(components)


        return openAPI
    }

    private fun jwtComponent() :SecurityScheme{
        return SecurityScheme()
            .name(jwt)
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
    }

    private fun apiInfo(): Info {
        return Info()
            .title("TinU API 명세서") // API의 제목
            .description("TinU API 명세서 종류입니다.") // API에 대한 설명
            .version("1.0.0") // API의 버전
    }

    @Bean
    fun customize(): OperationCustomizer {
        return OperationCustomizer { operation: Operation?, handlerMethod: HandlerMethod ->

            val swaggerExceptionResponses : SwaggerExceptionResponses? = handlerMethod.getMethodAnnotation(
                SwaggerExceptionResponses::class.java
            )


            if(swaggerExceptionResponses!=null){
                if(swaggerExceptionResponses.exceptions.size==1){
                    swaggerExceptionResponseAdaptor.generateErrorCodeResponseExample(operation!!, swaggerExceptionResponses.exceptions[0] )
                }else{
                    swaggerExceptionResponseAdaptor.generateErrorCodeResponseExample(operation!!, swaggerExceptionResponses.exceptions)
                }
            }

            operation
        }
    }
}