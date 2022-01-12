package com.gierre.gierrecontrolloduplicati.repository

import com.gierre.gierrecontrolloduplicati.entity.ExcelRow
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ExcelRepository: JpaRepository<ExcelRow, Long> {}