package com.atar.demo.gateways

import com.atar.demo.model.ListData
import com.atar.demo.model.ListItem
import com.atar.demo.model.Result
import com.atar.demo.utils.MyApplication
import io.reactivex.Observable

class MainGatewayImpl {

    private val databaseGateway: DatabaseGateway = DatabaseGatewayImpl()
    private val networkGateway: NetworkGateway = NetworkGatewayImpl()

    fun fetchData(url: String) : Observable<Result<ArrayList<ListItem>>> {
        //if(databaseGateway.getDataFromDb())
            return networkGateway.fetchDataFromNetwork(url)
        //return databaseGateway.getDataFromDb()
    }
}