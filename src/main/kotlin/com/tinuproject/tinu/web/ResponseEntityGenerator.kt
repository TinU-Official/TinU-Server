package com.tinuproject.tinu.web

import com.tinuproject.tinu.DTO.ResponseDTO
import com.tinuproject.tinu.domain.exception.base.BaseErrorCode
import org.springframework.http.ResponseEntity

class ResponseEntityGenerator {
    companion object{

        fun onSuccess() : ResponseEntity<ResponseDTO>{
            val responseDTO = ResponseDTO(
                isSuccess = true,
                stateCode = 200,
                result = null
            )

            return ResponseEntity.status(200).body(responseDTO)
        }
        fun onSuccess(result : Any?) : ResponseEntity<ResponseDTO> {
            val responseDTO = ResponseDTO(
                isSuccess = true,
                stateCode = 200,
                result = result
            )

            return ResponseEntity.status(200).body(responseDTO)
        }

        fun onSuccess(result : Any?, httpStatus : Int) : ResponseEntity<ResponseDTO>{
            val responseDTO = ResponseDTO(
                isSuccess = true,
                stateCode = httpStatus,
                result = result
            )

            return ResponseEntity.status(httpStatus).body(responseDTO)
        }

        fun onFailure(code : BaseErrorCode) : ResponseEntity<ResponseDTO>{
            val responseDTO = code.getResponse()
            return ResponseEntity.status(responseDTO!!.stateCode).body(responseDTO)
        }
    }
}