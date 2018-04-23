package com.nunez.bancaremota.framework.respository

import com.nunez.bancaremota.framework.helpers.PreferencesManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class ServiceProvider(
        private val preferencesManager: PreferencesManager
) {

    fun getService(): BancappService{
        val retrofit = Retrofit.Builder()
                .baseUrl(Endpoints.BASE)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        return retrofit.create(BancappService::class.java)
    }

    fun getAuthorizedService(): BancappService{

        val logging = HttpLoggingInterceptor()

        // set your desired log level
        logging.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
                .addInterceptor(AuthenticationInterceptor(preferencesManager.retrieveAccessToken()))
                .addInterceptor(logging)
                .build()

        val retrofit = Retrofit.Builder()
                .client(httpClient)
                .baseUrl(Endpoints.BASE)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        return retrofit.create(BancappService::class.java)
    }

}