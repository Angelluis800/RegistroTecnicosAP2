package edu.ucne.registrotecnicos.data.repository

import edu.ucne.registrotecnicos.data.local.dao.TicketDao
import edu.ucne.registrotecnicos.data.local.database.AppDataDb
import edu.ucne.registrotecnicos.data.local.entity.TechnicianEntity
import edu.ucne.registrotecnicos.data.local.entity.TicketEntity
import kotlinx.coroutines.flow.Flow

class TicketRepository(
    private val ticketDb: AppDataDb
) {
    suspend fun save(ticket: TicketEntity) {
        ticketDb.ticketDao().save(ticket)
    }

    suspend fun find(id: Int): TicketEntity? {
        return ticketDb.ticketDao().find(id)
    }

    fun getAll(): Flow<List<TicketEntity>> {
        return ticketDb.ticketDao().getAll()
    }

    suspend fun delete(ticket: TicketEntity) {
        ticketDb.ticketDao().delete(ticket)
    }
}