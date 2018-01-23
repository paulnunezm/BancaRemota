package com.nunez.bancaremota.framework.respository.data

data class User(val name: String,
                val email: String,
                val status: String){
    companion object {
        const val ENABLED = "enabled"
        const val DISABLED = "disabled"
    }
}