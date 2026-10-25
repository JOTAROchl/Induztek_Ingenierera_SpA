package cl.duocuc.induztekspa.ui.screens

import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duocuc.induztekspa.model.Protocolo
import cl.duocuc.induztekspa.ui.components.BotonPrincipal
import cl.duocuc.induztekspa.ui.components.CampoTexto
import cl.duocuc.induztekspa.ui.theme.Dimens
import cl.duocuc.induztekspa.viewmodel.ProtocolosViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProtocoloFormScreen(
    correoUsuario: String,
    protocoloAEditar: Protocolo?,
    onGuardadoExitoso: () -> Unit,
    viewModel: ProtocolosViewModel = viewModel()
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    var equipo by remember { mutableStateOf(protocoloAEditar?.equipo ?: "") }
    var fecha by remember { mutableStateOf(protocoloAEditar?.fecha ?: "") }
    var estado by remember { mutableStateOf(protocoloAEditar?.estado ?: "") }
    var observaciones by remember { mutableStateOf(protocoloAEditar?.observaciones ?: "") }

    var hasError by remember { mutableStateOf(false) }

    var mostrarCalendario by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    if (mostrarCalendario) {
        DatePickerDialog(
            onDismissRequest = { mostrarCalendario = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        fecha = sdf.format(Date(millis))
                    }
                    mostrarCalendario = false
                }) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(onClick = { mostrarCalendario = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
            .padding(Dimens.paddingMedium)
    ) {
        Text(
            text = if (protocoloAEditar == null) "Nuevo Protocolo" else "Editar Protocolo",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Complete los datos de la inspección",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(Dimens.paddingLarge))

        CampoTexto(
            label = "Nombre del Equipo / Maquinaria *",
            value = equipo,
            onValueChange = { 
                equipo = it
                hasError = false
            },
            isError = hasError && equipo.isBlank(),
            errorMessage = "Campo obligatorio"
        )
        
        Spacer(modifier = Modifier.height(Dimens.paddingMedium))

        Box {
            OutlinedTextField(
                value = fecha,
                onValueChange = { },
                readOnly = true,
                label = { Text("Fecha de Inspección *") },
                modifier = Modifier.fillMaxWidth(),
                isError = hasError && fecha.isBlank(),
                trailingIcon = {
                    IconButton(onClick = { mostrarCalendario = true }) {
                        Icon(imageVector = Icons.Default.DateRange, contentDescription = "Seleccionar Fecha")
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(Dimens.paddingMedium))

        // El estado ahora es un dropdown en vez de un texto libre (según RN-04)
        var expandedEstado by remember { mutableStateOf(false) }
        val opcionesEstado = listOf("Borrador", "Registrado en terreno", "Observado", "Formalizado", "Anulado")

        ExposedDropdownMenuBox(
            expanded = expandedEstado,
            onExpandedChange = { expandedEstado = !expandedEstado }
        ) {
            OutlinedTextField(
                value = estado,
                onValueChange = {},
                readOnly = true,
                label = { Text("Estado del Protocolo *") },
                isError = hasError && estado.isBlank(),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedEstado) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expandedEstado,
                onDismissRequest = { expandedEstado = false }
            ) {
                opcionesEstado.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion) },
                        onClick = {
                            estado = opcion
                            expandedEstado = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(Dimens.paddingMedium))

        CampoTexto(
            label = "Observaciones",
            value = observaciones,
            onValueChange = { observaciones = it }
        )

        Spacer(modifier = Modifier.height(Dimens.paddingExtraLarge))

        BotonPrincipal(
            text = if (protocoloAEditar == null) "Guardar Protocolo" else "Actualizar Protocolo",
            onClick = {
                if (equipo.isBlank() || fecha.isBlank() || estado.isBlank()) {
                    hasError = true
                } else {
                    val protocoloFinal = Protocolo(
                        id = protocoloAEditar?.id ?: 0,
                        equipo = equipo,
                        fecha = fecha,
                        estado = estado,
                        observaciones = observaciones,
                        correoUsuario = protocoloAEditar?.correoUsuario ?: correoUsuario
                    )

                    val esNuevo = protocoloAEditar == null
                    val exito = viewModel.guardarProtocolo(protocoloFinal, esNuevo)

                    if (exito) {
                        Toast.makeText(context, "Protocolo guardado", Toast.LENGTH_SHORT).show()
                        onGuardadoExitoso()
                    } else {
                        Toast.makeText(context, "Error al guardar", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )
    }
}