package com.gierre.gierrecontrolloduplicati.mapper

import com.gierre.gierrecontrolloduplicati.dto.ExcelRowDto
import com.gierre.gierrecontrolloduplicati.entity.ExcelRow
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class ExcelRowMapper: Mapper<ExcelRow, ExcelRowDto> {
    override fun toDto(entity: ExcelRow) = ExcelRowDto(id = entity.id, pod = entity.pod, pdr = entity.pdr, data_creaz = entity.data_creaz, createdAt = entity.createdAt)
    override fun toEntity(dto: ExcelRowDto) = ExcelRow(id = dto.id, pod = dto.pod, pdr = dto.pdr, data_creaz = dto.data_creaz, createdAt = dto.createdAt ?: LocalDateTime.now())
}