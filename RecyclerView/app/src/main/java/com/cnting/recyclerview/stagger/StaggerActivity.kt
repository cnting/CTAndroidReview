package com.cnting.recyclerview.stagger

import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.cnting.recyclerview.R
import com.cnting.recyclerview.util.NetConfig
import kotlinx.android.synthetic.main.activity_stagger.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by cnting on 2020/10/13
 *
 */
class StaggerActivity : AppCompatActivity() {

    lateinit var vm: StaggerViewModel
    lateinit var adapter: MyStaggerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(
            StaggerViewModel::class.java
        )
        setContentView(R.layout.activity_stagger)

        initRecyclerView()

        vm.fetchImgUrls()
        vm.imageLiveData.observe(this, Observer {
            val oldSize = adapter.list.size
            adapter.list.addAll(it)
            adapter.notifyItemRangeInserted(oldSize, it.size)
        })
    }

    private fun initRecyclerView() {
        adapter = MyStaggerAdapter(mutableListOf())
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
        recyclerView.addItemDecoration(MyItemDecoration())
        recyclerView.adapter = adapter
    }
}

class StaggerViewModel(application: Application) : AndroidViewModel(application) {

    val imageLiveData = MutableLiveData<List<String>>()

    fun fetchImgUrls() {
        viewModelScope.launch(Dispatchers.IO) {
            val bean = NetConfig.service.fetchImageUrl()
            if (bean.status == "success") {
                imageLiveData.postValue(bean.message)
            }
        }

    }
}