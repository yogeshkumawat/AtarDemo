package com.atar.demo.utils

import com.android.volley.NetworkResponse
import com.android.volley.ParseError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import com.atar.demo.model.ListItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

class FeedRequest(
    url: String, private val headers: MutableMap<String?, String?>?,
    listener: Response.Listener<ArrayList<ListItem>>?, errorListener: Response.ErrorListener?
) : Request<ArrayList<ListItem>>(Method.GET, url, errorListener) {

    private val gson: Gson? = Gson()
    private val listener: Response.Listener<ArrayList<ListItem>>? = listener


    override fun getHeaders(): MutableMap<String?, String?>? {
        return headers ?: super.getHeaders()
    }

    override fun deliverResponse(response: ArrayList<ListItem>?) {
        listener?.onResponse(response)
    }

    override fun parseNetworkResponse(response: NetworkResponse?): Response<ArrayList<ListItem>?>? {
        return try {
            val json = String(
                response!!.data,
                Charset.forName(HttpHeaderParser.parseCharset(response.headers))
            )
            var collectionType = object : TypeToken<List<ListItem>>() {}.type
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