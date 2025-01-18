package edu.ucne.registrotecnicos.presentation.Ticket

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.ucne.registrotecnicos.data.local.entity.TicketEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketListScreen(
    ticketList: List<TicketEntity>,
    createTicket: () -> Unit,
    editTicket: (Int) -> Unit,
    deleteTicket: (TicketEntity) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Lista de Tickets",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Fecha",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "ID",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Asunto",
                    modifier = Modifier.weight(2f),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Cliente",
                    modifier = Modifier.weight(2f),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            HorizontalDivider()

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(ticketList) { ticket ->
                    TicketRow(
                        ticket = ticket,
                        editTicket = { ticket.ticketId?.let { it1 -> editTicket(it1)}},
                    )
                }
            }
        }
        FilledIconButton(
            onClick = { createTicket() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Crear Ticket"
            )
        }
    }
}

@Composable
private fun TicketRow(
    ticket: TicketEntity,
    editTicket: (Int) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { ticket
                .ticketId?.let {
                    editTicket(it)
                }
            }
            .padding(
                horizontal = 3.dp,
                vertical = 2.dp
            )
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = ticket.fecha,
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            modifier = Modifier.weight(1f),
            text = ticket.ticketId.toString(),
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            modifier = Modifier.weight(2f),
            text = ticket.asunto,
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            modifier = Modifier.weight(2f),
            text = ticket.cliente,
            style = MaterialTheme.typography.bodyMedium
        )
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = "detalle"
        )
    }
    HorizontalDivider()
}
