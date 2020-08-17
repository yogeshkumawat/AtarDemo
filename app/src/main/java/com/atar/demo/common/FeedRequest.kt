package com.atar.demo.common

import com.android.volley.NetworkResponse
import com.android.volley.ParseError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import com.atar.demo.model.ListData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

class FeedRequest(
    url: String, private val headers: MutableMap<String?, String?>?,
    listener: Response.Listener<ListData>?, errorListener: Response.ErrorListener?
) : Request<ListData>(Method.GET, url, errorListener) {

    private val gson: Gson? = Gson()
    private val listener: Response.Listener<ListData>? = listener


    override fun getHeaders(): MutableMap<String?, String?>? {
        return headers ?: super.getHeaders()
    }

    override fun deliverResponse(response: ListData?) {
        listener?.onResponse(response)
    }

    override fun parseNetworkResponse(response: NetworkResponse?): Response<ListData?>? {
        return try {
            val json = String(
                response!!.data,
                Charset.forName(HttpHeaderParser.parseCharset(response.headers))
            )
            val collectionType = object : TypeToken<ListData>() {}.type
            Response.success(
                gson?.fromJson(json, collectionType),
                HttpHeaderParser.parseCacheHeaders(response)
            )
        } catch (e: UnsupportedEncodingException) {
            Response.error(ParseError(e))
        } catch (e: Exception) {
            Response.error(ParseError(e))
        }
    }
}