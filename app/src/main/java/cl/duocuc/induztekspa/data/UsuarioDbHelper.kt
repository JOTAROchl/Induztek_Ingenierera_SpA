package cl.duocuc.induztekspa.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import cl.duocuc.induztekspa.model.Usuario

class UsuarioDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 2
        private const val DATABASE_NAME = "Induztek.db"

        const val TABLE_USUARIOS = "usuarios"
        const val COLUMN_ID = "id"
        const val COLUMN_NOMBRE = "nombre"
        const val COLUMN_CORREO = "correo"
        const val COLUMN_CONTRASENA = "contrasena"
        const val COLUMN_ROL = "rol"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE $TABLE_USUARIOS ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_NOMBRE TEXT,"
                + "$COLUMN_CORREO TEXT,"
                + "$COLUMN_CONTRASENA TEXT,"
                + "$COLUMN_ROL TEXT)")
        db.execSQL(createTable)

        val adminValues = ContentValues().apply {
            put(COLUMN_NOMBRE, "Administrador Principal")
            put(COLUMN_CORREO, "admin@induztek.cl")
            put(COLUMN_CONTRASENA, "Admin123!")
            put(COLUMN_ROL, "Jefe técnico")
        }
        db.insert(TABLE_USUARIOS, null, adminValues)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USUARIOS")
        onCreate(db)
    }

    fun insertarUsuario(usuario: Usuario): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOMBRE, usuario.nombre)
            put(COLUMN_CORREO, usuario.correo)
            put(COLUMN_CONTRASENA, usuario.contrasena)
            put(COLUMN_ROL, usuario.rol)
        }
        val id = db.insert(TABLE_USUARIOS, null, values)
        db.close()
        return id
    }

    fun autenticarYObtenerUsuario(correo: String, contrasena: String): Usuario? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_USUARIOS,
            null,
            "$COLUMN_CORREO = ? AND $COLUMN_CONTRASENA = ?",
            arrayOf(correo, contrasena),
            null, null, null
        )
        var usuario: Usuario? = null
        if (cursor.moveToFirst()) {
            usuario = Usuario(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE)),
                correo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CORREO)),
                contrasena = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTRASENA)),
                rol = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROL))
            )
        }
        cursor.close()
        db.close()
        return usuario
    }

    fun obtenerUsuarios(): List<Usuario> {
        val lista = mutableListOf<Usuario>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_USUARIOS ORDER BY $COLUMN_ROL ASC", null)
        if (cursor.moveToFirst()) {
            do {
                lista.add(
                    Usuario(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        nombre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE)),
                        correo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CORREO)),
                        contrasena = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTRASENA)),
                        rol = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROL))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }

    fun actualizarRol(id: Int, nuevoRol: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply { put(COLUMN_ROL, nuevoRol) }
        val filasAfectadas = db.update(TABLE_USUARIOS, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
        return filasAfectadas > 0
    }

    fun actualizarCorreo(correoAntiguo: String, nuevoCorreo: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply { put(COLUMN_CORREO, nuevoCorreo) }
        val filasAfectadas = db.update(TABLE_USUARIOS, values, "$COLUMN_CORREO = ?", arrayOf(correoAntiguo))
        db.close()
        return filasAfectadas > 0
    }

    fun eliminarUsuario(id: Int): Boolean {
        val db = this.writableDatabase
        val filasAfectadas = db.delete(TABLE_USUARIOS, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
        return filasAfectadas > 0
    }
}