package com.cnting.ipc

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import com.cnting.ipc.aidl.BookManagerActivity
import com.cnting.ipc.aidl.IBigBitmapManager
import com.cnting.ipc.bigfile.BigFileActivity
import com.cnting.ipc.contentprovider.ProviderActivity
import com.cnting.ipc.messenger.MessengerActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        messengerBtn.setOnClickListener {
            startActivity(Intent(this, MessengerActivity::class.java))
        }
        aidlBtn.setOnClickListener { startActivity(Intent(this, BookManagerActivity::class.java)) }
        providerBtn.setOnClickListener { startActivity(Intent(this, ProviderActivity::class.java)) }
        bigFileBtn.setOnClickListener {
            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.a)
            val intent = Intent(this, BigFileActivity::class.java)
//            intent.putExtra("bitmap",bitmap)
            val bundle = Bundle()
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                val binder = object : IBigBitmapManager.Stub() {
                    override fun getBitmap(): Bitmap {
                        return bitmap
                    }
                }
                bundle.putBinder("bitmap", binder)
            }
            intent.putExtras(bundle)
            startActivity(intent)
        }


        Log.d(
            "===>",
            "activity:$this,view.context:${bigFileBtn.context},packageName:${bigFileBtn.context.packageName}"
        )
    }

}