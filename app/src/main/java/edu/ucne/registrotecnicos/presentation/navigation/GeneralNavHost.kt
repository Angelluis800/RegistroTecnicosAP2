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
import edu.ucne.registrotecnicos.presentation.mensajes.ResponseScreen
import edu.ucne.registrotecnicos.presentation.technician.TechnicianListScreen
import edu.ucne.registrotecnicos.presentation.technician.TechnicianScreen

@Composable
fun GeneralNavHost(
    navHostController: NavHostController,
){

    NavHost(
        navController = navHostController,
        startDestination = Screen.HomeScreen
    ) {
        composable<Screen.TechnicianList> {
            TechnicianListScreen(
                createTecnico = {
                    navHostController.navigate(Screen.Technician(0))
                },
                goToMenu = {
                    navHostController.navigateUp()
                },
                goToTecnico = { tecnicoId ->
                    navHostController.navigate(Screen.Technician(technicianId = tecnicoId))
                }
            )
        }
        composable<Screen.Technician> {
            val tecnicoId = it.toRoute<Screen.Technician>().technicianId
            TechnicianScreen(
                tecnicoId = tecnicoId,
                goBackToList = {
                    navHostController.navigateUp()
                }
            )
        }
        composable<Screen.TicketList> {
            TicketListScreen(
                createTicket = {
                    navHostController.navigate(Screen.Ticket(0))
                },
                goToMenu = {
                    navHostController.navigateUp()
                },
               goToTicket = {
                   navHostController.navigate(Screen.Ticket(it))
               }
            )
        }
        composable<Screen.Ticket> {
            val ticketId = it.toRoute<Screen.Ticket>().ticketId
            TicketScreen(
                ticketId = ticketId,
                goBackToList = {
                    navHostController.navigateUp()
                },
                goToReply = {
                    navHostController.navigate(Screen.TicketResponse(ticketId))
                }
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
        composable<Screen.TicketResponse> {
            val ticketId = it.toRoute<Screen.Ticket>().ticketId
            ResponseScreen(
                ticketId = ticketId,
                goBackToTicket = {
                    navHostController.navigateUp()
                }
            )
        }
    }
}