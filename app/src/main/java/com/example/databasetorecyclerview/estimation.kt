package com.example.databasetorecyclerview

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "estimation_details")
 data class estimation (

    @PrimaryKey(autoGenerate = true)
    val id : Int?,

    @ColumnInfo(name = "customer_mobile")
    val customermobile : String?,

    @ColumnInfo(name = "customer_name")
    val customername : String?,

    @ColumnInfo(name = "customer_address")
    val customeraddress : String?,

    @ColumnInfo(name = "project_name")
    val projectname : String?

)