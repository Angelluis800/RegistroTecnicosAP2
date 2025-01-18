package edu.ucne.registrotecnicos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import edu.ucne.registrotecnicos.data.local.database.TechnicianDb
import edu.ucne.registrotecnicos.data.local.entity.TechnicianEntity
import edu.ucne.registrotecnicos.data.repository.TecnicoRepository
import edu.ucne.registrotecnicos.presentation.navigation.GeneralNavHost
import edu.ucne.registrotecnicos.ui.theme.RegistroTecnicosTheme

class MainActivity : ComponentActivity() {
    private lateinit var tecnicoRepository: TecnicoRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val technicianDb = Room.databaseBuilder(
            applicationContext,
            TechnicianDb::class.java,
            "TechnicianDb"
        ).fallbackToDestructiveMigration()
            .build()
        tecnicoRepository = TecnicoRepository(technicianDb)

        setContent {
            RegistroTecnicosTheme {
                val navHost = rememberNavController()
                GeneralNavHost(navHost,tecnicoRepository)
            }
        }
    }

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun TechPreview() {
        RegistroTecnicosTheme {
            val tecnicoList = listOf(
                TechnicianEntity(1, "Angel", 15000.0)
            )
        }
    }
}



