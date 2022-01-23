package com.gierre.gierrecontrolloduplicati.entity

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "EXCEL_ROWS")
class ExcelRow(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,

    val pod: String,

    val pdr: String,

    val data_creaz: String,

    val createdAt: LocalDateTime = LocalDateTime.now()
)
{}