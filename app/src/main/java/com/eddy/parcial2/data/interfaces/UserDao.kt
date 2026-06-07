package com.eddy.parcial2.data.interfaces
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.eddy.parcial2.data.models.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: User): Long

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE email = :email)")
    suspend fun userExists(email: String): Boolean

    @Query("UPDATE users SET username = :nuevoNombre, avatar_id = :nuevoAvatarId WHERE email = :email")
    suspend fun updateNameAndAvatarByEmail(email: String, nuevoNombre: String, nuevoAvatarId: Int): Int
}
