package edu.ucne.registrotecnicos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import edu.ucne.registrotecnicos.data.local.database.AppDataDb
import edu.ucne.registrotecnicos.data.local.entity.TechnicianEntity
import edu.ucne.registrotecnicos.data.repository.TecnicoRepository
import edu.ucne.registrotecnicos.data.repository.TicketRepository
import edu.ucne.registrotecnicos.presentation.navigation.GeneralNavHost
import edu.ucne.registrotecnicos.ui.theme.RegistroTecnicosTheme

class MainActivity : ComponentActivity() {
    private lateinit var tecnicoRepository: TecnicoRepository
    private  lateinit var ticketRepository: TicketRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val appDataDb = Room.databaseBuilder(
            applicationContext,
            AppDataDb::class.java,
            "AppDataDb"
        ).fallbackToDestructiveMigration()
            .build()
        tecnicoRepository = TecnicoRepository(appDataDb)
        ticketRepository = TicketRepository(appDataDb)

        setContent {
            RegistroTecnicosTheme {
                val navHost = rememberNavController()
                GeneralNavHost(navHost,tecnicoRepository,ticketRepository)
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



