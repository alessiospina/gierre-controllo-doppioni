package com.gierre.gierrecontrolloduplicati.helper

import com.gierre.gierrecontrolloduplicati.exception.FileExcelException
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.springframework.boot.configurationprocessor.json.JSONArray
import org.springframework.boot.configurationprocessor.json.JSONObject
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.util.logging.Logger
import kotlin.system.measureTimeMillis


@Component
class ExcelHelper {

    companion object {
        @JvmStatic private val logger = Logger.getLogger(ExcelHelper::class.java.name)
    }

    fun read(file: MultipartFile): JSONArray {

        logger.info("read(${file.originalFilename}) started => [name: ${file.name}, bytes: ${file.bytes.size}, content-type: ${file.contentType}]")

        val jsonArray = JSONArray()

        val time = measureTimeMillis {
            val xlWb: Workbook

            try {
                xlWb = WorkbookFactory.create(file.inputStream)
            }
            catch (e: IOException) {
                throw FileExcelException("File: [${file.originalFilename}] non valid.")
            }

            val xlWs = xlWb.getSheetAt(0)
            val maxRow = xlWs.lastRowNum

            val headers = this.getHeaders(xlWs)

            for (i in 1..maxRow) {
                jsonArray.put(this.getRowData(xlWs.getRow(i), headers))
            }
        }

        logger.info("read(${file.originalFilename}) finished in $time ms => ${jsonArray.length()} row processed.")

        return jsonArray
    }


    private fun getHeaders(sheet: Sheet): List<String> {
        return sheet.getRow(0).cellIterator().asSequence().toList().map { it.stringCellValue }
    }


    private fun getRowData(row: Row, headers: List<String>): JSONObject {

        if(headers.size != row.lastCellNum.toInt())
            return JSONObject() // lanciare errore

        val data = JSONObject()

        for(i in 0 until row.lastCellNum) {
            data.put(headers[i], row.getCell(i))
        }

        return data
    }
}