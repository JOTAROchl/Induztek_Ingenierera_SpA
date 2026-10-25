package cl.duocuc.induztekspa.ui.screens

import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import cl.duocuc.induztekspa.data.UsuarioDbHelper
import cl.duocuc.induztekspa.model.Usuario
import cl.duocuc.induztekspa.ui.components.BotonPrincipal
import cl.duocuc.induztekspa.ui.components.CampoContrasena
import cl.duocuc.induztekspa.ui.components.CampoTexto
import cl.duocuc.induztekspa.ui.theme.Dimens
import cl.duocuc.induztekspa.utils.Validaciones
import kotlinx.coroutines.launch

@Composable
fun RegistroScreen(
    onNavigateBack: () -> Unit,
    onRegistroSuccess: () -> Unit
) {
    val context = LocalContext.current
    val dbHelper = remember { UsuarioDbHelper(context) }
    val focusManager = LocalFocusManager.current
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var errorNombre by remember { mutableStateOf<String?>(null) }
    var errorCorreo by remember { mutableStateOf<String?>(null) }
    var errorContrasena by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(Dimens.paddingLarge)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Crear Cuenta",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Regístrate en Induztek SpA",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(Dimens.paddingExtraLarge))
            CampoTexto(
                label = "Nombre Completo *",
                value = nombre,
                onValueChange = { 
                    nombre = it
                    errorNombre = null
                },
                isError = errorNombre != null,
                errorMessage = errorNombre,
                modifier = Modifier.onFocusChanged { focusState ->
                    if (!focusState.isFocused && nombre.isNotEmpty()) {
                        errorNombre = Validaciones.validarNombre(nombre)
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        errorNombre = Validaciones.validarNombre(nombre)
                        if (errorNombre == null) focusManager.moveFocus(FocusDirection.Down)
                    }
                )
            )
            Spacer(modifier = Modifier.height(Dimens.paddingMedium))
            CampoTexto(
                label = "Correo Electrónico *",
                value = correo,
                onValueChange = { 
                    correo = it
                    errorCorreo = null
                },
                isError = errorCorreo != null,
                errorMessage = errorCorreo,
                modifier = Modifier.onFocusChanged { focusState ->
                    if (!focusState.isFocused && correo.isNotEmpty()) {
                        errorCorreo = Validaciones.validarCorreo(correo)
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        errorCorreo = Validaciones.validarCorreo(correo)
                        if (errorCorreo == null) focusManager.moveFocus(FocusDirection.Down)
                    }
                )
            )
            Spacer(modifier = Modifier.height(Dimens.paddingMedium))
            CampoContrasena(
                label = "Contraseña *",
                value = contrasena,
                onValueChange = { 
                    contrasena = it
                    errorContrasena = null
                },
                isError = errorContrasena != null,
                errorMessage = errorContrasena,
                modifier = Modifier.onFocusChanged { focusState ->
                    if (!focusState.isFocused && contrasena.isNotEmpty()) {
                        errorContrasena = Validaciones.validarContrasena(contrasena)
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        errorContrasena = Validaciones.validarContrasena(contrasena)
                        if (errorContrasena == null) focusManager.clearFocus()
                    }
                )
            )
            Spacer(modifier = Modifier.height(Dimens.paddingExtraLarge))
            BotonPrincipal(
                text = "Registrarme",
                onClick = {
                    errorNombre = Validaciones.validarNombre(nombre)
                    errorCorreo = Validaciones.validarCorreo(correo)
                    errorContrasena = Validaciones.validarContrasena(contrasena)
                    if (errorNombre == null && errorCorreo == null && errorContrasena == null) {
                        val nuevoUsuario = Usuario(
                            nombre = nombre.trim(),
                            correo = correo.trim().lowercase(),
                            contrasena = contrasena
                        )
                        val id = dbHelper.insertarUsuario(nuevoUsuario)
                        if (id > -1) {
                            Toast.makeText(context, "Cuenta creada exitosamente", Toast.LENGTH_SHORT).show()
                            onRegistroSuccess()
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar("Error al crear la cuenta en base de datos")
                            }
                        }
                    }
                }
            )
            Spacer(modifier = Modifier.height(Dimens.paddingMedium))
            TextButton(onClick = onNavigateBack) {
                Text(text = "¿Ya tienes cuenta? Inicia Sesión")
            }
        }
    }
}