package com.nunez.palcine.framework.respository

import android.util.Log
import com.nunez.palcine.PalCineApplication
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class PalCineApi(app: PalCineApplication) {

    private var retrofit: Retrofit
    private var palCineService: PalCineService? = null

    init {
        Log.d("palCineApi", "${app.baseUrl}")
        retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create())
                .baseUrl(app.baseUrl)
                .build()
    }

    fun getService(): PalCineService {
        if (palCineService == null) {
            palCineService = retrofit.create(PalCineService::class.java)
        }
        return palCineService as PalCineService
    }
}