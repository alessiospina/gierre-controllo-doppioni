package com.gierre.gierrecontrolloduplicati.helper

import com.gierre.gierrecontrolloduplicati.exception.FileExcelException
import org.apache.commons.io.output.ByteArrayOutputStream
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.xssf.usermodel.XSSFWorkbook
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

        logger.info("read(filename: ${file.originalFilename}) finished in $time ms => ${jsonArray.length()} row processed.")

        return jsonArray
    }



    fun write(headers: List<String>, data: JSONArray): ByteArrayOutputStream {

        val xlWb = XSSFWorkbook()
        val xlWs = xlWb.createSheet()

        val headerRow = xlWs.createRow(0)

        headers.forEachIndexed {index, value ->
            headerRow.createCell(index).setCellValue(value)
        }

        for (i in 1 until data.length()) {
            val dataRow = xlWs.createRow(i)
            val obj = data.getJSONObject(i)
            headers.forEachIndexed { index, it ->
                dataRow.createCell(index).setCellValue(obj.optString(it))
            }
        }

        val bos = ByteArrayOutputStream()
        xlWb.write(bos)
        bos.close()

        return bos
    }


    private fun getHeaders(sheet: Sheet): List<String> {
        return sheet.getRow(0).cellIterator().asSequence().toList().map { it.stringCellValue }
    }


    private fun getRowData(row: Row, headers: List<String>): JSONObject {

        if(headers.size != row.lastCellNum.toInt())
            return JSONObject() // lanciare errore

        val data = JSONObject()

        for(i in 0 until row.lastCellNum) {
            val cell = row.getCell(i)
            data.put(headers[i], cell)
        }

        return data
    }
}