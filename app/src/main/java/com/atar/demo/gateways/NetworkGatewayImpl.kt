package com.atar.demo.gateways

import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.Volley
import com.atar.demo.common.FeedRequest
import com.atar.demo.common.MyApplication
import com.atar.demo.model.ListData
import com.atar.demo.model.Result
import io.reactivex.Observable
import io.reactivex.ObservableEmitter

class NetworkGatewayImpl : NetworkGateway {
    override fun fetchDataFromNetwork(url: String): Observable<Result<ListData>> {
        return Observable.create {
            val queue: RequestQueue =
                Volley.newRequestQueue(MyApplication.instance.applicationContext)
            queue.cache.clear()
            val request = FeedRequest(url,
                null,
                Response.Listener { result ->
                    onFeedSuccess(it, result)
                },
                Response.ErrorListener { error ->
                    onFeedError(it, error)
                }
            )
            request.setShouldCache(false)
            queue.add<ListData>(request)
        }
    }

    private fun onFeedSuccess(
        it: ObservableEmitter<Result<ListData>>,
        result: ListData?
    ) {
        it.onNext(Result(true, result, null, 0))
    }

    private fun onFeedError(
        it: ObservableEmitter<Result<ListData>>,
        error: VolleyError?
    ) {
        it.onNext(Result(false, null, Exception("Error: " + error?.message), 0))
    }
}