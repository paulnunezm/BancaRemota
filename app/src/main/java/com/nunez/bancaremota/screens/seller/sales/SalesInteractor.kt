package com.nunez.bancaremota.screens.seller.sales

import android.util.Log
import com.nunez.palcine.framework.helpers.ConnectivityChecker
import com.nunez.palcine.framework.respository.BancappService
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class SalesInteractor(
        val connectivityChecker: ConnectivityChecker,
        val service: BancappService,
        val androidScheduler: Scheduler
): SalesContract.Interactor {

    override fun postPlays(plays: List<Game>)  {
        val ticket = PostTicket(plays)
        if(connectivityChecker.isConected()){
            service.postPlays(ticket)
                    .subscribeOn(Schedulers.io())
                    .observeOn(androidScheduler)
                    .subscribe({
                        Log.d("SalesInteractor", it.body()?.string().toString())
                        Log.d("SalesInteractor", it.toString())
                    },{
                        Log.e("SalesInteractor", it.message, it)
                    })

        }else{

        }
    }

    class PostTicket( val games: List<Game>  )
}