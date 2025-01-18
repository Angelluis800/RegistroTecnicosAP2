package edu.ucne.registrotecnicos.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.registrotecnicos.data.local.entity.TechnicianEntity
import kotlinx.coroutines.flow.Flow

@Dao
    interface TechnicianDao{
        @Upsert()
        suspend fun save(technician: TechnicianEntity)

        @Query(
            """
        SELECT * 
        FROM Technicians
        WHERE technicianId =:id  
        LIMIT 1
        """
        )
        suspend fun find(id: Int): TechnicianEntity?

        @Delete
        suspend fun delete(technician: TechnicianEntity)

        @Query("SELECT * FROM Technicians")
        fun getAll(): Flow<List<TechnicianEntity>>
    }