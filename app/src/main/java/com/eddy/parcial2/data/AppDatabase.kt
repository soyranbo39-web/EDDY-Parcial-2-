package com.eddy.parcial2.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.eddy.parcial2.MovimientoContenedor
import com.eddy.parcial2.MovimientoDao
import com.eddy.parcial2.ProyectDaos.gastosDao
import com.eddy.parcial2.ProyectDaos.ingresosDao
import com.eddy.parcial2.data.interfaces.UserDao
import com.eddy.parcial2.data.models.User
import com.eddy.parcial2.models.Gastos
import com.eddy.parcial2.models.Ingresos

@Database(
    entities = [
        User::class,
        MovimientoContenedor::class,
        Gastos::class,
        Ingresos::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun movimientoDao(): MovimientoDao
    abstract fun gastosDao(): gastosDao
    abstract fun ingresosDao(): ingresosDao

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