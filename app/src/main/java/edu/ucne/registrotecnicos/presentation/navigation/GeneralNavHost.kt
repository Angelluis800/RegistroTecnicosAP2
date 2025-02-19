package edu.ucne.registrotecnicos.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import edu.ucne.registrotecnicos.presentation.Ticket.TicketListScreen
import edu.ucne.registrotecnicos.presentation.Ticket.TicketScreen
import edu.ucne.registrotecnicos.presentation.mensajes.ResponseScreen
import edu.ucne.registrotecnicos.presentation.technician.TechnicianListScreen
import edu.ucne.registrotecnicos.presentation.technician.TechnicianScreen
import edu.ucne.registrotecnicos.presentation.articulo.ArticuloListScreen
import edu.ucne.registrotecnicos.presentation.articulo.ArticuloScreen

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
                },
                goToArticulos = {
                    navHostController.navigate(Screen.ArticuloList)
                }

            )
        }
        composable<Screen.TicketResponse> {
            val ticketId = it.toRoute<Screen.Ticket>().ticketId
            ResponseScreen(
                ticketId = ticketId,
                goBackToTicket = {1
                    navHostController.navigateUp()
                }
            )
        }
        composable<Screen.ArticuloList> {
            ArticuloListScreen(
                createArticulo = { navHostController.navigate(Screen.Articulo(0)) },
                goToMenu = { navHostController.navigateUp() },
                goToArticulo = { navHostController.navigate(Screen.Articulo(it)) }
            )
        }
        composable<Screen.Articulo> {
            val articuloId = it.toRoute<Screen.Articulo>().articuloId
            ArticuloScreen(
                articuloId = articuloId,
                goBackToList = { navHostController.navigateUp() }
            )
        }
    }
}