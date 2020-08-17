package com.atar.demo.common

object Constant {
    const val DATA_URL = "https://raw.githubusercontent.com/yogeshkumawat/AtarDemo/master/API/"

    fun getNextURL(page: Int): String {
        return DATA_URL + "page_"+page+".json"
    }
}