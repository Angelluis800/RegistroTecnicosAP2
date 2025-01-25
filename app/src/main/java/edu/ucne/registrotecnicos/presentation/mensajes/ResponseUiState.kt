package edu.ucne.registrotecnicos.presentation.mensajes

data class ResponseUiState(
    val nombreTecnico: String = "",
    val mensajes: List<MensajeConDatos> = emptyList(),
    val ticketId: Int = 0
)