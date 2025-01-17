package edu.ucne.registrotecnicos.presentation.technician


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.ucne.registrotecnicos.data.local.entity.TechnicianEntity

@Composable
    fun TechnicianListScreen(techList: List<TechnicianEntity>){
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Text("Lista de Técnicos")

            LazyColumn(
                modifier =  Modifier.fillMaxSize()
            ) {
                items(techList){
                    TechnicianRow(it)
                }
            }
        }
    }

@Composable
private fun TechnicianRow(it: TechnicianEntity) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(modifier = Modifier.weight(1f), text = it.technicianId.toString())
        Text(
            modifier = Modifier.weight(2f),
            text = it.name,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(modifier = Modifier.weight(2f), text =  it.salary.toString())
    }
    HorizontalDivider()
}