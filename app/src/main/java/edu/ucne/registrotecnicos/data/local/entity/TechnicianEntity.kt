package edu.ucne.registrotecnicos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Technicians")
    data class TechnicianEntity(
        @PrimaryKey
        val technicianId: Int? = null,
        val name: String = "",
        val salary: Double = 0.0
    )