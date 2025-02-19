package edu.ucne.registrotecnicos.presentation.technician

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrotecnicos.data.local.entity.TechnicianEntity
import edu.ucne.registrotecnicos.data.repository.TecnicoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TecnicoViewModel @Inject constructor(
    private val tecnicoRepository: TecnicoRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(TecnicoUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getTecnicos()
    }

    fun save() {
        viewModelScope.launch {
            if (isValid()) {
                tecnicoRepository.save(_uiState.value.toEntity())
            }
        }
    }

    fun onNombresChange(nombres: String) {
        _uiState.update {
            it.copy(
                nombres = nombres,
                errorMessage = if (nombres.isBlank()) "Debes rellenar el campo Nombre"
                else null
            )
        }
    }

    fun onSueldoChange(sueldo: String) {
        _uiState.update {
            val sueldoDouble = sueldo.toDoubleOrNull()
            it.copy(
                sueldo = sueldo,
                errorMessage = when {
                    sueldoDouble == null -> "Sueldo no numérico"
                    sueldoDouble <= 0 -> "El sueldo debe ser mayor a 0"
                    else -> null
                }
            )
        }
    }

    fun new() {
        _uiState.value = TecnicoUiState()
    }

    fun delete() {
        viewModelScope.launch {
            tecnicoRepository.delete(_uiState.value.toEntity())
        }
    }

    private fun getTecnicos() {
        viewModelScope.launch {
            tecnicoRepository.getAll().collect { tecnicos ->
                _uiState.update {
                    it.copy(tecnicos = tecnicos)
                }
            }
        }
    }

    fun find(tecnicoId: Int) {
        viewModelScope.launch {
            if (tecnicoId > 0) {
                val tecnico = tecnicoRepository.find(tecnicoId)
                if (tecnico != null) {
                    _uiState.update {
                        it.copy(
                            tecnicoId = tecnico.technicianId,
                            nombres = tecnico.name,
                            sueldo = tecnico.salary.toString()
                        )
                    }
                }
            }
        }
    }

    fun isValid(): Boolean {
        val nombresValid = _uiState.value.nombres.isNotBlank()
        val sueldoValid = _uiState.value.sueldo.toDoubleOrNull() != null

        _uiState.update {
            it.copy(
                errorMessage = when {
                    !nombresValid -> "Debes rellenar el campo Nombre"
                    !sueldoValid -> "Debes rellenar el campo Sueldo"
                    else -> null
                }
            )
        }

        return nombresValid && sueldoValid
    }
}

fun TecnicoUiState.toEntity() = TechnicianEntity(
    technicianId = this.tecnicoId,
    name = this.nombres,
    salary = this.sueldo.toDouble(),
)