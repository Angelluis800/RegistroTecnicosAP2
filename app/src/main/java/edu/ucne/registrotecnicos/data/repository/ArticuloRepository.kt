package edu.ucne.registrotecnicos.data.repository

import android.util.Log
import edu.ucne.registrotecnicos.data.local.dao.ArticuloDao
import edu.ucne.registrotecnicos.data.local.entity.ArticuloEntity
import edu.ucne.registrotecnicos.data.remote.RemoteDataSource
import edu.ucne.registrotecnicos.data.remote.Resource
import edu.ucne.registrotecnicos.data.remote.dto.ArticuloDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ArticuloRepository @Inject constructor(
    private val articuloDao: ArticuloDao,
    private val remoteDataSource: RemoteDataSource
) {

    fun getAllArticulos(): Flow<Resource<List<ArticuloEntity>>> = flow {
        emit(Resource.Loading())

        try {
            val articuloRemoto = remoteDataSource.getArticulos()

            val listaArticulo = articuloRemoto.map { dto ->
                ArticuloEntity(
                    articuloId = dto.itemId,
                    descripcion = dto.description,
                    costo = dto.cost,
                    ganancia = dto.revenue,
                    precio = dto.price
                )
            }
            articuloDao.save(listaArticulo)

            emit(Resource.Success(listaArticulo))

        } catch (e: HttpException) {
            val errorMessage = e.response()?.errorBody()?.string() ?: e.message()
            Log.e("ArticuloRepository", "HttpException: $errorMessage")
            emit(Resource.Error("Error de conexión: $errorMessage"))
        } catch (e: Exception) {
            Log.e("ArticuloRepository", "Error al obtener datos remotos: ${e.message}")

            val localArticulos = articuloDao.getAll().first()

            if (localArticulos.isNotEmpty())
                emit(Resource.Success(localArticulos))
            else
                emit(Resource.Error("Error de conexión: ${e.message}"))
        }
    }

    fun find(id: Int): Flow<Resource<ArticuloDto>> = flow {
        try {
            emit(Resource.Loading())
            val response = remoteDataSource.getArticulo(id)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Resource.Success(it))
                } ?: emit(Resource.Error("El artículo no fue encontrado"))
            } else {
                emit(Resource.Error("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("ArticuloRepository", "Error en find: ${e.message}")
            emit(Resource.Error("Error inesperado: ${e.message}"))
        }
    }

    fun save(articulo: ArticuloDto): Flow<Resource<ArticuloDto>> = flow {
        try {
            emit(Resource.Loading())
            val response = remoteDataSource.saveArticulo(articulo)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Resource.Success(it))
                } ?: emit(Resource.Error("No se pudo guardar el artículo"))
            } else {
                emit(Resource.Error("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("ArticuloRepository", "Error en save: ${e.message}")
            emit(Resource.Error("Error inesperado: ${e.message}"))
        }
    }

    fun update(id: Int, articulo: ArticuloDto): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            val response = remoteDataSource.updateArticulo(id, articulo)
            if (response.isSuccessful) {
                emit(Resource.Success(Unit))
            } else {
                emit(Resource.Error("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("ArticuloRepository", "Error en update: ${e.message}")
            emit(Resource.Error("Error inesperado: ${e.message}"))
        }
    }

    fun delete(articuloId: Int): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            val response = remoteDataSource.deleteArticulo(articuloId)
            if (response.isSuccessful) {
                emit(Resource.Success(Unit))
            } else {
                emit(Resource.Error("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("ArticuloRepository", "Error en delete: ${e.message}")
            emit(Resource.Error("Error inesperado: ${e.message}"))
        }
    }
}
