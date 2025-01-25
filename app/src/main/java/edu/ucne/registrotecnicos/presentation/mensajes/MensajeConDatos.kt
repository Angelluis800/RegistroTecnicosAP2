package edu.ucne.registrotecnicos.presentation.mensajes

data class MensajeConDatos(
    val contenido: String,
    val fechaHora: String,
    val nombreTecnico: String
)