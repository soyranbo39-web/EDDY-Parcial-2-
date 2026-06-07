package com.eddy.parcial2.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.eddy.parcial2.data.interfaces.CategoriaDao
import com.eddy.parcial2.data.interfaces.MovimientoDao
import com.eddy.parcial2.data.interfaces.UserDao
import com.eddy.parcial2.data.models.User
import com.eddy.parcial2.models.Categoria
import com.eddy.parcial2.models.Movimiento
import com.eddy.parcial2.Pantalla9.interfaces.CuentaDao
import com.eddy.parcial2.Pantalla9.models.Cuenta


@Database(
    entities = [User::class, Movimiento::class, Categoria::class, Cuenta::class],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun movimientoDao(): MovimientoDao
    abstract fun categoriaDao(): CategoriaDao
    abstract fun cuentaDao(): CuentaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
