package com.eddy.parcial2

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.security.MessageDigest

// Esta clase maneja la creación y autenticación de usuarios utilizando SQLite para almacenamiento local
class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    // En onCreate se define la estructura de la tabla de usuarios, con un campo para el correo electrónico y otro para el hash de la contraseña
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE $TABLE_USERS (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_EMAIL TEXT UNIQUE NOT NULL,
                $COL_PASSWORD_HASH TEXT NOT NULL
            )
            """.trimIndent()
        )
    }

    // En onUpgrade se elimina la tabla existente y se vuelve a crear
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }
    // Crea un usuario demo si no existe para facilitar las pruebas
    fun createDemoUserIfNeeded() {
        val demoEmail = "demo@correo.com"
        val demoPassword = "123456"

        if (!userExistsByEmail(demoEmail)) {
            insertUser(demoEmail, demoPassword)
        }
    }

    // Inserta un nuevo usuario en la base de datos, almacenando el correo en minúsculas y el hash de la contraseña para mayor seguridad
    fun insertUser(email: String, password: String): Boolean {
        val values = ContentValues().apply {
            put(COL_EMAIL, email.lowercase())
            put(COL_PASSWORD_HASH, hashPassword(password))
        }

        val result = writableDatabase.insert(TABLE_USERS, null, values)
        return result != -1L
    }

    // Autentica a un usuario verificando su correo y contraseña
    fun authenticate(email: String, password: String): Boolean {
        val selection = "$COL_EMAIL = ? AND $COL_PASSWORD_HASH = ?"
        val args = arrayOf(email.lowercase(), hashPassword(password))

        readableDatabase.query(
            TABLE_USERS,
            arrayOf(COL_ID),
            selection,
            args,
            null,
            null,
            null
        ).use { cursor ->
            return cursor.count > 0
        }
    }
    // Verifica si un usuario con el correo dado ya existe en la base de datos
    private fun userExistsByEmail(email: String): Boolean {
        readableDatabase.query(
            TABLE_USERS,
            arrayOf(COL_ID),
            "$COL_EMAIL = ?",
            arrayOf(email.lowercase()),
            null,
            null,
            null
        ).use { cursor ->
            return cursor.count > 0
        }
    }
// Genera un hash SHA-256 de la contraseña para almacenarla de forma segura en la base de datos
    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
            .digest(password.toByteArray())
        return digest.joinToString("") { "%02x".format(it) }
    }
    
    // Define constantes para el nombre de la base de datos, su versión, el nombre de la tabla y los nombres de las columnas
    companion object {
        private const val DATABASE_NAME = "usuarios.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_USERS = "users"
        private const val COL_ID = "id"
        private const val COL_EMAIL = "email"
        private const val COL_PASSWORD_HASH = "password_hash"
    }
}