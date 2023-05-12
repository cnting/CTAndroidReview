package com.cnting.mvi.ui.main

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import com.cnting.mvi.R
import com.cnting.mvi.ui.adapter.BannerAdapter
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class MainActivity : AppCompatActivity() {
    lateinit var bannerRecyclerView: RecyclerView
    lateinit var detailRecyclerView: RecyclerView
    lateinit var btn: Button
    lateinit var bannerAdapter: BannerAdapter

    private val mainViewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initData()
    }

    private fun initView() {
        bannerRecyclerView = findViewById(R.id.bannerRecyclerView)
        detailRecyclerView = findViewById(R.id.detailRecyclerView)
        btn = findViewById(R.id.btn)

        bannerRecyclerView.layoutManager = LinearLayoutManager(this, HORIZONTAL, false)
        bannerAdapter = BannerAdapter(mutableListOf())
        bannerRecyclerView.adapter = bannerAdapter

        detailRecyclerView.layoutManager = LinearLayoutManager(this)
//        detailRecyclerView.adapter =
    }

    private fun initData() {
        registerEvent()
        btn.setOnClickListener {
            mainViewModel.sendUiIntent(MainIntent.GetBanner)
            mainViewModel.sendUiIntent(MainIntent.GetDetail(0))
        }
    }

    private fun registerEvent() {
        lifecycleScope.launchWhenStarted {
            mainViewModel.uiStateFlow.map { it.bannerUiState }.distinctUntilChanged()
                .collect { bannerUiState ->
                    when (bannerUiState) {
                        is BannerUiState.INIT -> {}
                        is BannerUiState.SUCCESS -> {
                            Log.d("===>banner", bannerUiState.models.toString())
                            bannerAdapter.setData(bannerUiState.models)
                        }
                    }
                }
        }

        lifecycleScope.launchWhenStarted {
            mainViewModel.uiStateFlow.map { it.detailUiState }.distinctUntilChanged()
                .collect { detailUiState ->
                    when (detailUiState) {
                        is DetailUiState.INIT -> {}
                        is DetailUiState.SUCCESS -> {
                            Log.d("===>detail", detailUiState.articles.toString())
                        }
                    }
                }
        }
    }
}