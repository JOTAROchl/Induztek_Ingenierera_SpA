package cl.duocuc.induztekspa.utils

object Validaciones {

    fun validarNombre(nombre: String): String? {
        if (nombre.trim().length < 3) {
            return "El nombre debe tener al menos 3 letras."
        }
        val regex = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$".toRegex()
        if (!nombre.matches(regex)) {
            return "El nombre no puede contener números ni caracteres especiales."
        }
        return null
    }

    fun validarCorreo(correo: String): String? {
        if (correo.isBlank()) {
            return "El correo electrónico es obligatorio."
        }
        val correoLimpio = correo.trim().lowercase()
        if (!correoLimpio.endsWith("@gmail.com") && !correoLimpio.endsWith("@induztek.cl") && !correoLimpio.endsWith(".ejemplo")) {
            return "Dominio no permitido. Use @induztek.cl, @gmail.com o .ejemplo"
        }
        return null
    }

    fun validarContrasena(contrasena: String): String? {
        if (contrasena.length < 6) {
            return "La contraseña debe tener mínimo 6 caracteres."
        }
        if (!contrasena.contains(Regex("[A-Z]"))) {
            return "La contraseña debe contener al menos 1 mayúscula."
        }
        if (!contrasena.contains(Regex("[a-z]"))) {
            return "La contraseña debe contener al menos 1 minúscula."
        }
        if (!contrasena.contains(Regex("[^A-Za-z0-9]"))) {
            return "La contraseña debe contener al menos 1 carácter especial."
        }
        return null
    }

    /**
     * Valida un RUT chileno (RN-03 de Induztek SpA)
     * Formatos aceptados: 12345678-9, 12.345.678-9
     */
    fun validarRUT(rutOriginal: String): String? {
        var rut = rutOriginal.replace(".", "").replace("-", "").trim().uppercase()
        
        if (rut.length < 8) return "RUT demasiado corto"
        
        val rutNumeros = rut.substring(0, rut.length - 1)
        val dvIngresado = rut.substring(rut.length - 1)
        
        try {
            var m = 0
            var s = 1
            var t = rutNumeros.toInt()
            while (t != 0) {
                s = (s + t % 10 * (9 - m++ % 6)) % 11
                t /= 10
            }
            val dvCalculado = if (s > 0) (s - 1).toString() else "K"
            
            if (dvCalculado != dvIngresado) {
                return "Dígito verificador incorrecto"
            }
        } catch (e: Exception) {
            return "Formato de RUT inválido"
        }
        return null
    }
}
