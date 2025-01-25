package edu.ucne.registrotecnicos.presentation.technician

import edu.ucne.registrotecnicos.data.local.entity.TechnicianEntity

data class TecnicoUiState(
    val tecnicoId: Int? = null,
    val nombres: String = "",
    val sueldo: String = "",
    val errorMessage: String? = null,
    val tecnicos: List<TechnicianEntity> = emptyList()
)