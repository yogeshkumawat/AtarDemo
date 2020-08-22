package com.atar.demo.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import com.atar.demo.common.Constant.getNextURL
import com.atar.demo.gateways.MainGatewayImpl
import com.atar.demo.model.ListData
import com.atar.demo.model.ListItem
import com.atar.demo.model.Pages
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

    private var localListData: ListData? = null

    private lateinit var localPage: Pages

    private fun fetchListData(url: String): Observable<Result<ListData>> {
        return gateway.fetchData(url).map {
            localListData?.items?.addAll(it.data?.items!!)
            localListData?.pages = it.data?.pages!!
            it
        }
    }

    fun fetchFirstListData(url: String): Observable<Result<ListData>> {
        return if(localListData?.items?.size?:0 > 0)
            Observable.just(Result(true, localListData, null, 0))
        else {
            gateway.fetchData(url).map {
                if(localListData == null) {
                    localListData = it.data
                }
                it
            }
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