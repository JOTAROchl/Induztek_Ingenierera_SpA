package cl.duocuc.induztekspa.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duocuc.induztekspa.ui.components.BotonPrincipal
import cl.duocuc.induztekspa.ui.theme.Dimens

@Composable
fun InicioScreen(
    nombreUsuario: String,
    rolUsuario: String,
    onNavigate: (PantallaHome) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.paddingLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "¡Hola, $nombreUsuario!",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Bienvenido a tu panel de $rolUsuario",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(48.dp))

        BotonPrincipal(
            text = if (rolUsuario == "Jefe técnico") "Gestión de Protocolos" else "Ver Mis Protocolos",
            icono = Icons.AutoMirrored.Filled.ListAlt,
            onClick = { onNavigate(PantallaHome.LISTA_PROTOCOLOS) }
        )
        Spacer(modifier = Modifier.height(Dimens.paddingMedium))
        
        BotonPrincipal(
            text = "Registrar Inspección",
            icono = Icons.Default.AddCircle,
            onClick = { onNavigate(PantallaHome.FORMULARIO_PROTOCOLO) }
        )
        Spacer(modifier = Modifier.height(Dimens.paddingMedium))
        
        BotonPrincipal(
            text = "Mi Perfil",
            icono = Icons.Default.Person,
            onClick = { onNavigate(PantallaHome.MI_PERFIL) }
        )

        if (rolUsuario == "Jefe técnico") {
            Spacer(modifier = Modifier.height(Dimens.paddingMedium))
            BotonPrincipal(
                text = "Gestión de Usuarios",
                icono = Icons.Default.ManageAccounts,
                onClick = { onNavigate(PantallaHome.GESTION_USUARIOS) }
            )
        }
    }
}