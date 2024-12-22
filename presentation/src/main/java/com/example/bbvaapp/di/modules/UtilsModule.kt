package com.example.bbvaapp.di.modules

import android.content.Context
import android.content.SharedPreferences
import com.example.bbvaapp.R
import com.example.data.sources.remote.impl.rest.api_service.AuthClient
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UtilsModule {

    @Singleton
    @Provides
    fun providesAuthClient(retrofit: Retrofit): AuthClient = retrofit.create(AuthClient::class.java)

    @Singleton
    @Provides
    fun providesSharedPreferences(@ApplicationContext context: Context) : SharedPreferences = context.getSharedPreferences(context.getString(R.string.preferences_file_key_session), Context.MODE_PRIVATE)

    @Provides
    fun providesRetrofit(
        @ApplicationContext context: Context,
        okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit = Retrofit.Builder()
        .baseUrl(context.getString(R.string.base_url_auth))
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun providesHttpClient(): OkHttpClient = OkHttpClient()
}