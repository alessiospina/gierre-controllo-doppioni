package com.gierre.gierrecontrolloduplicati.mapper

interface Mapper<E,D> {
    fun toDto(entity: E): D
    fun toDtos(listEntities: List<E>) = listEntities.map { toDto(it) }
    fun toEntity(dto: D): E
    fun toEntities(listDto: List<D>) = listDto.map { toEntity(it) }
}