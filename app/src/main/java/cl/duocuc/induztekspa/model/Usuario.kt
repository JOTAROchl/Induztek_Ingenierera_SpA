package cl.duocuc.induztekspa.model

data class Usuario(
    val id: Int = 0,
    val nombre: String,
    val correo: String,
    val contrasena: String,
    val rol: String = "Técnico" // Cambiado al rol base según el documento
)