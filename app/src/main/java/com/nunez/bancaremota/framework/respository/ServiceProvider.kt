package com.nunez.bancaremota.framework.respository

import com.nunez.bancaremota.framework.helpers.PreferencesManager
import com.nunez.palcine.framework.respository.BancappService
import com.nunez.palcine.framework.respository.Endpoints
import okhttp3.OkHttpClient
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
        val httpClient = OkHttpClient.Builder()
                .addInterceptor(AuthenticationInterceptor(preferencesManager.retrieveAccessToken()))
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