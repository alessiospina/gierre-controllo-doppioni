package com.gierre.gierrecontrolloduplicati.helper

import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

@Component
class ExcelHelper {

    fun <T: Any> read(file: MultipartFile): List<T> {
        return emptyList()
    }
}