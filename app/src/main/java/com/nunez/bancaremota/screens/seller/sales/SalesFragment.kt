package com.nunez.bancaremota.screens.seller.sales

import com.nunez.bancaremota.R
import com.nunez.palcine.BaseFragment

class SalesFragment: BaseFragment() {
    override var layoutId: Int = R.layout.sales_fragment

    companion object {
        fun newInstance(): SalesFragment{
            return SalesFragment()
        }
    }
}