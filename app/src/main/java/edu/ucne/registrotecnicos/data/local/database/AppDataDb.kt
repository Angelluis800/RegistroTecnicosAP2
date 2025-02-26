package edu.ucne.registrotecnicos.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.registrotecnicos.data.local.dao.ArticuloDao
import edu.ucne.registrotecnicos.data.local.dao.MensajeDao
import edu.ucne.registrotecnicos.data.local.dao.TechnicianDao
import edu.ucne.registrotecnicos.data.local.dao.TicketDao
import edu.ucne.registrotecnicos.data.local.entity.ArticuloEntity
import edu.ucne.registrotecnicos.data.local.entity.MensajeEntity
import edu.ucne.registrotecnicos.data.local.entity.TechnicianEntity
import edu.ucne.registrotecnicos.data.local.entity.TicketEntity

@Database(
        entities = [
            TechnicianEntity::class,
            TicketEntity::class,
            MensajeEntity::class,
            ArticuloEntity::class
        ],
        version = 6,
        exportSchema = false
    )abstract class AppDataDb : RoomDatabase() {
        abstract fun technicianDao(): TechnicianDao
        abstract fun ticketDao(): TicketDao
        abstract fun mensajeDao(): MensajeDao
        abstract fun articuloDao(): ArticuloDao
    }