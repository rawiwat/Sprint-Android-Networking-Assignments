package com.lambdaschool.serviceimagedownloader

import android.content.Intent
import android.os.IBinder
import android.app.Service
import android.support.v4.content.LocalBroadcastManager
import android.util.Log

// TODO: S04M04-1 create new service
class LargeImageDownloadService : Service(){

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("LargeImageDownload","started")
        //Thread.sleep(10000) this can freeze the app apparently
        Thread(Runnable (){

            val bitmap = NetworkAdapter.getBitmapFromUrl("https://i.imgur.com/wdy9G9x.jpeg")

            val intent = Intent(FILE_DOWNLOADED_ACTION).apply {
                putExtra(DOWNLOADED_IMAGE,bitmap)
            }

            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
            stopSelf()
        }).start()


        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()
        Log.i("LargeImageDownload","created")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("LargeImageDownload","destroyed")
    }

    override fun onBind(p0 : Intent?): IBinder?{
        throw UnsupportedOperationException()
    }
        companion object{
            const val FILE_DOWNLOADED_ACTION = "Kekw.service.FILE_DOWNLOADED"

            const val DOWNLOADED_IMAGE = "downloadedImage"
        }
}
// TODO: S04M04-2 Override onStartCommand

// TODO: S04M04-3 Add network call