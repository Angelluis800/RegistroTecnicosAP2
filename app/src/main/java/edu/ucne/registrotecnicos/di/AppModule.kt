package edu.ucne.registrotecnicos.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.ucne.registrotecnicos.data.local.database.AppDataDb
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    @Singleton
    fun provideAppDataDb(@ApplicationContext context : Context) : AppDataDb =
        Room.databaseBuilder(
            context,
            AppDataDb::class.java,
            "AppDataDb"
        ).fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideTicketDao(appDataDb: AppDataDb) = appDataDb.ticketDao()

    @Provides
    fun provideTecnicoDao(appDataDb: AppDataDb) = appDataDb.technicianDao()

    @Provides
    fun provideMensajeDao(appDataDb: AppDataDb) = appDataDb.mensajeDao()

    @Provides
    fun provideArticuloDao(appDataDb: AppDataDb) = appDataDb.articuloDao()
}