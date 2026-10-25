package cl.duocuc.induztekspa.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import cl.duocuc.induztekspa.data.ProtocoloDbHelper
import cl.duocuc.induztekspa.data.UsuarioDbHelper

class PerfilViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPref = application.getSharedPreferences("perfil_prefs", Context.MODE_PRIVATE)
    private val usuarioDbHelper = UsuarioDbHelper(application)
    private val protocoloDbHelper = ProtocoloDbHelper(application)

    fun obtenerDato(clave: String, valorPorDefecto: String): String {
        return sharedPref.getString(clave, valorPorDefecto) ?: valorPorDefecto
    }

    fun guardarPerfilCompleto(
        correoOriginal: String,
        correoNuevo: String,
        descripcion: String,
        genero: String,
        fechaNac: String,
        avatarBase64: String,
        bannerBase64: String
    ): Boolean {
        val nuevoCorreoLimpio = correoNuevo.trim().lowercase()

        if (nuevoCorreoLimpio != correoOriginal) {
            val actualizado = usuarioDbHelper.actualizarCorreo(correoOriginal, nuevoCorreoLimpio)
            if (actualizado) {
                protocoloDbHelper.actualizarCorreoUsuario(correoOriginal, nuevoCorreoLimpio)
            } else {
                return false
            }
        }

        sharedPref.edit()
            .putString("correo", nuevoCorreoLimpio)
            .putString("desc", descripcion)
            .putString("genero", genero)
            .putString("fecha_nac", fechaNac)
            .putString("avatar", avatarBase64)
            .putString("banner", bannerBase64)
            .apply()

        return true
    }
}