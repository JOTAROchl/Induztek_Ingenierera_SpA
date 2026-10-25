package cl.duocuc.induztekspa.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duocuc.induztekspa.permitions.Permisos
import cl.duocuc.induztekspa.ui.components.AvatarCircular
import cl.duocuc.induztekspa.ui.components.BotonPrincipal
import cl.duocuc.induztekspa.ui.theme.Dimens
import cl.duocuc.induztekspa.utils.ConvertidorImagen
import cl.duocuc.induztekspa.viewmodel.PerfilViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    viewModel: PerfilViewModel = viewModel()
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    
    var nombre by remember { mutableStateOf(viewModel.obtenerDato("nombre", "Usuario Desconocido")) }
    var rol by remember { mutableStateOf(viewModel.obtenerDato("rol", "Operador")) }
    var correoOriginal by remember { mutableStateOf(viewModel.obtenerDato("correo", "")) }
    
    var correoEditable by remember { mutableStateOf(correoOriginal) }
    var descripcion by remember { mutableStateOf(viewModel.obtenerDato("desc", "Ingeniero de terreno en Induztek SpA.")) }
    var avatarBase64 by remember { mutableStateOf(viewModel.obtenerDato("avatar", "")) }
    var bannerBase64 by remember { mutableStateOf(viewModel.obtenerDato("banner", "")) }
    var genero by remember { mutableStateOf(viewModel.obtenerDato("genero", "No especificado")) }
    var fechaNacimiento by remember { mutableStateOf(viewModel.obtenerDato("fecha_nac", "")) }

    var isEditing by remember { mutableStateOf(false) }

    var mostrarCalendario by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    var expandedGenero by remember { mutableStateOf(false) }
    val opcionesGenero = listOf("Masculino", "Femenino", "Otro", "Prefiero no decirlo")

    val launcherCamara = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap != null) {
            avatarBase64 = ConvertidorImagen.bitmapABase64(bitmap)
        }
    }
    
    val launcherPermisoCamara = Permisos.rememberCameraPermissionLauncher(
        onGranted = { launcherCamara.launch(null) },
        onDenied = { Toast.makeText(context, "Se requiere permiso de cámara para tomar fotos", Toast.LENGTH_LONG).show() }
    )

    val launcherGaleria = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            val bitmap = android.provider.MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            avatarBase64 = ConvertidorImagen.bitmapABase64(bitmap)
        }
    }

    val launcherBanner = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            val bitmap = android.provider.MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            bannerBase64 = ConvertidorImagen.bitmapABase64(bitmap)
        }
    }

    if (mostrarCalendario) {
        DatePickerDialog(
            onDismissRequest = { mostrarCalendario = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        fechaNacimiento = sdf.format(Date(millis))
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
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(MaterialTheme.colorScheme.tertiaryContainer)
            ) {
                val bannerBitmap = ConvertidorImagen.base64ABitmap(bannerBase64)
                if (bannerBitmap != null) {
                    Image(
                        bitmap = bannerBitmap.asImageBitmap(),
                        contentDescription = "Banner",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                
                if (isEditing) {
                    IconButton(
                        onClick = { launcherBanner.launch("image/*") },
                        modifier = Modifier.align(Alignment.TopEnd).padding(8.dp)
                    ) {
                        Icon(Icons.Default.Image, contentDescription = "Cambiar Banner", tint = Color.White)
                    }
                }
            }
            
            Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                AvatarCircular(
                    bitmap = ConvertidorImagen.base64ABitmap(avatarBase64),
                    modifier = Modifier.size(100.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = nombre,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = rol,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(Dimens.paddingLarge))

        if (!isEditing) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.paddingMedium),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(Dimens.paddingLarge)) {
                    InfoRow(icono = Icons.Default.Email, titulo = "Correo", valor = correoOriginal)
                    InfoRow(icono = Icons.Default.Person, titulo = "Género", valor = genero)
                    InfoRow(icono = Icons.Default.DateRange, titulo = "Fecha de Nac.", valor = if (fechaNacimiento.isBlank()) "No especificada" else fechaNacimiento)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Sobre mí:", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Text(text = descripcion.ifBlank { "Sin descripción" }, style = MaterialTheme.typography.bodyMedium)
                }
            }

            Spacer(modifier = Modifier.height(Dimens.paddingExtraLarge))

            BotonPrincipal(
                text = "Modificar Perfil",
                onClick = { isEditing = true },
                modifier = Modifier.padding(horizontal = Dimens.paddingMedium)
            )
            Spacer(modifier = Modifier.height(Dimens.paddingLarge))
        } 
        else {
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = { 
                    Permisos.solicitarPermisoCamara(
                        context = context,
                        launcher = launcherPermisoCamara,
                        onGranted = { launcherCamara.launch(null) }
                    )
                }) {
                    Icon(Icons.Default.CameraAlt, contentDescription = "Cámara")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Cámara")
                }
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedButton(onClick = { launcherGaleria.launch("image/*") }) {
                    Icon(Icons.Default.PhotoLibrary, contentDescription = "Galería")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Galería")
                }
            }

            Spacer(modifier = Modifier.height(Dimens.paddingMedium))

            Column(modifier = Modifier.padding(horizontal = Dimens.paddingMedium)) {
                
                OutlinedTextField(
                    value = correoEditable,
                    onValueChange = { correoEditable = it },
                    label = { Text("Correo Electrónico *") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))

                ExposedDropdownMenuBox(
                    expanded = expandedGenero,
                    onExpandedChange = { expandedGenero = !expandedGenero }
                ) {
                    OutlinedTextField(
                        value = genero,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Género") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedGenero) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedGenero,
                        onDismissRequest = { expandedGenero = false }
                    ) {
                        opcionesGenero.forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text(opcion) },
                                onClick = {
                                    genero = opcion
                                    expandedGenero = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = fechaNacimiento,
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Fecha de Nacimiento") },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = { mostrarCalendario = true }) {
                            Icon(Icons.Default.DateRange, contentDescription = "Calendario")
                        }
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción Profesional") },
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    maxLines = 4
                )
            }

            Spacer(modifier = Modifier.height(Dimens.paddingExtraLarge))

            BotonPrincipal(
                text = "Guardar Cambios",
                onClick = {
                    val exito = viewModel.guardarPerfilCompleto(
                        correoOriginal = correoOriginal,
                        correoNuevo = correoEditable,
                        descripcion = descripcion,
                        genero = genero,
                        fechaNac = fechaNacimiento,
                        avatarBase64 = avatarBase64,
                        bannerBase64 = bannerBase64
                    )
                    
                    if (exito) {
                        correoOriginal = correoEditable.trim().lowercase()
                        isEditing = false
                        Toast.makeText(context, "Perfil guardado", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Error al guardar el perfil", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.padding(horizontal = Dimens.paddingMedium)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            TextButton(onClick = { 
                correoEditable = correoOriginal
                descripcion = viewModel.obtenerDato("desc", "")
                genero = viewModel.obtenerDato("genero", "No especificado")
                fechaNacimiento = viewModel.obtenerDato("fecha_nac", "")
                avatarBase64 = viewModel.obtenerDato("avatar", "")
                bannerBase64 = viewModel.obtenerDato("banner", "")
                isEditing = false 
            }) {
                Text("Cancelar")
            }
            Spacer(modifier = Modifier.height(Dimens.paddingLarge))
        }
    }
}

@Composable
fun InfoRow(icono: androidx.compose.ui.graphics.vector.ImageVector, titulo: String, valor: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
        Icon(icono, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "$titulo: ", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
        Text(text = valor, style = MaterialTheme.typography.bodyMedium)
    }
}