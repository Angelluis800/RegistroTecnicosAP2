package edu.ucne.registrotecnicos.data.remote

import edu.ucne.registrotecnicos.data.remote.dto.ArticuloDto
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val articuloManagerApi: ArticuloManagerApi
) {
    suspend fun getArticulos() = articuloManagerApi.getArticulos()
    suspend fun getArticulo(id: Int) = articuloManagerApi.getArticulo(id)
    suspend fun saveArticulo(articuloDto: ArticuloDto) = articuloManagerApi.saveArticulo(articuloDto)
    suspend fun updateArticulo(id: Int, articuloDto: ArticuloDto) = articuloManagerApi.updateArticulo(id, articuloDto)
    suspend fun deleteArticulo(id: Int) = articuloManagerApi.deleteArticulo(id)
}