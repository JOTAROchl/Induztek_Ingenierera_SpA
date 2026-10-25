package cl.duocuc.induztekspa.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import cl.duocuc.induztekspa.ui.theme.Dimens
import kotlinx.coroutines.launch

enum class PantallaHome {
    INICIO,
    LISTA_PROTOCOLOS,
    FORMULARIO_PROTOCOLO,
    MI_PERFIL,
    GESTION_USUARIOS
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onLogout: () -> Unit) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val sharedPref = remember { context.getSharedPreferences("perfil_prefs", android.content.Context.MODE_PRIVATE) }
    val rolUsuarioActual = sharedPref.getString("rol", "Operador") ?: "Operador"
    val nombreUsuarioActual = sharedPref.getString("nombre", "") ?: ""
    val correoUsuarioActual = sharedPref.getString("correo", "") ?: ""
    
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var pantallaActual by remember { mutableStateOf(PantallaHome.INICIO) }
    var protocoloAEditar by remember { mutableStateOf<cl.duocuc.induztekspa.model.Protocolo?>(null) }
    
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(Dimens.paddingLarge))
                Text(
                    text = "Menú Principal",
                    modifier = Modifier.padding(Dimens.paddingMedium),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(Dimens.paddingMedium))
                NavigationDrawerItem(
                    label = { Text(text = "Inicio") },
                    selected = pantallaActual == PantallaHome.INICIO,
                    onClick = {
                        pantallaActual = PantallaHome.INICIO
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(horizontal = Dimens.paddingMedium)
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.AutoMirrored.Filled.ListAlt, contentDescription = null) },
                    label = { Text(text = if (rolUsuarioActual == "Jefe técnico") "Gestión de Protocolos" else "Mis Protocolos") },
                    selected = pantallaActual == PantallaHome.LISTA_PROTOCOLOS,
                    onClick = {
                        pantallaActual = PantallaHome.LISTA_PROTOCOLOS
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(horizontal = Dimens.paddingMedium)
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.AddCircle, contentDescription = null) },
                    label = { Text(text = "Nuevo Protocolo") },
                    selected = pantallaActual == PantallaHome.FORMULARIO_PROTOCOLO,
                    onClick = {
                        protocoloAEditar = null
                        pantallaActual = PantallaHome.FORMULARIO_PROTOCOLO
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(horizontal = Dimens.paddingMedium)
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text(text = "Mi Perfil") },
                    selected = pantallaActual == PantallaHome.MI_PERFIL,
                    onClick = {
                        pantallaActual = PantallaHome.MI_PERFIL
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(horizontal = Dimens.paddingMedium)
                )
                if (rolUsuarioActual == "Jefe técnico") {
                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.ManageAccounts, contentDescription = null) },
                        label = { Text(text = "Gestión de Usuarios") },
                        selected = pantallaActual == PantallaHome.GESTION_USUARIOS,
                        onClick = {
                            pantallaActual = PantallaHome.GESTION_USUARIOS
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(horizontal = Dimens.paddingMedium)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                NavigationDrawerItem(
                    icon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null) },
                    label = { Text(text = "Cerrar Sesión") },
                    selected = false,
                    onClick = {
                        sharedPref.edit().clear().apply()
                        scope.launch { drawerState.close() }
                        onLogout()
                    },
                    modifier = Modifier.padding(horizontal = Dimens.paddingMedium, vertical = Dimens.paddingLarge)
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { 
                        Text(
                            text = when(pantallaActual) {
                                PantallaHome.INICIO -> "Inicio"
                                PantallaHome.LISTA_PROTOCOLOS -> if (rolUsuarioActual == "Jefe técnico") "Gestión de Protocolos" else "Mis Protocolos"
                                PantallaHome.FORMULARIO_PROTOCOLO -> if(protocoloAEditar == null) "Registro de Inspección" else "Editando Inspección"
                                PantallaHome.MI_PERFIL -> "Mi Perfil"
                                PantallaHome.GESTION_USUARIOS -> "Gestión de Usuarios"
                            }
                        ) 
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                painter = painterResource(id = android.R.drawable.ic_menu_sort_by_size),
                                contentDescription = null
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (pantallaActual) {
                    PantallaHome.INICIO -> {
                        InicioScreen(
                            nombreUsuario = nombreUsuarioActual,
                            rolUsuario = rolUsuarioActual,
                            onNavigate = { nuevaPantalla ->
                                if (nuevaPantalla == PantallaHome.FORMULARIO_PROTOCOLO) {
                                    protocoloAEditar = null
                                }
                                pantallaActual = nuevaPantalla
                            }
                        )
                    }
                    PantallaHome.LISTA_PROTOCOLOS -> {
                        ProtocolosListScreen(
                            correoUsuario = correoUsuarioActual,
                            rolUsuario = rolUsuarioActual,
                            onEdit = { protocoloSeleccionado ->
                                protocoloAEditar = protocoloSeleccionado
                                pantallaActual = PantallaHome.FORMULARIO_PROTOCOLO
                            }
                        )
                    }
                    PantallaHome.FORMULARIO_PROTOCOLO -> {
                        ProtocoloFormScreen(
                            correoUsuario = correoUsuarioActual,
                            protocoloAEditar = protocoloAEditar,
                            onGuardadoExitoso = {
                                pantallaActual = PantallaHome.LISTA_PROTOCOLOS
                            }
                        )
                    }
                    PantallaHome.MI_PERFIL -> {
                        PerfilScreen()
                    }
                    PantallaHome.GESTION_USUARIOS -> {
                        GestionUsuariosScreen()
                    }
                }
            }
        }
    }
}