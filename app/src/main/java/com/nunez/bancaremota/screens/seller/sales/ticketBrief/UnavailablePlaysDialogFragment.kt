package com.nunez.bancaremota.screens.seller.sales.ticketBrief

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nunez.bancaremota.R
import com.nunez.bancaremota.framework.respository.data.Game
import com.nunez.bancaremota.screens.seller.sales.GamesAdapter
import com.squareup.moshi.Moshi
import kotlinx.android.synthetic.main.unvailable_plays_dialog_fragment.*

class UnavailablePlaysDialogFragment : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "UnavailablePlaysDialog"
        const val ARG_PLAYS = "plays"

        fun newInstance(plays: List<Game>): UnavailablePlaysDialogFragment {
            // Serialize to json
            val moshi = Moshi.Builder().build()
            val jsonAdapter = moshi.adapter(TicketBriefFragment.ListOfGames::class.java)
            val json = jsonAdapter.toJson(TicketBriefFragment.ListOfGames(plays))

            val args = Bundle()
            val fragment = UnavailablePlaysDialogFragment()
            args.putSerializable(ARG_PLAYS, json)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater?.inflate(R.layout.unvailable_plays_dialog_fragment, container) as View
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val serializedPlays = arguments.getSerializable(UnavailablePlaysDialogFragment.ARG_PLAYS)
        val moshi = Moshi.Builder().build()
        val jsonAdapter = moshi.adapter(TicketBriefFragment.ListOfGames::class.java)
        val listOfGames = jsonAdapter.fromJson(serializedPlays.toString())

        unavailablePlaysRecycler.apply {
            adapter = GamesAdapter(listOfGames.games as ArrayList<Game>)
            layoutManager = LinearLayoutManager(activity)
        }

        okButton.setOnClickListener { dismiss() }
    }

}