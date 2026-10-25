package cl.duocuc.induztekspa.model

data class Protocolo(
    val id: Int = 0,
    val equipo: String, // TAG del equipo (Ej: TR-01)
    val fecha: String,
    val estado: String, // Borrador, Registrado en terreno, Formalizado, etc.
    val observaciones: String,
    val correoUsuario: String,
    val temperatura: String = "",
    val humedad: String = ""
)