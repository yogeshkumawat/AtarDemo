package com.atar.demo.utils

object Constant {
    const val DATA_URL = "https://api.github.com/orgs/octokit/repos?"

    fun getNextURL(page: Int): String {
        return DATA_URL + "page="+page+"&per_page=10"
    }

    const val TOTAL_PAGES = 5
}