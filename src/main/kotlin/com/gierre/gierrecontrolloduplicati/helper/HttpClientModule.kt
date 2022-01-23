package com.gierre.gierrecontrolloduplicati.helper


import com.gierre.gierrecontrolloduplicati.dto.ResponseExcelRowDto
import com.gierre.gierrecontrolloduplicati.exception.InternalServerException
import com.google.gson.GsonBuilder
import org.apache.http.client.methods.*
import org.apache.http.entity.ContentType
import org.apache.http.entity.StringEntity
import org.apache.http.entity.mime.MultipartEntityBuilder
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager
import org.apache.http.util.EntityUtils
import org.json.JSONObject
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.net.URLEncoder
import java.util.logging.Logger
import kotlin.system.measureTimeMillis


@Component
class HttpClientModule {

    companion object {

        private val Log = Logger.getLogger(HttpClientModule::class.java.name)
        private var GsonParser = GsonBuilder().create()


        //Creating the Client Connection Pool Manager by instantiating the PoolingHttpClientConnectionManager class.
        val ConnManager = PoolingHttpClientConnectionManager()
        val ClientBuilder = HttpClients.custom().setConnectionManager(ConnManager)
    }

    private val Client: CloseableHttpClient = ClientBuilder.build()

    init {
        ConnManager.maxTotal = 100
    }



     fun call(request: Request): String {

        val response: CloseableHttpResponse
        var res = ""

        val timer = measureTimeMillis {
            try {
                response = Client.execute(request.get())
            } catch (e: IOException) {
                Log.severe(e.printStackTrace().toString())
                throw InternalServerException("HttpClientModuleException", "Connection Error!")
            } catch (e: Exception) {
                Log.severe(e.printStackTrace().toString())
                throw InternalServerException("HttpClientModuleException", "Unknown Error!")
            }
        }

        try {
            res = EntityUtils.toString(response.entity, "UTF-8")
        }
        catch (e: Exception) {
            Log.severe(e.printStackTrace().toString())
            throw InternalServerException("HttpClientModuleException", "Error to retrieve response!")
        }

         Log.info("HttpClientModule.call($request) executed in $timer ms with result length:${res.length}")

         return res
    }


    object ResponseParser {
        fun <T> parse(response: String, classType: Class<T>): T {

            val obj: T

            try {
                obj = GsonParser.fromJson(response.toString(), classType)
            }
            catch (e: Exception) {
                Log.severe(e.printStackTrace().toString())
                throw InternalServerException("HttpClientModuleException", "Error to parse response!")
            }

            return obj
        }
    }


    class Request() {
        private lateinit var request: HttpRequestBase
        private lateinit var type: HttpCall
        private lateinit var url: String

        sealed class HttpCall {
            object Post : HttpCall()
            object Get : HttpCall()
            object Put : HttpCall()
            object Delete : HttpCall()
            object Patch : HttpCall()
        }


        companion object {
            @JvmStatic fun create() = Request()
        }


        fun get() = this.request



        fun setCall(url: String, type: HttpCall): Request {

            this.type = type
            this.url = url

            this.request = when(type) {
                    is HttpCall.Get -> { HttpGet(url) }
                    is HttpCall.Delete -> { HttpDelete(url) }
                    is HttpCall.Patch -> { HttpPatch(url) }
                    is HttpCall.Post -> { HttpPost(url) }
                    is HttpCall.Put -> { HttpPut(url) }
            }

            return this
        }


        fun addHeaders(headers: List<Pair<String, String>>): Request {

            headers.forEach {
                this.request.addHeader(it.first, it.second)
            }

            return this
        }




        fun setBody(body: String): Request {
            val entity = StringEntity(body)

            this.request.addHeader("Content-Type", "application/json")

            when(this.type) {
                is HttpCall.Post -> { (this.request as HttpPost).entity = entity }
                is HttpCall.Put -> { (this.request as HttpPut).entity = entity }
                is HttpCall.Delete -> { (this.request as HttpPatch).entity = entity }
                else -> { throw InternalServerException("HttpClientModuleException", "Invalid composition of http call: $type call cannot have a body.")}
            }

            return this
        }




         fun setQueryParams(params: List<Pair<String, String>>): Request {

            var query = ""

            for (i in params.indices) {
                query += "${params[i].first}=" + URLEncoder.encode(params[i].second, "UTF-8")

                if(i < params.size-1)
                    query += "&"
            }

             this.url = this.url + "?" + query

            return this
        }


        fun attachExcelFile(file: MultipartFile): Request {

            val entity = MultipartEntityBuilder
                .create()
                .addBinaryBody("file", file.inputStream, ContentType.MULTIPART_FORM_DATA, file.name)
                .build()

            when(this.type) {
                is HttpCall.Post -> { (this.request as HttpPost).entity = entity }
                is HttpCall.Put -> { (this.request as HttpPut).entity = entity }
                is HttpCall.Delete -> { (this.request as HttpPatch).entity = entity }
                else -> { throw InternalServerException("HttpClientModuleException", "Invalid composition of http call: $type call cannot have a body.")}
            }

            return this
        }


        override fun toString(): String {
            return "Request(URL=${this.url}, METHOD: ${this.type}, HEADERS: ${this.request.allHeaders})"
        }

    }

}