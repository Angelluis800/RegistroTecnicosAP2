package edu.ucne.registrotecnicos.presentation.mensajes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrotecnicos.data.repository.TecnicoRepository
import edu.ucne.registrotecnicos.data.repository.TicketRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ResponseViewModel @Inject constructor(
    private val ticketRepository: TicketRepository,
    private val tecnicoRepository: TecnicoRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ResponseUiState())
    val uiState = _uiState.asStateFlow()

    fun loadResponses(ticketId: Int) {
        viewModelScope.launch {
            val ticket = ticketRepository.find(ticketId)
            val tecnico = tecnicoRepository.find(ticket?.tecnicoId ?: 0)
            val mensajes = ticketRepository.getMessagesByTicketId(ticketId).first()

            val mensajesConDatos = mensajes.map { mensaje ->
                MensajeConDatos(
                    contenido = mensaje.contenido,
                    fechaHora = mensaje.fecha,
                    nombreTecnico = tecnico?.name ?: "Admin"
                )
            }
            _uiState.update {
                it.copy(
                    ticketId = ticketId,
                    nombreTecnico = tecnico?.name ?: "Admin",
                    mensajes = mensajesConDatos
                )
            }
        }
    }

    fun sendResponse(respuesta: String) {
        viewModelScope.launch {
            ticketRepository.addMessageToTicket(uiState.value.ticketId, respuesta)
            loadResponses(uiState.value.ticketId)
        }
    }
}
