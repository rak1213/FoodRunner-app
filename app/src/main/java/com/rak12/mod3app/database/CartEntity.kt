package com.rak12.mod3app.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "cartorders")
data class CartEntity
    (
    @ColumnInfo(name = "foodid")  @PrimaryKey val foodid: String,
    @ColumnInfo(name = "resid") val resid: Int,
    @ColumnInfo(name = "foodname") val foodname: String,
    @ColumnInfo(name = "price") val price: String
)