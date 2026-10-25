package cl.duocuc.induztekspa.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duocuc.induztekspa.model.Protocolo
import cl.duocuc.induztekspa.ui.styles.EstilosTarjeta
import cl.duocuc.induztekspa.ui.theme.Dimens
import cl.duocuc.induztekspa.viewmodel.ProtocolosViewModel

@Composable
fun ProtocolosListScreen(
    correoUsuario: String,
    rolUsuario: String,
    onEdit: (Protocolo) -> Unit,
    viewModel: ProtocolosViewModel = viewModel()
) {
    val protocolos by viewModel.protocolos.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.cargarProtocolos(correoUsuario, rolUsuario)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.paddingMedium)
    ) {
        Text(
            text = if (rolUsuario == "Jefe técnico" || rolUsuario == "Ingeniero") "Todos los Protocolos" else "Mis Protocolos",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(Dimens.paddingMedium))

        if (protocolos.isEmpty()) {
            Text(
                text = "No hay protocolos registrados aún.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            LazyColumn {
                items(protocolos) { protocolo ->
                    ProtocoloCard(
                        protocolo = protocolo,
                        onEdit = { onEdit(protocolo) },
                        onDelete = {
                            viewModel.eliminarProtocolo(protocolo.id, correoUsuario, rolUsuario)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ProtocoloCard(
    protocolo: Protocolo,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = EstilosTarjeta.modifierLista,
        shape = EstilosTarjeta.formaTarjeta,
        colors = EstilosTarjeta.coloresTarjeta()
    ) {
        Column(
            modifier = Modifier.padding(Dimens.paddingMedium)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = protocolo.equipo,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Responsable: ${protocolo.correoUsuario}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Row {
                    IconButton(onClick = onEdit) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.secondary)
                    }
                    IconButton(onClick = onDelete) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                    }
                }
            }

            Spacer(modifier = Modifier.height(Dimens.paddingSmall))
            Text(
                text = "Estado: ${protocolo.estado}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Fecha: ${protocolo.fecha}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (protocolo.observaciones.isNotBlank()) {
                Spacer(modifier = Modifier.height(Dimens.paddingSmall))
                Text(
                    text = "Obs: ${protocolo.observaciones}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}