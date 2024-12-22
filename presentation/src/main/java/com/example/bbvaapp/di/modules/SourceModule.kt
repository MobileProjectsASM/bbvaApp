package com.example.bbvaapp.di.modules

import com.example.data.sources.hardware.LoggerSourceMobile
import com.example.data.sources.local.abstract_locals.AuthLocalSource
import com.example.data.sources.local.impl.shared_preferences.AuthLocalSourceImpl
import com.example.data.sources.remote.abstract_remotes.AuthRemoteSource
import com.example.data.sources.remote.abstract_remotes.MediaRemoteSource
import com.example.data.sources.remote.impl.rest.AuthRemoteSourceImpl
import com.example.data.sources.remote.impl.rest.MediaRemoteSourceImpl
import com.example.domain.utils.Logger
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class SourceModule {

    @ViewModelScoped
    @Binds
    abstract fun providesAuthRemoteSource(authRemoteSourceImpl: AuthRemoteSourceImpl): AuthRemoteSource

    @ViewModelScoped
    @Binds
    abstract fun providesAuthLocalSource(authLocalSourceImpl: AuthLocalSourceImpl): AuthLocalSource

    @ViewModelScoped
    @Binds
    abstract fun providesMediaRemoteSource(mediaRemoteSourceImpl: MediaRemoteSourceImpl): MediaRemoteSource

    @ViewModelScoped
    @Binds
    abstract fun providesLoggerSource(loggerSourceMobile: LoggerSourceMobile): Logger
}