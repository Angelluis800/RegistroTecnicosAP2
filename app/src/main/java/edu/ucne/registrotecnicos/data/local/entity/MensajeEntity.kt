package edu.ucne.registrotecnicos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "Mensajes")
data class MensajeEntity (
    @PrimaryKey
    val mensajeId: Int? = null,
    val ticketId: Int,
    val contenido: String,
    val fecha: String = "",
)