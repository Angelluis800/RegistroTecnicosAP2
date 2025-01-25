package edu.ucne.registrotecnicos.data.repository

import edu.ucne.registrotecnicos.data.local.dao.TechnicianDao
import edu.ucne.registrotecnicos.data.local.database.AppDataDb
import edu.ucne.registrotecnicos.data.local.entity.TechnicianEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TecnicoRepository @Inject constructor(
     private val tecnicoDao: TechnicianDao
){
    suspend fun save(tecnico: TechnicianEntity) = tecnicoDao.save(tecnico)

    suspend fun find(id: Int): TechnicianEntity? = tecnicoDao.find(id)

    fun getAll(): Flow<List<TechnicianEntity>> = tecnicoDao.getAll()

    suspend fun delete(tecnico: TechnicianEntity) = tecnicoDao.delete(tecnico)
}