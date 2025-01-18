package edu.ucne.registrotecnicos.data.repository

import edu.ucne.registrotecnicos.data.local.database.TechnicianDb
import edu.ucne.registrotecnicos.data.local.entity.TechnicianEntity
import kotlinx.coroutines.flow.Flow

class TecnicoRepository(
     private val tecnicoDb: TechnicianDb
) {
    suspend fun save(tecnico: TechnicianEntity) {
        tecnicoDb.technicianDao().save(tecnico)
    }

    suspend fun find(id: Int): TechnicianEntity? {
        return tecnicoDb.technicianDao().find(id)
    }

    fun getAll(): Flow<List<TechnicianEntity>> {
        return tecnicoDb.technicianDao().getAll()
    }

    suspend fun delete(tecnico: TechnicianEntity) {
        tecnicoDb.technicianDao().delete(tecnico)
    }
}