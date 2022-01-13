package com.gierre.gierrecontrolloduplicati.service

import com.gierre.gierrecontrolloduplicati.dto.ExcelRowDto
import com.gierre.gierrecontrolloduplicati.dto.ServerResponseDto
import org.springframework.core.io.InputStreamResource
import org.springframework.web.multipart.MultipartFile
import javax.annotation.Resource

interface ExcelService {
    fun findDoppioni(file1: MultipartFile, file2: MultipartFile): ServerResponseDto<List<ExcelRowDto>>
    fun findDoppioniExcel(file1: MultipartFile, file2: MultipartFile): ByteArray
}