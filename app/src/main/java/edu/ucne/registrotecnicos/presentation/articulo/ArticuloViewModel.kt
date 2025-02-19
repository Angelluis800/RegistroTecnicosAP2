package edu.ucne.registrotecnicos.presentation.articulo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrotecnicos.data.remote.Resource
import edu.ucne.registrotecnicos.data.remote.dto.ArticuloDto
import edu.ucne.registrotecnicos.data.repository.ArticuloRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ArticuloViewModel @Inject constructor(
    private val articuloRepository: ArticuloRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ArticuloUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getAllArticulos()
    }

    /** ✅ Guardar artículo con manejo de estados */
    fun save() {
        viewModelScope.launch {
            if (isValidate()) {
                articuloRepository.save(_uiState.value.toEntity()).collectLatest { result ->
                    when (result) {
                        is Resource.Loading -> _uiState.update { it.copy(isLoading = true) }
                        is Resource.Success -> {
                            _uiState.update { it.copy(errorMessage = null, isLoading = false) }
                            new()
                            getAllArticulos()
                        }
                        is Resource.Error -> _uiState.update { it.copy(errorMessage = result.message, isLoading = false) }
                    }
                }
            }
        }
    }

    fun delete(id: Int) {
        viewModelScope.launch {
            articuloRepository.delete(id).collectLatest { result ->
                when (result) {
                    is Resource.Loading -> _uiState.update { it.copy(isLoading = true) }
                    is Resource.Success -> {
                        _uiState.update { it.copy(errorMessage = null, isLoading = false) }
                        getAllArticulos()
                    }
                    is Resource.Error -> _uiState.update { it.copy(errorMessage = result.message, isLoading = false) }
                }
            }
        }
    }

    fun update() {
        viewModelScope.launch {
            articuloRepository.update(
                _uiState.value.articuloId, _uiState.value.toEntity()
            ).collectLatest { result ->
                when (result) {
                    is Resource.Loading -> _uiState.update { it.copy(isLoading = true) }
                    is Resource.Success -> {
                        _uiState.update { it.copy(errorMessage = null, isLoading = false) }
                        getAllArticulos()
                    }
                    is Resource.Error -> _uiState.update { it.copy(errorMessage = result.message, isLoading = false) }
                }
            }
        }
    }

    fun find(articuloId: Int) {
        viewModelScope.launch {
            if (articuloId > 0) {
                articuloRepository.find(articuloId).collectLatest { result ->
                    when (result) {
                        is Resource.Loading -> _uiState.update { it.copy(isLoading = true) }
                        is Resource.Success -> {
                            result.data?.let { articulo ->
                                _uiState.update {
                                    it.copy(
                                        articuloId = articulo.itemId,
                                        descripcion = articulo.description,
                                        costo = articulo.cost,
                                        ganancia = articulo.revenue,
                                        precio = articulo.price,
                                        isLoading = false
                                    )
                                }
                            }
                        }
                        is Resource.Error -> _uiState.update { it.copy(errorMessage = result.message, isLoading = false) }
                    }
                }
            }
        }
    }

    fun getAllArticulos() {
        viewModelScope.launch {
            articuloRepository.getAllArticulos().collectLatest { result ->
                when (result) {
                    is Resource.Loading -> _uiState.update { it.copy(isLoading = true) }
                    is Resource.Success -> _uiState.update {
                        it.copy(listaArticulos = result.data ?: emptyList(), isLoading = false)
                    }
                    is Resource.Error -> _uiState.update { it.copy(errorMessage = result.message, isLoading = false) }
                }
            }
        }
    }

    fun new() {
        _uiState.value = ArticuloUiState()
    }

    fun isValidate(): Boolean {
        val state = uiState.value
        return when {
            state.descripcion.isBlank() -> {
                _uiState.update { it.copy(errorMessage = "La descripción es obligatoria") }
                false
            }
            state.costo <= 0 -> {
                _uiState.update { it.copy(errorMessage = "El costo debe ser mayor a 0") }
                false
            }
            state.ganancia <= 0 || state.ganancia > 100 -> {
                _uiState.update { it.copy(errorMessage = "La ganancia debe ser entre 1 y 100") }
                false
            }
            else -> true
        }
    }

    fun onDescripcionChange(descripcion: String) {
        _uiState.update {
            it.copy(
                descripcion = descripcion,
                errorMessage = if (descripcion.isBlank()) "La descripción no puede estar vacía" else null
            )
        }
    }

    fun onCostoChange(costo: Double) {
        val ganancia = _uiState.value.ganancia
        val precioCalculado = calcularPrecio(costo, ganancia)
        _uiState.update {
            it.copy(costo = costo, precio = precioCalculado)
        }
    }

    fun onGananciaChange(ganancia: Double) {
        val costo = _uiState.value.costo
        val precioCalculado = calcularPrecio(costo, ganancia)
        _uiState.update {
            it.copy(ganancia = ganancia, precio = precioCalculado)
        }
    }

    private fun calcularPrecio(costo: Double, ganancia: Double): Double {
        return if (costo > 0 && ganancia > 0)
            (costo + (costo * (ganancia / 100))).toBigDecimal().setScale(2, java.math.RoundingMode.HALF_UP).toDouble()
        else 0.0
    }
}


fun ArticuloUiState.toEntity() = ArticuloDto(
    itemId = this.articuloId,
    description = this.descripcion,
    cost = this.costo,
    revenue = this.ganancia,
    price = this.precio
)
