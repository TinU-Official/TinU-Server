package com.tinuproject.tinu.swagger.adaptor

import com.tinuproject.tinu.DTO.ErrorResponse
import com.tinuproject.tinu.DTO.ResponseDTO
import com.tinuproject.tinu.domain.exception.base.BaseException
import com.tinuproject.tinu.swagger.annotation.ExampleHolder
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.examples.Example
import io.swagger.v3.oas.models.media.Content
import io.swagger.v3.oas.models.media.MediaType
import io.swagger.v3.oas.models.responses.ApiResponse
import io.swagger.v3.oas.models.responses.ApiResponses
import org.springframework.stereotype.Component
import java.util.function.Consumer
import kotlin.reflect.KClass
import java.lang.String

@Component
class SwaggerExceptionResponseAdaptor {


    fun generateErrorCodeResponseExample(operation : Operation, exceptions: Array<KClass<out BaseException>>){
        val responses: ApiResponses = operation.responses

        val statusWithExampleHolders: Map<Int, MutableList<ExampleHolder>> = exceptions
            .map { exception ->
                val ex = exception.constructors.first().call().getResponse()
                ExampleHolder(
                    holder = getSwaggerExample(ex),
                    code = ex.stateCode,
                    name = ex.result!!.statusCode
                )
            }
            .groupBy { it.code }.mapValues { it.value.toMutableList() }


        // ExampleHolders를 ApiResponses에 추가
        addExamplesToResponses(responses, statusWithExampleHolders)
    }

    fun generateErrorCodeResponseExample(operation : Operation, exception: KClass<out BaseException>){
        val responses: ApiResponses = operation.responses

        val ex = exception.constructors.first().call().getResponse()
        // ExampleHolder 객체 생성 및 ApiResponses에 추가
        val exampleHolder: ExampleHolder = ExampleHolder(
            holder = getSwaggerExample(ex),
            code = ex.stateCode,
            name = ex.result!!.statusCode
        )

        addExamplesToResponses(responses, exampleHolder)
    }


    // ErrorResponseDto 형태의 예시 객체 생성
    private fun getSwaggerExample(errorResponseDto: ResponseDTO<ErrorResponse>): Example {
        val example = Example()
        example.value = errorResponseDto

        return example
    }

    // exampleHolder를 ApiResponses에 추가
    private fun addExamplesToResponses(
        responses: ApiResponses,
        statusWithExampleHolders: Map<Int, MutableList<ExampleHolder>>
    ) {
        statusWithExampleHolders.forEach { (status: Int?, v: MutableList<ExampleHolder>?) ->
            val content: Content = Content()
            val mediaType: MediaType = MediaType()
            val apiResponse: ApiResponse = ApiResponse()

            v.forEach(
                Consumer<ExampleHolder> { exampleHolder: ExampleHolder ->
                    mediaType.addExamples(
                        exampleHolder.name,
                        exampleHolder.holder
                    )
                }
            )
            content.addMediaType("application/json", mediaType)
            apiResponse.setContent(content)
            responses.addApiResponse(status.toString(), apiResponse)
        }
    }

    private fun addExamplesToResponses(responses: ApiResponses, exampleHolder: ExampleHolder) {
        val content: Content = Content()
        val mediaType: MediaType = MediaType()
        val apiResponse: ApiResponse = ApiResponse()

        mediaType.addExamples(exampleHolder.name, exampleHolder.holder)
        content.addMediaType("application/json", mediaType)
        apiResponse.content(content)
        responses.addApiResponse(String.valueOf(exampleHolder.code), apiResponse)
    }
}