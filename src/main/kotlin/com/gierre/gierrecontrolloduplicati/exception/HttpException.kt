package com.gierre.gierrecontrolloduplicati.exception

import org.springframework.http.HttpStatus

open class HttpException(
    open val entity: String,
    open val info: String,
    open val status: HttpStatus
): Exception("HttpException(entity: $entity, message: $info, status: $status)") {}