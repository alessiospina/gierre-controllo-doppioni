package com.gierre.gierrecontrolloduplicati.controller

import com.gierre.gierrecontrolloduplicati.dto.ErrorDetailsDto
import com.gierre.gierrecontrolloduplicati.dto.ServerResponseDto
import com.gierre.gierrecontrolloduplicati.exception.BadRequestException
import com.gierre.gierrecontrolloduplicati.exception.FileExcelException
import com.gierre.gierrecontrolloduplicati.exception.InternalServerException
import com.gierre.gierrecontrolloduplicati.exception.UnathorizedException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.LocalDateTime
import java.util.logging.Logger


@ControllerAdvice
class ControllerDuplicatiControllerAdvice {

    companion object {
        @JvmStatic private val logger = Logger.getLogger(ControllerDuplicatiControllerAdvice::class.java.name)
    }


    @ExceptionHandler(FileExcelException::class)
    fun exceptionHandler(e: FileExcelException): ResponseEntity<ServerResponseDto<ErrorDetailsDto>> {

        logger.severe(e.printStackTrace().toString())

        val error = ErrorDetailsDto(
            LocalDateTime.now(),
            "FileExcelException",
            e.localizedMessage
        )

        val responseError: ServerResponseDto<ErrorDetailsDto> = ServerResponseDto.badRequest(error)
        return ResponseEntity.badRequest().body(responseError);
    }



    @ExceptionHandler(UnathorizedException::class)
    fun exceptionUnathorizedExceptionHandler(e: UnathorizedException): ResponseEntity<ServerResponseDto<UnathorizedException>> {
        logger.severe(e.printStackTrace().toString())
        val responseError: ServerResponseDto<UnathorizedException> = ServerResponseDto.error(e)
        return ResponseEntity.badRequest().body(responseError);
    }



    @ExceptionHandler(BadRequestException::class)
    fun exceptionBadRequestExceptionHandler(e: BadRequestException): ResponseEntity<ServerResponseDto<BadRequestException>> {
        logger.severe(e.printStackTrace().toString())
        val responseError: ServerResponseDto<BadRequestException> = ServerResponseDto.error(e)
        return ResponseEntity.badRequest().body(responseError);
    }



    @ExceptionHandler(InternalServerException::class)
    fun exceptionInternalServerErrorHandler(e: InternalServerException): ResponseEntity<ServerResponseDto<InternalServerException>> {
        logger.severe(e.printStackTrace().toString())
        val responseError: ServerResponseDto<InternalServerException> = ServerResponseDto.error(e)
        return ResponseEntity.badRequest().body(responseError);
    }


    @ExceptionHandler(Exception::class)
    fun exceptionHandler(e: Exception): ResponseEntity<ServerResponseDto<ErrorDetailsDto>> {

        logger.severe(e.printStackTrace().toString())

        val error = ErrorDetailsDto(
            LocalDateTime.now(),
            "Generic Error",
            "Error encountered during the execution of the program."
        )
        val responseError: ServerResponseDto<ErrorDetailsDto> = ServerResponseDto.error(error)
        return ResponseEntity.internalServerError().body(responseError);
    }
}