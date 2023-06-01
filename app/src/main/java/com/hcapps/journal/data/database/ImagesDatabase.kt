package com.hcapps.journal.data.database

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

/*
@Database(
    entities = [ImageToUpload::class],
    version = 1,
    exportSchema = false
)
abstract class ImagesDatabase: RoomDatabase() {
    abstract fun imageToUploadDao(): ImageToUploadDao
}*/
