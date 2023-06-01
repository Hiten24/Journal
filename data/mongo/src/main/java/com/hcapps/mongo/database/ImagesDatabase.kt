package com.hcapps.mongo.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ImageToUploadDao::class],
    version = 1,
    exportSchema = false
)
abstract class ImageDatabase: RoomDatabase() {
    abstract fun imageToUploadDao(): ImageToUploadDao
}
