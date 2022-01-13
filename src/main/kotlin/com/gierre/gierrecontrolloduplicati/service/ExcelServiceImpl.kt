package com.gierre.gierrecontrolloduplicati.service

import com.gierre.gierrecontrolloduplicati.dto.ExcelRowDto
import com.gierre.gierrecontrolloduplicati.dto.ServerResponseDto
import com.gierre.gierrecontrolloduplicati.helper.ExcelHelper
import com.gierre.gierrecontrolloduplicati.mapper.ExcelRowMapper
import com.gierre.gierrecontrolloduplicati.repository.ExcelRepository
import kotlinx.coroutines.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.configurationprocessor.json.JSONArray
import org.springframework.core.io.InputStreamResource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.logging.Logger
import javax.annotation.Resource
import kotlin.system.measureTimeMillis


@Service
class ExcelServiceImpl: ExcelService {

    @Autowired
    private lateinit var excelHelper: ExcelHelper

    @Autowired
    lateinit var excelRowMapper: ExcelRowMapper

    @Autowired
    lateinit var excelRepository: ExcelRepository

    private var jsonParser = Json { ignoreUnknownKeys = true }

    companion object {
        @JvmStatic private val logger = Logger.getLogger(ExcelServiceImpl::class.java.name)
    }


    override fun findDoppioni(file1: MultipartFile, file2: MultipartFile): ServerResponseDto<List<ExcelRowDto>> {
        return ServerResponseDto.ok(this.findDoppioniWorker(file1, file2))
    }



    override fun findDoppioniExcel(file1: MultipartFile, file2: MultipartFile): ByteArray {
        val doppioni = this.findDoppioniWorker(file1, file2)
        val headers = listOf<String>("Data Creaz.", "POD", "PDR")
        val string = this.jsonParser.encodeToString<List<ExcelRowDto>>(doppioni)
        val fileBuffer = this.excelHelper.write(headers, JSONArray(string))
        return fileBuffer.toByteArray()
    }



    private fun findDoppioniWorker(file1: MultipartFile, file2: MultipartFile): List<ExcelRowDto> {
        logger.info("findDoppioniWorker(file1: ${file1.originalFilename}, file2: ${file2.originalFilename}) started.")

        var list1: List<ExcelRowDto> = emptyList()
        var list2: List<ExcelRowDto> = emptyList()

        var jsonArray1 = JSONArray()
        var jsonArray2 = JSONArray()

        val doppioni = mutableSetOf<ExcelRowDto>()

        val time = measureTimeMillis {
            runBlocking {
                withContext(Dispatchers.IO) {
                    val job1 = async { this@ExcelServiceImpl.excelHelper.read(file1) }
                    val job2 = async { this@ExcelServiceImpl.excelHelper.read(file2) }

                    jsonArray1 = job1.await()
                    jsonArray2 = job2.await()
                }
            }

            runBlocking {
                val job1 = async { this@ExcelServiceImpl.jsonParser.decodeFromString<List<ExcelRowDto>>(jsonArray1.toString()) }
                val job2 = async { this@ExcelServiceImpl.jsonParser.decodeFromString<List<ExcelRowDto>>(jsonArray2.toString()) }

                list1 = job1.await()
                list2 = job2.await()
            }

            val set1 = list1.toSet()
            val set2 = list2.toSet()

            set1.forEach { if(set2.contains(it)) doppioni.add(it) }
        }

        val listEntities = this.excelRowMapper.toEntities(doppioni.toList())
        val doppioniSaved = this.excelRepository.saveAll(listEntities)

        logger.info("findDoppioniWorker(file1: ${file1.originalFilename}, file2: ${file2.originalFilename}) finished in $time ms, found ${doppioni.size} doppioni")

        return this.excelRowMapper.toDtos(doppioniSaved)
    }
}