package edu.ucne.registrotecnicos.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Screen {
    //Técnicos
    @Serializable
    data object TechnicianList : Screen() //Consulta

    @Serializable
    data class Technician(val technicianId: Int) : Screen() //Registro

    //Tickets

    @Serializable
    data object TicketList : Screen() //Consulta

    @Serializable
    data class Ticket(val ticketId: Int) : Screen() //Registro

    @Serializable
    data object HomeScreen : Screen() //Pantalla de Inicio

    @Serializable
    data class TicketResponse(val ticketId: Int) : Screen()
}