package edu.ucne.registrotecnicos.presentation.articulo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registrotecnicos.presentation.components.ConfirmDeletionDialog
import edu.ucne.registrotecnicos.presentation.components.TopBar

@Composable
fun ArticuloScreen(
    viewModel: ArticuloViewModel = hiltViewModel(),
    articuloId: Int,
    goBackToList: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ArticuloBodyScreen(
        articuloId = articuloId,
        viewModel = viewModel,
        uiState = uiState,
        goBackToList = goBackToList
    )
}

@Composable
fun ArticuloBodyScreen(
    articuloId: Int,
    viewModel: ArticuloViewModel,
    uiState: ArticuloUiState,
    goBackToList: () -> Unit
) {
    LaunchedEffect(articuloId) {
        if (articuloId > 0) viewModel.find(articuloId)
    }

    val openDialog = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopBar(if (articuloId > 0) "Editar Artículo" else "Registrar Artículo")
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Campo Descripción
                    OutlinedTextField(
                        label = { Text(text = "Descripción") },
                        value = uiState.descripcion,
                        onValueChange = viewModel::onDescripcionChange,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface
                        )
                    )

                    // Campo Costo
                    OutlinedTextField(
                        label = { Text(text = "Costo") },
                        value = if (uiState.costo == 0.0) "" else uiState.costo.toString(),
                        onValueChange = { newValue ->
                            newValue.toDoubleOrNull()?.let { viewModel.onCostoChange(it) }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface
                        )
                    )

                    // Campo Ganancia
                    OutlinedTextField(
                        label = { Text(text = "Porciento de Ganancia") },
                        value = if (uiState.ganancia == 0.0) "" else uiState.ganancia.toString(),
                        onValueChange = { newValue ->
                            newValue.toDoubleOrNull()?.let { viewModel.onGananciaChange(it) }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface
                        )
                    )

                    // Campo Precio (Solo lectura)
                    OutlinedTextField(
                        label = { Text(text = "Precio") },
                        value = uiState.precio.toString(),
                        onValueChange = {},
                        modifier = Modifier.fillMaxWidth(),
                        enabled = false,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Gray,
                            unfocusedBorderColor = Color.Gray,
                            disabledTextColor = Color.Gray
                        )
                    )

                    // Espacio para el mensaje de error
                    uiState.errorMessage?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                    }

                    Spacer(modifier = Modifier.padding(2.dp))

                    // Botones
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
                            if (articuloId > 0) {
                                openDialog.value = true
                            } else {
                                viewModel.new()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = if (articuloId > 0) "Borrar" else "Limpiar"
                            )
                            Text(text = if (articuloId > 0) "Borrar" else "Limpiar")
                        }
                        OutlinedButton(
                            onClick = {
                                if (viewModel.isValidate()) {
                                    if (articuloId > 0) viewModel.update() else viewModel.save()
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

    // Diálogo de confirmación de eliminación
    ConfirmDeletionDialog(
        openDialog = openDialog,
        onConfirm = {
            viewModel.delete(uiState.articuloId)
            goBackToList()
        },
        onDismiss = {
            openDialog.value = false
        }
    )
}
