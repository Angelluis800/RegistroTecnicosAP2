package edu.ucne.registrotecnicos.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.registrotecnicos.data.local.entity.ArticuloEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticuloDao {
    @Upsert
    suspend fun save(listaArticulo: List<ArticuloEntity>)

    @Query(
        """
            Select * From Articulos 
            Where articuloId = :articuloId
            Limit 1
        """
    )
    suspend fun find(articuloId: Int): ArticuloEntity?

    @Delete
    suspend fun delete(articulo: ArticuloEntity)

    @Query("Select * From Articulos")
    fun getAll(): Flow<List<ArticuloEntity>>
}