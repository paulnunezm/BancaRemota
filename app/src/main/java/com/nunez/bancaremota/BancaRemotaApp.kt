package com.nunez.bancaremota

import android.app.Application
import com.nunez.bancaremota.framework.respository.Endpoints

class BancaRemotaApp : Application(), BancaRemotaAplication {
    override var baseUrl = Endpoints.BASE
}

interface BancaRemotaAplication {
    var baseUrl: String
}