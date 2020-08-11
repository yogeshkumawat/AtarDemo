package com.atar.demo.gateways

import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.Volley
import com.atar.demo.model.ListItem
import com.atar.demo.model.Result
import com.atar.demo.utils.FeedRequest
import com.atar.demo.utils.MyApplication
import io.reactivex.Observable
import io.reactivex.ObservableEmitter

class NetworkGatewayImpl : NetworkGateway {
    override fun fetchDataFromNetwork(url: String): Observable<Result<ArrayList<ListItem>>> {
        return Observable.create {
            val queue: RequestQueue =
                Volley.newRequestQueue(MyApplication.instance.applicationContext)
            queue.cache.clear()
            val request: FeedRequest = FeedRequest(url,
                null,
                Response.Listener { result ->
                    onFeedSuccess(it, result)
                },
                Response.ErrorListener { error ->
                    onFeedError(it, error)
                }
            )
            request.setShouldCache(false)
            queue.add<ArrayList<ListItem>>(request)
        }
    }

    private fun onFeedSuccess(
        it: ObservableEmitter<Result<ArrayList<ListItem>>>,
        result: ArrayList<ListItem>?
    ) {
        it.onNext(Result(true, result, null, 0))
    }

    private fun onFeedError(
        it: ObservableEmitter<Result<ArrayList<ListItem>>>,
        error: VolleyError?
    ) {
        it.onNext(Result(false, null, Exception("Error: " + error?.message), 0))
    }
}