package cl.duocuc.induztekspa.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import cl.duocuc.induztekspa.data.UsuarioDbHelper
import cl.duocuc.induztekspa.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UsuariosViewModel(application: Application) : AndroidViewModel(application) {
    private val dbHelper = UsuarioDbHelper(application)
    
    private val _usuarios = MutableStateFlow<List<Usuario>>(emptyList())
    val usuarios: StateFlow<List<Usuario>> = _usuarios.asStateFlow()

    fun cargarUsuarios() {
        _usuarios.value = dbHelper.obtenerUsuarios()
    }

    fun actualizarRol(id: Int, nuevoRol: String) {
        dbHelper.actualizarRol(id, nuevoRol)
        cargarUsuarios()
    }

    fun eliminarUsuario(id: Int) {
        dbHelper.eliminarUsuario(id)
        cargarUsuarios()
    }
}