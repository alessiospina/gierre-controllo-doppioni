package com.gierre.gierrecontrolloduplicati.controller

import com.gierre.gierrecontrolloduplicati.dto.ExcelRowDto
import com.gierre.gierrecontrolloduplicati.dto.ServerResponseDto
import com.gierre.gierrecontrolloduplicati.service.ExcelService
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import java.util.logging.Logger
import javax.annotation.Resource


@Controller
class ControlloDuplicatiController {

    @Autowired
    lateinit var excelService: ExcelService

    companion object {
        @JvmStatic private val logger = Logger.getLogger(ControlloDuplicatiController::class.java.name)
    }


    @ApiOperation(value = "Dashboard View")
    @GetMapping(value = ["/dashboard"])
    fun dashboard(model: Model): String{
        return "dashboard"
    }

    @ApiOperation(value = "Richiesta per elaborare un file excel ed estrarre i doppioni")
    @PostMapping(value = ["/find/doppioni"], produces = ["application/json"])
    fun findDoppioni(
        @ApiParam(value = "File Excel") @RequestParam("file1") file1: MultipartFile,
        @ApiParam(value = "File Excel") @RequestParam("file2") file2: MultipartFile, ): ResponseEntity<ServerResponseDto<List<ExcelRowDto>>> {
        logger.info { "API: POST [/find/doppioni] called" }
        return ResponseEntity.ok(this.excelService.findDoppioni(file1, file2))
    }


    @ApiOperation(value = "Richiesta per estrarre i doppioni ed inviarli in un file excel")
    @PostMapping(value = ["/find/doppioni/send/excel"], produces = ["application/json"])
    fun findDoppioniAndSend(
        @ApiParam(value = "File Excel") @RequestParam("file1") file1: MultipartFile,
        @ApiParam(value = "File Excel") @RequestParam("file2") file2: MultipartFile, ): ResponseEntity<ByteArray> {
        logger.info { "API: POST [/find/doppioni/send/excel] called" }

        return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=result.xlsx")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(this.excelService.findDoppioniExcel(file1, file2))
    }
}