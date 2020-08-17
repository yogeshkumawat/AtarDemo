package com.atar.demo.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import com.atar.demo.common.Constant.getNextURL
import com.atar.demo.gateways.MainGatewayImpl
import com.atar.demo.model.ListData
import com.atar.demo.model.ListItem
import com.atar.demo.model.Result
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

    private val localListItems = ArrayList<ListItem>()

    fun fetchListData(url: String): Observable<Result<ListData>> {
        //return if(localListItems.size > 0)
            //Observable.just(Result(true, ))
        return gateway.fetchData(url).map {
            if(it.data?.items != null)
                localListItems.addAll(it.data.items)
            it
        }
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

    fun observeNextPageLoading(): Observable<Result<ListData>> {
        return fetchListData(getNextURL(currentPage))
    }
}