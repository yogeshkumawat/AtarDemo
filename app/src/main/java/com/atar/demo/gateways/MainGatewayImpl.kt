package com.atar.demo.gateways

import com.atar.demo.model.ListData
import com.atar.demo.model.Result
import io.reactivex.Observable

class MainGatewayImpl {

    private val databaseGateway: DatabaseGateway = DatabaseGatewayImpl()
    private val networkGateway: NetworkGateway = NetworkGatewayImpl()

    fun fetchData(url: String): Observable<Result<ListData>> {
        return networkGateway.fetchDataFromNetwork(url)
    }
}