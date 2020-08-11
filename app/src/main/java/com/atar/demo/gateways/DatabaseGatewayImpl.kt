package com.atar.demo.gateways

import com.atar.demo.model.ListData
import com.atar.demo.model.Result
import io.reactivex.Observable

class DatabaseGatewayImpl : DatabaseGateway {
    override fun getDataFromDb() : Observable<Result<ListData>> {
        return Observable.create {  }
    }
}