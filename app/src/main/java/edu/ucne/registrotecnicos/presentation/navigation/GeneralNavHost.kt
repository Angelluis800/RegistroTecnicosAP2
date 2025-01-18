package edu.ucne.registrotecnicos.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import edu.ucne.registrotecnicos.data.repository.TecnicoRepository
import edu.ucne.registrotecnicos.data.repository.TicketRepository
import edu.ucne.registrotecnicos.presentation.Ticket.TicketListScreen
import edu.ucne.registrotecnicos.presentation.Ticket.TicketScreen
import edu.ucne.registrotecnicos.presentation.technician.TechnicianListScreen
import edu.ucne.registrotecnicos.presentation.technician.TechnicianScreen

@Composable
fun GeneralNavHost(
    navHostController: NavHostController,
    tecnicoRepository: TecnicoRepository,
    ticketRepository: TicketRepository
){
    val lifecycleOwner = LocalLifecycleOwner.current
    val technicianList by tecnicoRepository.getAll()
        .collectAsStateWithLifecycle(
            initialValue = emptyList(),
            lifecycleOwner = lifecycleOwner,
            minActiveState = Lifecycle.State.STARTED
        )
    val ticketList by ticketRepository.getAll()
        .collectAsStateWithLifecycle(
            initialValue = emptyList(),
            lifecycleOwner = lifecycleOwner,
            minActiveState = Lifecycle.State.STARTED
        )

    NavHost(
        navController = navHostController,
        startDestination = Screen.HomeScreen
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
        composable<Screen.TicketList> {
            TicketListScreen(
                ticketList = ticketList,
                createTicket = {
                    navHostController.navigate(Screen.Ticket(0))
                },
                editTicket = {
                    navHostController.navigate(Screen.Ticket(it))

                },
                deleteTicket = { ticket ->
                    //ticketRepository.delete(ticket)
                }
            )
        }
        composable<Screen.Ticket> {
            val ticketId = it.toRoute<Screen.Ticket>().ticketId
            TicketScreen(
                ticketRepository = ticketRepository,
                tecnicoRepository = tecnicoRepository,
                ticketId = ticketId
            )
        }
        composable<Screen.HomeScreen> {
            HomeScreen(
                goToTecnicos = {
                    navHostController.navigate(Screen.TechnicianList)
                },
                goToTickets = {
                    navHostController.navigate(Screen.TicketList)
                }
            )
        }
    }
}