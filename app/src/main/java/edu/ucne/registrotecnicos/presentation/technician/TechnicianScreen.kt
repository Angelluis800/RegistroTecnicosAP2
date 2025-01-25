package edu.ucne.registrotecnicos.presentation.technician

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registrotecnicos.data.local.entity.TechnicianEntity
import edu.ucne.registrotecnicos.data.repository.TecnicoRepository
import edu.ucne.registrotecnicos.presentation.components.ConfirmDeletionDialog
import edu.ucne.registrotecnicos.presentation.components.TopBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TechnicianScreen(
    viewModel: TecnicoViewModel = hiltViewModel(),
    tecnicoId: Int,
    goBackToList: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    TecnicoBodyScreen(
        tecnicoId = tecnicoId,
        viewModel = viewModel,
        uiState = uiState,
        goBackToList = goBackToList
    )
}

@Composable
fun TecnicoBodyScreen(
    tecnicoId: Int,
    viewModel: TecnicoViewModel,
    uiState: TecnicoUiState,
    goBackToList: () -> Unit
) {
    LaunchedEffect(tecnicoId) {
        if (tecnicoId > 0) viewModel.find(tecnicoId)
    }
    val openDialog = remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopBar(if (tecnicoId > 0) "Editar Técnico" else "Registrar Técnico")
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
                        label = { Text(text = "Nombres") },
                        value = uiState.nombres,
                        onValueChange = viewModel::onNombresChange,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        label = { Text(text = "Sueldo") },
                        value = uiState.sueldo,
                        onValueChange = viewModel::onSueldoChange,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    uiState.errorMessage?.let {
                        Text(text = it, color = Color.Red)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(onClick = { goBackToList() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Go back"
                            )
                            Text(text = "Atrás")
                        }
                        OutlinedButton(onClick = {
                            if (tecnicoId > 0) {
                                openDialog.value = true
                            } else {
                                viewModel.new()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = if (tecnicoId > 0) "Borrar" else "Limpiar"
                            )
                            Text(text = if (tecnicoId > 0) "Borrar" else "Limpiar")
                        }
                        OutlinedButton(
                            onClick = {
                                if (viewModel.isValid()) {
                                    viewModel.save()
                                    goBackToList()
                                }
                            }) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Save button"
                            )
                            Text(text = "Guardar")
                        }
                    }
                }
            }
        }
    }
    ConfirmDeletionDialog(
        openDialog = openDialog,
        onConfirm = {
            viewModel.delete()
            goBackToList()
        },
        onDismiss = {
            openDialog.value = false
        }
    )
}