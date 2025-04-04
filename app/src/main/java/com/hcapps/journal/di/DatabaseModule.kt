package com.hcapps.journal.di

import android.content.Context
import androidx.room.Room
import com.hcapps.mongo.database.ImageDatabase
import com.hcapps.mongo.database.ImageToUploadDao
import com.hcapps.util.Constants.IMAGES_DATABASE
import com.hcapps.util.connectivity.NetworkConnectivityObserver
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

    @Singleton
    @Provides
    fun provideNetworkConnectivityObserver(
        @ApplicationContext context: Context
    ) = NetworkConnectivityObserver(context = context)

}