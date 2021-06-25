package com.dscvit.werk.di

import android.content.Context
import android.util.Log
import com.dscvit.werk.network.ApiClient
import com.dscvit.werk.network.ApiInterface
import com.dscvit.werk.repository.AppRepository
import com.dscvit.werk.repository.AppRepositoryImpl
import com.dscvit.werk.util.APP_PREF
import com.dscvit.werk.util.PREF_TOKEN
import com.dscvit.werk.util.PrefHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val BASE_URL = "https://infinite-eyrie-56387.herokuapp.com/"

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideApi(@ApplicationContext context: Context): ApiInterface = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(getOkHttpClient(context))
        .build()
        .create(ApiInterface::class.java)

    private fun getOkHttpClient(context: Context): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        val sharedPref = PrefHelper.customPrefs(context, APP_PREF)

        httpClient.connectTimeout(25, TimeUnit.SECONDS)
        httpClient.readTimeout(25, TimeUnit.SECONDS)

        httpClient.addInterceptor { chain ->
            val token = sharedPref.getString(PREF_TOKEN, "")
            val tokenStr = if (token != "") {
                "Bearer $token"
            } else {
                ""
            }

            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .addHeader(
                    "Content-Type",
                    "application/json"
                )
                .addHeader(
                    "Authorization",
                    tokenStr
                )
            val request = requestBuilder.build()
            return@addInterceptor chain.proceed(request)
        }
        return httpClient.build()
    }

    @Singleton
    @Provides
    fun provideApiClient(api: ApiInterface): ApiClient = ApiClient(api)

    @Singleton
    @Provides
    fun provideAppRepository(
        apiClient: ApiClient,
        @ApplicationContext context: Context
    ): AppRepository = AppRepositoryImpl(apiClient, context)
}