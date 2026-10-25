package cl.duocuc.induztekspa.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cl.duocuc.induztekspa.R
import cl.duocuc.induztekspa.data.UsuarioDbHelper
import cl.duocuc.induztekspa.ui.components.BotonPrincipal
import cl.duocuc.induztekspa.ui.components.CampoContrasena
import cl.duocuc.induztekspa.ui.components.CampoTexto
import cl.duocuc.induztekspa.ui.theme.Dimens
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegistro: () -> Unit
) {
    val context = LocalContext.current
    val dbHelper = remember { UsuarioDbHelper(context) }
    val focusManager = LocalFocusManager.current

    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var hasError by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    
    val errorMessageEmpty = stringResource(id = R.string.login_error_empty)
    val errorMessageInvalid = stringResource(id = R.string.login_error_invalid)

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
            Image(
                painter = painterResource(id = R.drawable.logo_induztek),
                contentDescription = null,
                modifier = Modifier.size(150.dp)
            )

            Spacer(modifier = Modifier.height(Dimens.paddingLarge))

            Text(
                text = stringResource(id = R.string.login_title),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            
            Text(
                text = stringResource(id = R.string.login_subtitle),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(Dimens.paddingExtraLarge))

            CampoTexto(
                label = stringResource(id = R.string.username_label),
                value = correo,
                onValueChange = { 
                    correo = it
                    hasError = false
                },
                isError = hasError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )

            Spacer(modifier = Modifier.height(Dimens.paddingMedium))

            CampoContrasena(
                label = stringResource(id = R.string.password_label),
                value = password,
                onValueChange = { 
                    password = it
                    hasError = false
                },
                isError = hasError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                )
            )

            Spacer(modifier = Modifier.height(Dimens.paddingExtraLarge))

            BotonPrincipal(
                text = stringResource(id = R.string.login_button),
                onClick = {
                    if (correo.isBlank() || password.isBlank()) {
                        hasError = true
                        scope.launch {
                            snackbarHostState.showSnackbar(errorMessageEmpty)
                        }
                    } else {
                        val correoLimpio = correo.trim().lowercase()
                        
                        val usuarioLogueado = dbHelper.autenticarYObtenerUsuario(correoLimpio, password)
                        
                        if (usuarioLogueado != null) {
                            val sharedPref = context.getSharedPreferences("perfil_prefs", android.content.Context.MODE_PRIVATE)
                            sharedPref.edit()
                                .putString("nombre", usuarioLogueado.nombre)
                                .putString("correo", usuarioLogueado.correo)
                                .putString("rol", usuarioLogueado.rol)
                                .apply()

                            onLoginSuccess()
                        } else {
                            hasError = true
                            scope.launch {
                                snackbarHostState.showSnackbar(errorMessageInvalid)
                            }
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(Dimens.paddingMedium))

            TextButton(onClick = onNavigateToRegistro) {
                Text(text = "¿No tienes cuenta? Regístrate aquí")
            }
        }
    }
}