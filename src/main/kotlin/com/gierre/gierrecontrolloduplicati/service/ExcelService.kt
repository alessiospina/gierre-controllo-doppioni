package com.gierre.gierrecontrolloduplicati.service

import com.gierre.gierrecontrolloduplicati.dto.ExcelRowDto
import com.gierre.gierrecontrolloduplicati.dto.ServerResponseDto
import org.springframework.boot.configurationprocessor.json.JSONArray
import org.springframework.boot.configurationprocessor.json.JSONObject
import org.springframework.web.multipart.MultipartFile

interface ExcelService {
    fun findDoppioni(file1: MultipartFile, file2: MultipartFile): ServerResponseDto<List<ExcelRowDto>>
}