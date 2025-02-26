package edu.ucne.registrotecnicos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Articulos")
data class ArticuloEntity(
    @PrimaryKey(autoGenerate = true)
    val articuloId: Int? = null,
    val descripcion: String,
    val costo: Double,
    val ganancia: Double,
    val precio: Double
)