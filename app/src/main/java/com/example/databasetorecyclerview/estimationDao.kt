package com.example.databasetorecyclerview

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface estimationDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE )
    fun insert(estimate : estimation)

    @Query("SELECT * FROM estimation_details ")
    fun getAll() : LiveData<List<estimation>>

}