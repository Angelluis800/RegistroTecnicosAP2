package edu.ucne.registrotecnicos.presentation.technician

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import edu.ucne.registrotecnicos.data.local.entity.TechnicianEntity
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
    fun TechnicianListScreen(
    tecnicoList: List<TechnicianEntity>,
    createTecnico: () -> Unit
    ){
        Box(
            modifier = Modifier.fillMaxSize()
        ){
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Lista de Técnicos",
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(tecnicoList) {
                        TechnicianRow(it)
                    }
                }
            }
            FilledIconButton(
                onClick = {
                    createTecnico()
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Crear Tecnico"
                )
            }
        }
    }

@Composable
private fun TechnicianRow(it: TechnicianEntity) {
    val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 8.dp,
                vertical = 4.dp
            )
    ) {
        Text(modifier = Modifier.weight(1f), text = it.technicianId.toString())
        Text(
            modifier = Modifier.weight(2f),
            text = it.name,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(modifier = Modifier
            .weight(2f),
            text = formatter.format(it.salary),
            textAlign = TextAlign.Center
        )
    }
    HorizontalDivider()
}