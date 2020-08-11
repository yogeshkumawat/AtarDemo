package com.atar.demo.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import com.atar.demo.gateways.MainGatewayImpl
import com.atar.demo.model.ListItem
import com.atar.demo.model.Result
import com.atar.demo.utils.Constant
import io.reactivex.Observable

class MainViewModel() : ViewModel() {

    private val gateway: MainGatewayImpl = MainGatewayImpl()

    var progressBarVisibility: ObservableBoolean = ObservableBoolean(true)

    var listVisibility: ObservableBoolean = ObservableBoolean(false)

    fun fetchListData(url: String): Observable<Result<ArrayList<ListItem>>> {
        return gateway.fetchData(url)
    }
}