package com.tinuproject.tinu.web

import com.tinuproject.tinu.domain.exception.base.BaseErrorCode
import com.tinuproject.tinu.domain.exception.base.ResponseDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class ResponseEntityGenerator {
    companion object{
        fun onSuccess(result : Any?) : ResponseEntity<ResponseDTO> {
            val responseDTO = ResponseDTO(
                isSuccess = true,
                httpStatusCode = 200,
                result = result
            )

            return ResponseEntity.status(200).body(responseDTO)
        }

        fun onSuccess(result : Any?, httpStatus : Int) : ResponseEntity<ResponseDTO>{
            val responseDTO = ResponseDTO(
                isSuccess = true,
                httpStatusCode = httpStatus,
                result = result
            )

            return ResponseEntity.status(httpStatus).body(responseDTO)
        }

        fun onFailure(code : BaseErrorCode) : ResponseEntity<ResponseDTO>{
            val responseDTO = code.getResponse()
            return ResponseEntity.status(responseDTO!!.httpStatusCode!!).body(responseDTO)
        }
    }
}