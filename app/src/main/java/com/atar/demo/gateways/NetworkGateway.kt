package com.atar.demo.gateways

import com.atar.demo.model.ListData
import com.atar.demo.model.Result
import io.reactivex.Observable

interface NetworkGateway {
    fun fetchDataFromNetwork(url: String) : Observable<Result<ListData>>
}