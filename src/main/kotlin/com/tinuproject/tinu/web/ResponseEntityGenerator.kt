package com.tinuproject.tinu.web

import com.tinuproject.tinu.DTO.ErrorResponse
import com.tinuproject.tinu.DTO.ResponseDTO
import com.tinuproject.tinu.domain.exception.base.BaseErrorCode
import org.springframework.http.ResponseEntity

class ResponseEntityGenerator {
    companion object{

        fun <T> onSuccess() : ResponseEntity<ResponseDTO<T?>>{
            val responseDTO = ResponseDTO<T?>(
                isSuccess = true,
                stateCode = 200,
                result = null
            )

            return ResponseEntity.status(200).body(responseDTO)
        }

        fun <T> onSuccess(result : T?) : ResponseEntity<ResponseDTO<T?>> {
            val responseDTO = ResponseDTO<T?>(
                isSuccess = true,
                stateCode = 200,
                result = result
            )

            return ResponseEntity.status(200).body(responseDTO)
        }

        fun <T> onSuccess(result : T?, httpStatus : Int) : ResponseEntity<ResponseDTO<T?>>{
            val responseDTO = ResponseDTO<T?>(
                isSuccess = true,
                stateCode = httpStatus,
                result = result
            )

            return ResponseEntity.status(httpStatus).body(responseDTO)
        }

        fun onFailure(code : BaseErrorCode) : ResponseEntity<ResponseDTO<ErrorResponse>>{
            val responseDTO = code.getResponse()
            return ResponseEntity.status(responseDTO!!.stateCode).body(responseDTO)
        }
    }
}