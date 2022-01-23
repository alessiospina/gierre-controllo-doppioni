package com.gierre.gierrecontrolloduplicati.service

import com.gierre.gierrecontrolloduplicati.config.ConfigEnv
import com.gierre.gierrecontrolloduplicati.dto.ExcelRowDto
import com.gierre.gierrecontrolloduplicati.dto.ResponseExcelRowDto
import com.gierre.gierrecontrolloduplicati.dto.ServerResponseDto
import com.gierre.gierrecontrolloduplicati.entity.ExcelRow
import com.gierre.gierrecontrolloduplicati.exception.BadRequestException
import com.gierre.gierrecontrolloduplicati.helper.HttpClientModule
import com.gierre.gierrecontrolloduplicati.mapper.ExcelRowMapper
import com.gierre.gierrecontrolloduplicati.repository.ExcelRepository
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.logging.Logger
import kotlin.system.measureTimeMillis


@Service
class ExcelServiceImpl: ExcelService {


    @Autowired
    private lateinit var excelRowMapper: ExcelRowMapper

    @Autowired
    private lateinit var excelRepository: ExcelRepository

    @Autowired
    private lateinit var configEnv: ConfigEnv

    @Autowired
    private lateinit var httpClientModule: HttpClientModule

    private var jsonParser = Json { ignoreUnknownKeys = true }
    private var gsonParser = GsonBuilder().create()

    companion object {
        @JvmStatic private val Log = Logger.getLogger(ExcelServiceImpl::class.java.name)
    }

    override fun findDoppioni(file1: MultipartFile, file2: MultipartFile): ServerResponseDto<List<ExcelRowDto>> {

        val toSave: List<ExcelRow>

        val time = measureTimeMillis {

            val requestFile1 = HttpClientModule.Request
                .create()
                .setCall(this.configEnv.GIERRE_EXCEL_MANAGER, HttpClientModule.Request.HttpCall.Post)
                .attachExcelFile(file1)

            val requestFile2 = HttpClientModule.Request
                .create()
                .setCall(this.configEnv.GIERRE_EXCEL_MANAGER, HttpClientModule.Request.HttpCall.Post)
                .attachExcelFile(file2)

            val resFile1: Array<ResponseExcelRowDto>
            val resFile2: Array<ResponseExcelRowDto>

            runBlocking(context = Dispatchers.IO) {
                val job1 = async { this@ExcelServiceImpl.httpClientModule.call(requestFile1) }
                val job2 = async { this@ExcelServiceImpl.httpClientModule.call(requestFile2) }

                val jsonRes1 = JSONObject(job1.await()).get("data") ?: BadRequestException("BadRequestException", "JSON returned from file ${file1.name}does not have \"data\" parameter")
                val jsonRes2 = JSONObject(job2.await()).get("data") ?: BadRequestException("BadRequestException", "JSON returned from file ${file2.name}does not have \"data\" parameter")

                resFile1 = HttpClientModule.ResponseParser.parse(jsonRes1.toString(), Array<ResponseExcelRowDto>::class.java)
                resFile2 = HttpClientModule.ResponseParser.parse(jsonRes2.toString(), Array<ResponseExcelRowDto>::class.java)
            }

            val set1 = resFile1.toSet()
            val set2 = resFile2.toSet()

            val resSet = mutableSetOf<ResponseExcelRowDto>()

            set1.forEach {
                if (set2.contains(it)) resSet.add(it)
            }

            toSave = resSet.map {
                this.excelRepository.save(ExcelRow(id = null, pod = it.POD, pdr = it.PDR, data_creaz = it.data_creaz))
            }
        }

        Log.info("ExcelServiceImpl.findDoppioni(${file1.originalFilename}, ${file2.originalFilename}) finished in $time ms processed rows ${toSave.size}")

        return ServerResponseDto.ok(this.excelRowMapper.toDtos(toSave))
    }

    override fun findDoppioniExcel(file1: MultipartFile, file2: MultipartFile): ByteArray {
        TODO("Not yet implemented")
    }

}