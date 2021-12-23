package com.zaelani.submission3.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favorite: Favorite)

    @Update
    fun update(favorite: Favorite)

    @Query("DELETE FROM favorite WHERE username = :username")
    fun delete(username : String?)

    @Query("SELECT * from favorite ORDER BY id ASC")
    fun getAllFavorite(): LiveData<List<Favorite>>
}
