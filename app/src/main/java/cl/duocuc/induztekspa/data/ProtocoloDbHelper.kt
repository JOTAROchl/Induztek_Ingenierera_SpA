package cl.duocuc.induztekspa.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import cl.duocuc.induztekspa.model.Protocolo

class ProtocoloDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 3 // Versión 3: Se agregan T° y Humedad
        private const val DATABASE_NAME = "ProtocolosInduztek.db"

        const val TABLE_PROTOCOLOS = "protocolos"
        const val COLUMN_ID = "id"
        const val COLUMN_EQUIPO = "equipo"
        const val COLUMN_FECHA = "fecha"
        const val COLUMN_ESTADO = "estado"
        const val COLUMN_OBSERVACIONES = "observaciones"
        const val COLUMN_CORREO_USUARIO = "correo_usuario"
        const val COLUMN_TEMPERATURA = "temperatura"
        const val COLUMN_HUMEDAD = "humedad"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE $TABLE_PROTOCOLOS ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_EQUIPO TEXT,"
                + "$COLUMN_FECHA TEXT,"
                + "$COLUMN_ESTADO TEXT,"
                + "$COLUMN_OBSERVACIONES TEXT,"
                + "$COLUMN_CORREO_USUARIO TEXT,"
                + "$COLUMN_TEMPERATURA TEXT,"
                + "$COLUMN_HUMEDAD TEXT)")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PROTOCOLOS")
        onCreate(db)
    }

    fun insertarProtocolo(protocolo: Protocolo): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_EQUIPO, protocolo.equipo)
            put(COLUMN_FECHA, protocolo.fecha)
            put(COLUMN_ESTADO, protocolo.estado)
            put(COLUMN_OBSERVACIONES, protocolo.observaciones)
            put(COLUMN_CORREO_USUARIO, protocolo.correoUsuario)
            put(COLUMN_TEMPERATURA, protocolo.temperatura)
            put(COLUMN_HUMEDAD, protocolo.humedad)
        }
        val id = db.insert(TABLE_PROTOCOLOS, null, values)
        db.close()
        return id
    }

    fun actualizarProtocolo(protocolo: Protocolo): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_EQUIPO, protocolo.equipo)
            put(COLUMN_FECHA, protocolo.fecha)
            put(COLUMN_ESTADO, protocolo.estado)
            put(COLUMN_OBSERVACIONES, protocolo.observaciones)
            put(COLUMN_TEMPERATURA, protocolo.temperatura)
            put(COLUMN_HUMEDAD, protocolo.humedad)
        }
        val filas = db.update(TABLE_PROTOCOLOS, values, "$COLUMN_ID = ?", arrayOf(protocolo.id.toString()))
        db.close()
        return filas > 0
    }

    fun actualizarCorreoUsuario(correoAntiguo: String, nuevoCorreo: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply { put(COLUMN_CORREO_USUARIO, nuevoCorreo) }
        db.update(TABLE_PROTOCOLOS, values, "$COLUMN_CORREO_USUARIO = ?", arrayOf(correoAntiguo))
        db.close()
    }

    fun eliminarProtocolo(id: Int): Boolean {
        val db = this.writableDatabase
        val filas = db.delete(TABLE_PROTOCOLOS, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
        return filas > 0
    }

    fun obtenerProtocolos(correoUsuario: String, rol: String): List<Protocolo> {
        val listaProtocolos = mutableListOf<Protocolo>()
        val db = this.readableDatabase
        
        // Según RNF-06 y Permisos: Jefe técnico y Administrador ven todo. Cliente ve lo suyo (pendiente de armar tablas). Técnicos ven solo lo de ellos.
        val cursor = if (rol == "Jefe técnico" || rol == "Administrador" || rol == "Ingeniero") {
            db.rawQuery("SELECT * FROM $TABLE_PROTOCOLOS ORDER BY $COLUMN_ID DESC", null)
        } else {
            db.rawQuery("SELECT * FROM $TABLE_PROTOCOLOS WHERE $COLUMN_CORREO_USUARIO = ? ORDER BY $COLUMN_ID DESC", arrayOf(correoUsuario))
        }

        if (cursor.moveToFirst()) {
            do {
                // Safely read columns (they might be missing if DB wasn't cleanly upgraded, but we drop it on upgrade anyway)
                val tempIdx = cursor.getColumnIndex(COLUMN_TEMPERATURA)
                val humIdx = cursor.getColumnIndex(COLUMN_HUMEDAD)
                
                listaProtocolos.add(
                    Protocolo(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        equipo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EQUIPO)),
                        fecha = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA)),
                        estado = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ESTADO)),
                        observaciones = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OBSERVACIONES)),
                        correoUsuario = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CORREO_USUARIO)),
                        temperatura = if (tempIdx != -1) cursor.getString(tempIdx) ?: "" else "",
                        humedad = if (humIdx != -1) cursor.getString(humIdx) ?: "" else ""
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return listaProtocolos
    }
}