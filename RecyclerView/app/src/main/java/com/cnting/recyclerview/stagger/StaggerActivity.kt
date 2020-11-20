package com.cnting.recyclerview.stagger

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.cnting.recyclerview.R
import com.cnting.recyclerview.util.ImageBean
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

    val imageLiveData = MutableLiveData<List<ImageBean>>()
    private val images = mutableListOf<ImageBean>()
    private val bgColorArr = arrayOf("372983", "292929", "494949", "121212", "666688", "335868")
    private val textColor = "ffffff"
    private val sizeArr = arrayOf("600x400", "400x800", "480x640", "640x480", "320x640")

    private val baseUrl =
        "https://via.placeholder.com"  //显示有问题：https://github.com/bumptech/glide/issues/4074


    fun fetchImgUrls() {
        // TODO: 暂时没找到带尺寸的图片api接口，暂时用占位图
//        viewModelScope.launch(Dispatchers.IO) {
//            val bean = NetConfig.service.fetchImageUrl()
//            if (bean.status == "success") {
//                val size = sizeArr[(Math.random() * sizeArr.size).toInt()]
//                val width: Int
//                val height: Int
//                size.split("x")
//                    .apply {
//                        width = this[0].toInt()
//                        height = this[1].toInt()
//                    }
//                imageLiveData.postValue(bean.message.map { ImageBean(it, width, height) }.toList())
//            }
//        }

        (0..20).forEach { _ ->
            val size = sizeArr[(Math.random() * sizeArr.size).toInt()]
            val width: Int
            val height: Int
            size.split("x")
                .apply {
                    width = this[0].toInt()
                    height = this[1].toInt()
                }
            val bgColor = bgColorArr[(Math.random() * bgColorArr.size).toInt()]
            val url =
                "${baseUrl}/${size}.jpg/${bgColor}/${textColor}?text=${images.size}" //对应 https://via.placeholder.com 规则
            Log.d("===>", "url:$url")
            val bean = ImageBean(url, width, height)
            images.add(bean)
        }
        imageLiveData.postValue(images)
    }
}