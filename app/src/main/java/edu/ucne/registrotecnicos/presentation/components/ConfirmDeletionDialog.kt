package edu.ucne.registrotecnicos.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ConfirmDeletionDialog(
    openDialog: MutableState<Boolean>,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    title: String = "Confirmar eliminación",
    message: String = "¿Quieres eliminar este elemento? Esta acción no se puede deshacer."
) {
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
                onDismiss()
            },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(48.dp)
                )
            },
            title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirm()
                        openDialog.value = false
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Eliminar", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                        onDismiss()
                    }
                ) {
                    Text("Cancelar", fontWeight = FontWeight.Bold)
                }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }
}
