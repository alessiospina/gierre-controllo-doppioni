package com.gierre.gierrecontrolloduplicati.service

import com.gierre.gierrecontrolloduplicati.dto.ServerResponseDto
import org.springframework.boot.configurationprocessor.json.JSONObject
import org.springframework.web.multipart.MultipartFile

interface ExcelService {
    fun findDoppioni(file: MultipartFile): ServerResponseDto<JSONObject>
}