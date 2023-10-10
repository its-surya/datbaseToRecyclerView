package com.example.databasetorecyclerview

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [estimation :: class], version = 1)
abstract class estimationDatabase : RoomDatabase() {

    abstract fun EstimationDao() : estimationDao

    companion object{
        @Volatile
        private var INSTANCE: estimationDatabase?= null

        fun getDataBase(context : Context) : estimationDatabase{

            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance  = Room.databaseBuilder(
                    context.applicationContext,
                    estimationDatabase :: class.java ,
                    "App_Database"
                ).build()

                INSTANCE = instance
                return instance

            }

        }


    }



}