package com.atar.demo.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.ViewModelProviders
import com.atar.demo.R
import com.atar.demo.databinding.ActivityMainBinding
import com.atar.demo.model.Result
import com.atar.demo.common.Constant.getNextURL
import com.atar.demo.common.DisposableOnNextObserver
import com.atar.demo.model.ListData
import com.atar.demo.viewmodel.MainViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private val composite: CompositeDisposable = CompositeDisposable()
    private val adapter = MainListAdapter(this)
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = setContentView<ActivityMainBinding>(this, R.layout.activity_main).apply {
            adapter = this@MainActivity.adapter
        }
        initData()
    }

    private fun initData() {
        initViewModel()
        loadFirstResponse()
    }

    private fun initViewModel() {
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        binding.viewModel = mainViewModel
        //mainViewModel.currentPage = 1
        //mainViewModel.isLastPage = false
    }

    private fun loadFirstResponse() {
        val disposableOnNextObserver =
            object : DisposableOnNextObserver<Result<ListData>>() {
                override fun onNext(result: Result<ListData>) {
                    if (result.success)
                        onFirstResponse(result.data!!)
                    else
                        onDataFetchFail(result.exception)
                }
            }
        mainViewModel.fetchListData(getNextURL(mainViewModel.currentPage))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(disposableOnNextObserver)

        composite.add(disposableOnNextObserver)
    }

    private fun onFirstResponse(data: ListData) {
        hideProgressBarAndShowList()
        setUpRecyclerViewAdapter(data)
        observeLoadMoreItems()
    }

    private fun hideProgressBarAndShowList() {
        mainViewModel.progressBarVisibility.set(false)
        mainViewModel.listVisibility.set(true)
    }

    private fun observeLoadMoreItems() {
        val disposableOnNextObserver =
            object : DisposableOnNextObserver<Unit>() {
                override fun onNext(result: Unit) {
                    loadNextPage()
                }
            }
        mainViewModel.observeLoadMoreItems()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(disposableOnNextObserver)

        composite.add(disposableOnNextObserver)
    }

    private fun loadNextPage() {
        val disposableOnNextObserver =
            object : DisposableOnNextObserver<Result<ListData>>() {
                override fun onNext(result: Result<ListData>) {
                    if (result.success)
                        onDataFetchSuccess(result.data!!)
                    else
                        onDataFetchFail(result.exception)
                }
            }
        mainViewModel.observeNextPageLoading()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(disposableOnNextObserver)

        composite.add(disposableOnNextObserver)
    }

    private fun setUpRecyclerViewAdapter(data: ListData) {
        adapter.addListItems(data.items)

        if (mainViewModel.currentPage <= data.pages.tp)
            adapter.addLoadingFooter()
        else
            mainViewModel.isLastPage = true
    }

    private fun onDataFetchFail(exception: Exception?) {
        mainViewModel.progressBarVisibility.set(false)
        Toast.makeText(this, "Error while fetching: ${exception?.message}", Toast.LENGTH_SHORT)
            .show()
    }

    private fun onDataFetchSuccess(data: ListData) {
        hideProgressBarAndShowList()
        updateList(data)
    }

    private fun updateList(data: ListData) {
        adapter.removeLoadingFooter()
        mainViewModel.isLoading = false
        adapter.addListItems(data.items)

        if (mainViewModel.currentPage != data.pages.tp)
            adapter.addLoadingFooter()
        else
            mainViewModel.isLastPage = true
    }

    override fun onDestroy() {
        super.onDestroy()
        composite.dispose()
    }
}