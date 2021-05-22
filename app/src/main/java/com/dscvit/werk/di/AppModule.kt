package com.dscvit.werk.di

import com.dscvit.werk.network.ApiInterface
import com.dscvit.werk.repository.AppRepository
import com.dscvit.werk.repository.AppRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val BASE_URL = "https://infinite-eyrie-56387.herokuapp.com/"

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideApi(): ApiInterface = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiInterface::class.java)

    @Singleton
    @Provides
    fun provideAppRepository(api: ApiInterface): AppRepository = AppRepositoryImpl(api)
}