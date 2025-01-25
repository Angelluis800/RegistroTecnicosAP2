package edu.ucne.registrotecnicos.presentation.Ticket

import edu.ucne.registrotecnicos.data.local.entity.TicketEntity
import java.util.Date

data class TicketUiState(
    val ticketId: Int? = null,
    val fecha: String = "",
    val prioridadId: Int? = null,
    val cliente: String = "",
    val asunto: String = "",
    val descripcion: String = "",
    val tecnicoId: Int? = null,
    val errorMessage: String? = null,
    val tickets: List<TicketEntity> = emptyList()
)