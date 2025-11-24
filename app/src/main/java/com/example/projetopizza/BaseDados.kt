package com.example.projetopizza

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Delete

@Entity(tableName = "utilizadores_bd")
data class UtilizadorBd(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nome: String,
    val telefone: String,
    val morada: String
)

@Dao
interface UtilizadorDao {
    @Insert
    fun insertUtilizador(utilizador: UtilizadorBd)

    @Query("SELECT * FROM utilizadores_bd ORDER BY id DESC")
    fun getAllUtilizadores(): LiveData<List<UtilizadorBd>>

    @Delete
    fun deleteUtilizador(utilizador: UtilizadorBd)
}


@Database(entities = [UtilizadorBd::class], version = 1)
abstract class PizzaDatabase : RoomDatabase() {
    abstract fun utilizadorDao(): UtilizadorDao

    companion object {
        @Volatile
        private var INSTANCE: PizzaDatabase? = null

        fun getDatabase(context: Context): PizzaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PizzaDatabase::class.java,
                    "pizza_database"
                ).fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}