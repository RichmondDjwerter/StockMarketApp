package com.richmondprojects.stockmarketapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CompanyListingEntity::class], version = 1, exportSchema = false)
abstract class ListingsDatabase : RoomDatabase() {

    abstract val dao: CompanyListingDao
}