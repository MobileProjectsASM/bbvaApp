package com.example.bbvaapp.di.modules

import com.example.data.repositories.AuthRepositoryImpl
import com.example.data.repositories.MediaRepositoryImpl
import com.example.data.sources.local.abstract_locals.AuthLocalSource
import com.example.data.sources.remote.abstract_remotes.AuthRemoteSource
import com.example.data.sources.remote.abstract_remotes.MediaRemoteSource
import com.example.domain.repositories.AuthRepository
import com.example.domain.repositories.MediaRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @ViewModelScoped
    @Binds
    abstract fun providesAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @ViewModelScoped
    @Binds
    abstract fun provideMediaRepository(mediaRepositoryImpl: MediaRepositoryImpl): MediaRepository
}