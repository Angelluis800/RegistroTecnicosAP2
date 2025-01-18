package edu.ucne.registrotecnicos.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.registrotecnicos.data.local.dao.TechnicianDao
import edu.ucne.registrotecnicos.data.local.dao.TicketDao
import edu.ucne.registrotecnicos.data.local.entity.TechnicianEntity
import edu.ucne.registrotecnicos.data.local.entity.TicketEntity

@Database(
        entities = [
            TechnicianEntity::class,
            TicketEntity::class
        ],
        version = 2,
        exportSchema = false
    )abstract class AppDataDb : RoomDatabase() {
        abstract fun technicianDao(): TechnicianDao
        abstract fun ticketDao(): TicketDao
    }