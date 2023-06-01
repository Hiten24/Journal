package com.hcapps.journal.di

import android.content.Context
import androidx.room.Room
import com.hcapps.journal.data.database.ImageDatabase
import com.hcapps.journal.data.database.ImageToUploadDao
import com.hcapps.journal.data.database.entity.ImageToUpload
import com.hcapps.journal.util.Constants.IMAGES_DATABASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): ImageDatabase {
        return Room.databaseBuilder(
            context,
            ImageDatabase::class.java,
            IMAGES_DATABASE
        ).build()
    }

    @Provides
    @Singleton
    fun provideImageToUploadDao(database: ImageDatabase): ImageToUploadDao = database.imageToUploadDao()

}