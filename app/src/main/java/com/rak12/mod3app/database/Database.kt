package com.rak12.mod3app.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = [RestaurantEntity::class, CartEntity::class], version = 3)

abstract class Database : RoomDatabase() {
    abstract fun restdao(): Dao

}

val MIGRATION_1_2: Migration = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "CREATE TABLE cartorders(foodid TEXT   Primary key NOT NULL,resid INTEGER NOT NULL,foodname TEXT  NOT NULL,price TEXT NOT NULL )"
        )
    }
}
