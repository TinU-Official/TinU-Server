package com.tinuproject.tinu.swagger.annotation

import com.tinuproject.tinu.domain.exception.base.BaseException
import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class SwaggerExceptionResponses(
    val exceptions: Array<KClass<out BaseException>> = []
){

}
