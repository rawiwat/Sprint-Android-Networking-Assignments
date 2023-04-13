package com.lambdaschool.serviceimagedownloader

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.*
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.AlarmManagerCompat
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_fullscreen.*

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class FullscreenActivity : AppCompatActivity() {
    private val mHideHandler = Handler()
    private var mContentView: View? = null
    private val mHidePart2Runnable = Runnable {
        // Delayed removal of status and navigation bar

        // Note that some of these constants are new as of API 16 (Jelly Bean)
        // and API 19 (KitKat). It is safe to use them, as they are inlined
        // at compile-time and do nothing on earlier devices.
        mContentView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }
    private var mControlsView: View? = null
    private val mShowPart2Runnable = Runnable {
        // Delayed display of UI elements
        val actionBar = supportActionBar
        actionBar?.show()
        mControlsView?.visibility = View.VISIBLE
    }
    private var mVisible: Boolean = false
    private val mHideRunnable = Runnable { hide() }
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private val mDelayHideTouchListener = View.OnTouchListener { view, _ ->
        if (AUTO_HIDE) {
            delayedHide(AUTO_HIDE_DELAY_MILLIS)
        }
        // TODO: S04M04-4 Start service

        false
    }

    private lateinit var imageDownloadReceiver: BroadcastReceiver
    private lateinit var downloadManager: DownloadManager
    private lateinit var downloadReceiver: BroadcastReceiver
    var enqueue : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fullscreen)

        // TODO: (Optional) S04M04-8 setupDownloadManager
        downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        downloadManagerButton.setOnClickListener{
            val request = DownloadManager.Request(Uri.parse("https://i.imgur.com/wdy9G9x.jpeg"))
            enqueue = downloadManager.enqueue(request)
        }

        myImageDownload.setOnClickListener {
            val serviceIntent = Intent(this,LargeImageDownloadService::class.java)
            this.startService(serviceIntent)
            //myImageDownload.isEnabled = false
        }

        val downloadIntentFilter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        //registerReceiver(downloadReceiver, downloadIntentFilter)
        downloadReceiver = object : BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                if(intent?.action == LargeImageDownloadService.FILE_DOWNLOADED_ACTION) {
                    val bitmap =
                        intent.getParcelableExtra<Bitmap>(LargeImageDownloadService.DOWNLOADED_IMAGE)
                fullscreen_content.setImageBitmap(bitmap)
                }
                    val action = intent?.action
                when(action){
                    DownloadManager.ACTION_DOWNLOAD_COMPLETE->{
                        val query = DownloadManager.Query()
                        query.setFilterById(enqueue)
                        val c = downloadManager.query(query)
                        if (c.moveToFirst()){
                            val columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS)
                            if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)){
                                val uriString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))
                                val uriFileDescriptor = contentResolver.openFileDescriptor(Uri.parse(uriString),"r")

                                val width = fullscreen_content.width
                                val height = fullscreen_content.height
                                fullscreen_content.setImageBitmap(decodeSampledBitmapFileDescriptor(uriFileDescriptor, width, height))
                            }
                        }
                    }
                    DownloadManager.ACTION_NOTIFICATION_CLICKED->{

                    }
                    DownloadManager.ACTION_VIEW_DOWNLOADS->{

                    }
                }
            }

        }
        registerReceiver(downloadReceiver,downloadIntentFilter)

        showDownloadsButton.setOnClickListener{
            val i = Intent()
            i.action = DownloadManager.ACTION_VIEW_DOWNLOADS
            startActivity(i)
        }
        mVisible = true
        mControlsView = findViewById(R.id.fullscreen_content_controls)
        mContentView = findViewById(R.id.fullscreen_content)

        // TODO: S04M04-5 Create BroadcastReceiver
        imageDownloadReceiver = object : BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {

                if (intent?.action == LargeImageDownloadService.FILE_DOWNLOADED_ACTION){
                val bitmap = intent.getParcelableExtra<Bitmap>(LargeImageDownloadService.DOWNLOADED_IMAGE)
                fullscreen_content.setImageBitmap(bitmap)
                }

            }

        }
        // TODO: S04M04-6 Add IntentFilter
        val intentFilter = IntentFilter().apply {
            addAction(LargeImageDownloadService.FILE_DOWNLOADED_ACTION)
        }
        // TODO: S04M04-7 Register receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(imageDownloadReceiver,intentFilter)

        // Set up the user interaction to manually show or hide the system UI.
        mContentView?.setOnClickListener { toggle() }

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        myImageDownload.setOnTouchListener(mDelayHideTouchListener)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100)
    }

    private fun toggle() {
        if (mVisible) {
            hide()
        } else {
            show()
        }
    }

    private fun hide() {
        // Hide UI first
        val actionBar = supportActionBar
        actionBar?.hide()
        mControlsView?.visibility = View.GONE
        mVisible = false

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable)
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    @SuppressLint("InlinedApi")
    private fun show() {
        // Show the system bar
        mContentView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        mVisible = true

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable)
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private fun delayedHide(delayMillis: Int) {
        mHideHandler.removeCallbacks(mHideRunnable)
        mHideHandler.postDelayed(mHideRunnable, delayMillis.toLong())
    }

    companion object {
        /**
         * Whether or not the system UI should be auto-hidden after
         * [.AUTO_HIDE_DELAY_MILLIS] milliseconds.
         */
        private val AUTO_HIDE = true

        /**
         * If [.AUTO_HIDE] is set, the number of milliseconds to wait after
         * user interaction before hiding the system UI.
         */
        private val AUTO_HIDE_DELAY_MILLIS = 3000

        /**
         * Some older devices needs a small delay between UI widget updates
         * and a change of the status and navigation bar.
         */
        private val UI_ANIMATION_DELAY = 300
    }

    // TODO S04M04-9 DownloadManager

        override fun onDestroy() {
            super.onDestroy()
            // TODO S04M04-10 Unregister receivers
            unregisterReceiver(imageDownloadReceiver)
        }

        fun showDownload(view: View) {
            val i = Intent()
            i.action = DownloadManager.ACTION_VIEW_DOWNLOADS
            startActivity(i)
        }

    }
