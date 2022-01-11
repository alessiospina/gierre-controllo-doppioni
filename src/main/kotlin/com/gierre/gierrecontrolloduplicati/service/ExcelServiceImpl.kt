package com.gierre.gierrecontrolloduplicati.service

import com.gierre.gierrecontrolloduplicati.dto.ServerResponseDto
import com.gierre.gierrecontrolloduplicati.helper.ExcelHelper
import org.springframework.boot.configurationprocessor.json.JSONObject
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile


@Service
class ExcelServiceImpl: ExcelService {

    lateinit var excelHelper: ExcelHelper

    override fun findDoppioni(file: MultipartFile): ServerResponseDto<JSONObject> {
        TODO("Not yet implemented")
    }
}