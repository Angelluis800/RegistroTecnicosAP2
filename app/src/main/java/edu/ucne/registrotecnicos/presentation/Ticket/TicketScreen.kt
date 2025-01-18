package edu.ucne.registrotecnicos.presentation.Ticket

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import edu.ucne.registrotecnicos.data.local.entity.TechnicianEntity
import edu.ucne.registrotecnicos.data.local.entity.TicketEntity
import edu.ucne.registrotecnicos.data.repository.TecnicoRepository
import edu.ucne.registrotecnicos.data.repository.TicketRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketScreen(
    ticketId: Int,
    ticketRepository: TicketRepository,
    tecnicoRepository: TecnicoRepository
) {
    var fecha by remember { mutableStateOf("") }
    var cliente by remember { mutableStateOf("") }
    var asunto by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var tecnicoId by remember { mutableIntStateOf(0) }
    var prioridad by remember { mutableStateOf("") }
    var errorMessage: String? by remember { mutableStateOf(null) }

    val technicians = tecnicoRepository.getAll().collectAsState(emptyList())

    LaunchedEffect(ticketId) {
        if (ticketId > 0) {
            val ticket = ticketRepository.find(ticketId)
            ticket?.let {
                fecha = it.fecha
                cliente = it.cliente
                asunto = it.asunto
                descripcion = it.descripcion
                tecnicoId = it.tecnicoId
                prioridad = it.prioridadId.toString()
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = if (ticketId > 0) "Editar Ticket" else "Crear Ticket",
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp)
        ) {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    OutlinedTextField(
                        label = { Text(text = "Fecha") },
                        value = fecha,
                        onValueChange = { fecha = it },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        label = { Text(text = "Cliente") },
                        value = cliente,
                        onValueChange = { cliente = it },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        label = { Text(text = "Asunto") },
                        value = asunto,
                        onValueChange = { asunto = it },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        label = { Text(text = "Descripción") },
                        value = descripcion,
                        onValueChange = { descripcion = it },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        label = { Text(text = "Prioridad (1-6)") },
                        value = prioridad,
                        onValueChange = { prioridad = it },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.padding(2.dp))

                    TechnicianDropdown(
                        tecnicos = technicians.value,
                        tecnicoIdSeleccionado = tecnicoId,
                        onTechnicianSelected = { tecnicoId = it }
                    )

                    Spacer(modifier = Modifier.padding(2.dp))
                    errorMessage?.let {
                        Text(text = it, color = Color.Red)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        OutlinedButton(
                            onClick = {
                                fecha = ""
                                cliente = ""
                                asunto = ""
                                descripcion = ""
                                prioridad = ""
                                tecnicoId = 0
                                errorMessage = null
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "new button"
                            )
                            Text(text = "Nuevo")
                        }

                        val scope = rememberCoroutineScope()
                        OutlinedButton(
                            onClick = {
                                if (fecha.isBlank() || cliente.isBlank() || asunto.isBlank() || descripcion.isBlank() || prioridad.isBlank()) {
                                    errorMessage = "No se puede guardar con los campos vacíos"
                                    return@OutlinedButton
                                }

                                if (prioridad.toIntOrNull() !in 1..6) {
                                    errorMessage = "La prioridad debe estar entre 1 y 6"
                                    return@OutlinedButton
                                }

                                scope.launch {
                                    ticketRepository.save(
                                        TicketEntity(
                                            ticketId = if (ticketId > 0) ticketId else null,
                                            fecha = fecha,
                                            prioridadId = prioridad.toInt(),
                                            cliente = cliente,
                                            asunto = asunto,
                                            descripcion = descripcion,
                                            tecnicoId = tecnicoId
                                        )
                                    )
                                    fecha = ""
                                    cliente = ""
                                    asunto = ""
                                    descripcion = ""
                                    prioridad = ""
                                    tecnicoId = 0
                                    errorMessage = null
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "save button"
                            )
                            Text(text = if (ticketId > 0) "Editar" else "Guardar")
                        }
                        if(ticketId > 0){
                            OutlinedButton(
                                onClick = {
                                    scope.launch {
                                        ticketRepository.delete(
                                            TicketEntity(
                                                ticketId = ticketId,
                                                fecha = "",
                                                prioridadId = 0,
                                                cliente = "",
                                                asunto = "",
                                                descripcion = "",
                                                tecnicoId = 0
                                            )
                                        )
                                        cliente = ""
                                        asunto = ""
                                        descripcion = ""
                                        prioridad = ""
                                        fecha = ""
                                        tecnicoId = 0
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Eliminar",
                                    tint = Color.Red
                                )
                                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                                Text(text = "Eliminar", color = Color.Red)
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TechnicianDropdown(
    tecnicos: List<TechnicianEntity>,
    tecnicoIdSeleccionado: Int,
    onTechnicianSelected: (Int) -> Unit
) {
    var expandido by remember { mutableStateOf(false) }
    val nombreSeleccionado = tecnicos.find { it.technicianId == tecnicoIdSeleccionado }?.name ?: "Seleccionar Técnico"

    ExposedDropdownMenuBox(
        expanded = expandido,
        onExpandedChange = { expandido = !expandido }
    ) {
        TextField(
            value = nombreSeleccionado,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.menuAnchor(),
            label = { Text("Técnico") },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Abrir menú"
                )
            }
        )
        DropdownMenu(
            expanded = expandido,
            onDismissRequest = { expandido = false }
        ) {
            tecnicos.forEach { technician ->
                DropdownMenuItem(
                    onClick = {
                        technician.technicianId?.let { onTechnicianSelected(it) }
                        expandido = false
                    },
                    text = { Text(technician.name) }
                )
            }
        }
    }
}





