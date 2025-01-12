package edu.ucne.registrotecnicos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import edu.ucne.registrotecnicos.ui.theme.RegistroTecnicosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RegistroTecnicosTheme {

            }
        }
    }
}

@Entity(tableName = "Technician")
data class TechnicianEntity(
    @PrimaryKey
    val id: Int? = null,
    val name: String = "",
    val salary: Double = 0.0
)

