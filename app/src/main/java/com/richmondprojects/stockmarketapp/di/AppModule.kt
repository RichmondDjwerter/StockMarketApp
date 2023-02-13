package com.richmondprojects.stockmarketapp.di

import android.content.Context
import androidx.room.Room
import com.richmondprojects.stockmarketapp.data.local.ListingsDatabase
import com.richmondprojects.stockmarketapp.data.remote.StockApi
import com.richmondprojects.stockmarketapp.data.remote.StockApi.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
    private val client = OkHttpClient.Builder().addInterceptor(logging).build()

    @Provides
    @Singleton
    fun providesRetrofit(): StockApi {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client).baseUrl(BASE_URL)
            .build().create()
    }

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): ListingsDatabase {
        return Room.databaseBuilder(
            context, ListingsDatabase::class.java, "company_database"
        ).build()
    }
}