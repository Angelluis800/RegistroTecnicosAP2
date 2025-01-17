package edu.ucne.registrotecnicos.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    data object TechnicianList : Screen() //Consulta

    @Serializable
    data class Technician(val technicianId: Int) : Screen() //Registro
}