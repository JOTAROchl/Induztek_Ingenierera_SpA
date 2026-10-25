package cl.duocuc.induztekspa.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cl.duocuc.induztekspa.ui.screens.HomeScreen
import cl.duocuc.induztekspa.ui.screens.LoginScreen
import cl.duocuc.induztekspa.ui.screens.RegistroScreen

@Composable
fun AppNavegacion() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Rutas.LOGIN) {
        
        composable(Rutas.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Rutas.HOME) {
                        popUpTo(Rutas.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToRegistro = {
                    navController.navigate(Rutas.REGISTRO)
                }
            )
        }

        composable(Rutas.REGISTRO) {
            RegistroScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onRegistroSuccess = {
                    navController.popBackStack()
                }
            )
        }

        composable(Rutas.HOME) {
            HomeScreen(
                onLogout = {
                    navController.navigate(Rutas.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}