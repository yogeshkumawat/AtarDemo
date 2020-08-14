package com.atar.demo.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import com.atar.demo.gateways.MainGatewayImpl
import com.atar.demo.model.ListItem
import com.atar.demo.model.Result
import com.atar.demo.utils.Constant.getNextURL
import com.atar.demo.view.PaginationScrollListener
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class MainViewModel() : ViewModel() {

    private val gateway: MainGatewayImpl = MainGatewayImpl()

    var progressBarVisibility: ObservableBoolean = ObservableBoolean(true)

    var listVisibility: ObservableBoolean = ObservableBoolean(false)

    private val loadMoreItemsSubject = PublishSubject.create<Unit>()

    var isLoading = false

    var currentPage = 1

    var isLastPage = false

    fun fetchListData(url: String): Observable<Result<ArrayList<ListItem>>> {
        return gateway.fetchData(url)
    }

    val onScrollListener = object : PaginationScrollListener() {
        override fun loadMoreItems() {
            isLoading = true
            currentPage += 1
            loadMoreItemsSubject.onNext(Unit)
        }

        override fun isLastPage(): Boolean {
            return isLastPage
        }

        override fun isLoading(): Boolean {
            return isLoading
        }

    }

    fun observeLoadMoreItems(): Observable<Unit> {
        return loadMoreItemsSubject
    }

    fun observeNextPageLoading(): Observable<Result<ArrayList<ListItem>>> {
        return fetchListData(getNextURL(currentPage))
    }
}