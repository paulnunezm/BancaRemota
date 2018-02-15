package com.nunez.palcine.framework.respository

class Endpoints {
    companion object {
        const val BASE = "http://app.bancasjmsport.com"
        const val REQUEST_ACCESS_TOKEN = "oauth/token"
        const val USER_INFO = "api/user"
        const val GET_AVAILABLE_LOTTERIES = "api/get/lotteries"
        const val POST_PLAYS = "api/ticket"
        const val WINNING_NUMBERS = "api/winners"
    }
}