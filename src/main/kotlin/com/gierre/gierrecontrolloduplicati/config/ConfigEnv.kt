package com.gierre.gierrecontrolloduplicati.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class ConfigEnv {

    @Value("\${spring.env.gierre-excel-manager}")
    lateinit var GIERRE_EXCEL_MANAGER: String

}