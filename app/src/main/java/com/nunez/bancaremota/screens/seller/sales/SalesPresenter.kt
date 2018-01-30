package com.nunez.bancaremota.screens.seller.sales

import android.util.Log

class SalesPresenter(
        private val view: SalesContract.View,
        private val interactor: SalesContract.Interactor
) : SalesContract.Presenter {

    private lateinit var currentPlay: Game
    private var plays = ArrayList<Game>()

    init {
        observeGameEntry()
        observeLotteriesEntry()
    }

    override fun observeGameEntry() {
        view.observeGameEntry()
                .subscribe({
                    currentPlay = it
                    view.showLotteriesSelector()
                }, {

                })
    }

    override fun observeLotteriesEntry() {
        view.observeSelectedLotteries()
                .subscribe({
                    var lottery = it
                    lateinit var playWithLottery: Game
                    with(currentPlay){
                        playWithLottery = Game(first, second, third,
                                amount, lottery.id, lottery.name, type_id)
                    }
                    plays.add(playWithLottery)
                    view.addPlay(playWithLottery)
                },{
                    Log.e("SALESPRESENTER",it.message, it)
                })
    }

    override fun onSellButtonPressed() {
        view.showLoading()
        interactor.postPlays(plays)
    }
}