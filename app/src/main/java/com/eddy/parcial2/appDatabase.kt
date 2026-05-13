package com.example.roomapp

import android.content.Context
import androidx.room.*
import com.eddy.parcial2.Movimiento
import com.eddy.parcial2.ProyectDaos.MovimientoDao
import com.eddy.parcial2.ProyectDaos.gastosDao
import com.eddy.parcial2.ProyectDaos.ingresosDao
import com.eddy.parcial2.models.Gastos
import com.eddy.parcial2.models.Ingresos
@Database([Movimiento::class, Gastos::class, Ingresos::class], version = 1)
abstract class AppDatabase : RoomDatabase(){
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
                    "movimientos_database"
                ).build()

                INSTANCE = instance

                instance
            }
        }
    }

}