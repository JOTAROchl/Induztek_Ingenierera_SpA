package cl.duocuc.induztekspa.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import cl.duocuc.induztekspa.data.ProtocoloDbHelper
import cl.duocuc.induztekspa.model.Protocolo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProtocolosViewModel(application: Application) : AndroidViewModel(application) {
    private val dbHelper = ProtocoloDbHelper(application)
    
    private val _protocolos = MutableStateFlow<List<Protocolo>>(emptyList())
    val protocolos: StateFlow<List<Protocolo>> = _protocolos.asStateFlow()

    fun cargarProtocolos(correoUsuario: String, rol: String) {
        _protocolos.value = dbHelper.obtenerProtocolos(correoUsuario, rol)
    }

    fun guardarProtocolo(protocolo: Protocolo, esNuevo: Boolean): Boolean {
        return if (esNuevo) {
            dbHelper.insertarProtocolo(protocolo) > -1
        } else {
            dbHelper.actualizarProtocolo(protocolo)
        }
    }

    fun eliminarProtocolo(id: Int, correoUsuario: String, rol: String) {
        dbHelper.eliminarProtocolo(id)
        cargarProtocolos(correoUsuario, rol)
    }
}