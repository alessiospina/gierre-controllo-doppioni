package com.gierre.gierrecontrolloduplicati.exception

import org.springframework.http.HttpStatus

data class BadRequestException(
    override val entity: String,
    override val info: String,
    override val status: HttpStatus = HttpStatus.UNAUTHORIZED
): HttpException(entity, info, status) {}