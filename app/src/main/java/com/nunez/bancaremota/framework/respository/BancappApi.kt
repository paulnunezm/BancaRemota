package com.nunez.bancaremota.framework.respository

import com.nunez.bancaremota.BancaRemotaAplication
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class BancappApi(app: BancaRemotaAplication) {

    private val retrofitBuilder: Retrofit.Builder = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl(app.baseUrl)

    private var bancappService: BancappService? = null
    private var isAuthorized = false

    fun getService(): BancappService {
        if (bancappService == null || isAuthorized) {
            bancappService = retrofitBuilder.build()
                    .create(BancappService::class.java)
        }
        return bancappService as BancappService
    }

    fun getAuthorizedService(accessToken: String): BancappService {
        if (bancappService == null || !isAuthorized) {

            val httpClient = OkHttpClient.Builder()
                    .addInterceptor(AuthenticationInterceptor(accessToken))
                    .build()

            val authorizedRetrofitBuilder = retrofitBuilder.client(httpClient)
                    .build()

            bancappService = authorizedRetrofitBuilder.create(BancappService::class.java)
        }
        return bancappService as BancappService
    }
}