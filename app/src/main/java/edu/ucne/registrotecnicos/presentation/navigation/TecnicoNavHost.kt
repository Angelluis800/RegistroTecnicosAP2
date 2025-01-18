package edu.ucne.registrotecnicos.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edu.ucne.registrotecnicos.data.repository.TecnicoRepository
import edu.ucne.registrotecnicos.presentation.technician.TechnicianListScreen
import edu.ucne.registrotecnicos.presentation.technician.TechnicianScreen

@Composable
fun TecnicoNavHost(
    navHostController: NavHostController,
    tecnicoRepository: TecnicoRepository
){
    val lifecycleOwner = LocalLifecycleOwner.current
    val technicianList by tecnicoRepository.getAll()
        .collectAsStateWithLifecycle(
            initialValue = emptyList(),
            lifecycleOwner = lifecycleOwner,
            minActiveState = Lifecycle.State.STARTED
        )

    NavHost(
        navController = navHostController,
        startDestination = Screen.TechnicianList
    ) {
        composable<Screen.TechnicianList> {
            TechnicianListScreen(
                tecnicoList = technicianList,
                createTecnico = {
                    navHostController.navigate(Screen.Technician(0))
                },
            )
        }
        composable<Screen.Technician> {
            TechnicianScreen(
                tecnicoRepository = tecnicoRepository
            )
        }
    }
}