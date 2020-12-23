package com.rak12.mod3app.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface Dao {
    @Insert
    fun insert(restaurantEntity: RestaurantEntity)

    @Delete
    fun delete(restaurantEntity: RestaurantEntity)

    @Query("SELECT * FROM RESTAURANT")
    fun getall(): List<RestaurantEntity>

    @Query("SELECT * FROM RESTAURANT WHERE ID=:id")
    fun getbyid(id: Int): RestaurantEntity

    @Insert
    fun insertfooditem(cartEntity: CartEntity)

    @Delete
    fun deletefooditem(cartEntity: CartEntity)

    @Query("SELECT * FROM cartorders")
    fun getallfooditem(): List<CartEntity>

    @Query("SELECT * FROM cartorders WHERE foodid=:foodid ")
    fun getbyid2(foodid: String): CartEntity


    @Query("DELETE FROM cartorders;")
    fun deleteAllEntries()

}