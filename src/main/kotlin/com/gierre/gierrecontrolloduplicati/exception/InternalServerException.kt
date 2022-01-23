package com.gierre.gierrecontrolloduplicati.exception

import org.springframework.http.HttpStatus

class InternalServerException(
    override val entity: String,
    override val info: String,
    override val status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR
): HttpException(entity, info, status) {}