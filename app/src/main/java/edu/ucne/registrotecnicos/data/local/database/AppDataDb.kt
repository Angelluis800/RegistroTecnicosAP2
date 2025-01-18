package edu.ucne.registrotecnicos.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.registrotecnicos.data.local.dao.TechnicianDao
import edu.ucne.registrotecnicos.data.local.entity.TechnicianEntity

@Database(
        entities = [
            TechnicianEntity::class
        ],
        version = 1,
        exportSchema = false
    )abstract class AppDataDb : RoomDatabase() {
        abstract fun technicianDao(): TechnicianDao
    }