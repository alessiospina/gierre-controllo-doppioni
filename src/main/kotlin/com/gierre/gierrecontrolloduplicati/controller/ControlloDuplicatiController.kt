package com.gierre.gierrecontrolloduplicati.controller

import com.gierre.gierrecontrolloduplicati.dto.ServerResponseDto
import com.gierre.gierrecontrolloduplicati.service.ExcelService
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.configurationprocessor.json.JSONObject
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import java.util.logging.Logger


@Controller
class ControlloDuplicatiController {

    @Autowired
    lateinit var excelService: ExcelService

    companion object {
        @JvmStatic private val logger = Logger.getLogger(ControlloDuplicatiController::class.java.name)
    }

    @ApiOperation(value = "Richiesta per elaborare un file excel ed estrarre i doppioni")
    @PostMapping(value = ["/find/doppioni"], produces = ["application/json"])
    fun findDoppioni(@ApiParam(value = "File Excel") @RequestParam("file") file: MultipartFile): ResponseEntity<ServerResponseDto<JSONObject>> {
        logger.info { "API: POST [/find/doppioni] called" }
        return ResponseEntity.ok(this.excelService.findDoppioni(file))
    }
}