package com.atar.demo.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.atar.demo.R
import com.atar.demo.model.BaseItem
import com.atar.demo.model.ListItem
import com.atar.demo.model.Result
import com.atar.demo.utils.Constant.TOTAL_PAGES
import com.atar.demo.utils.Constant.getNextURL
import com.atar.demo.utils.DisposableOnNextObserver
import com.atar.demo.viewmodel.MainViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private val composite: CompositeDisposable = CompositeDisposable()
    private lateinit var adapter: MainListAdapter
    private var isLoading = false
    private var currentPage = 1
    private var isLastPage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViewModel()
        loadFirstResponse()
    }

    private fun initViewModel() {
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    private fun loadFirstResponse() {
        val disposableOnNextObserver =
            object : DisposableOnNextObserver<Result<ArrayList<ListItem>>>() {
                override fun onNext(result: Result<ArrayList<ListItem>>) {
                    if (result.success)
                        onFirstResponse(result.data!!)
                    else
                        onDataFetchFail(result.exception)
                }
            }
        mainViewModel.fetchListData(getNextURL(1))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(disposableOnNextObserver)

        composite.add(disposableOnNextObserver)
    }

    private fun onFirstResponse(data: ArrayList<ListItem>) {
        progressBar.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        showListData(data)
    }

    private fun showListData(data: ArrayList<ListItem>) {
        adapter = MainListAdapter(this, data as ArrayList<BaseItem>)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        addScrollListener(layoutManager)

        if (currentPage <= TOTAL_PAGES)
            adapter.addLoadingFooter()
        else
            isLastPage = true
    }

    private fun onDataFetchFail(exception: Exception?) {
        progressBar.visibility = View.GONE
        Toast.makeText(this, "Error while fetching: ${exception?.message}", Toast.LENGTH_SHORT).show()
    }

    private fun addScrollListener(layoutManager: LinearLayoutManager) {
        recyclerView.addOnScrollListener(object: PaginationScrollListener(layoutManager) {

            override fun loadMoreItems() {
                isLoading = true
                currentPage += 1
                loadNextPage()
            }

            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }
        })
    }

    private fun loadNextPage() {
        val disposableOnNextObserver =
            object : DisposableOnNextObserver<Result<ArrayList<ListItem>>>() {
                override fun onNext(result: Result<ArrayList<ListItem>>) {
                    if (result.success)
                        onDataFetchSuccess(result.data!!)
                    else
                        onDataFetchFail(result.exception)
                }
            }
        mainViewModel.fetchListData(getNextURL(currentPage))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(disposableOnNextObserver)

        composite.add(disposableOnNextObserver)
    }

    private fun onDataFetchSuccess(data: ArrayList<ListItem>) {
        progressBar.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        updateList(data)
    }

    private fun updateList(data: ArrayList<ListItem>) {
        adapter.removeLoadingFooter()
        isLoading = false
        adapter.addListItems(data)

        if (currentPage != TOTAL_PAGES)
            adapter.addLoadingFooter()
        else
            isLastPage = true
    }

    override fun onDestroy() {
        super.onDestroy()
        composite.dispose()
    }
}