package com.hcapps.journal.di

import android.content.Context
import androidx.room.Room
import com.hcapps.journal.data.database.ImageToUploadDao
import com.hcapps.journal.data.database.ImagesDatabase
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

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): ImagesDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = ImagesDatabase::class.java,
            name = IMAGES_DATABASE
        ).build()
    }

    @Singleton
    @Provides
    fun provideImageToUploadDao(database: ImagesDatabase): ImageToUploadDao = database.imageToUploadDao()

}