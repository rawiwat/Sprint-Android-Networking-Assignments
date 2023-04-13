package com.example.httprequestassignmentrebuilt

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.getActivity).setOnClickListener {
            if(!isNetworkConnected()) {
                Toast.makeText(this, "Connection Failed", Toast.LENGTH_LONG).show()
            } else {
            val intent = Intent(this,GETPICKERACTIVITY::class.java)
            startActivity(intent)}
        }

        findViewById<Button>(R.id.postActivity).setOnClickListener {
            if(!isNetworkConnected()) {
                Toast.makeText(this, "Connection Failed", Toast.LENGTH_LONG).show()
            } else {
            val intent = Intent(this,POSTACTIVITY::class.java)
            startActivity(intent)}
        }

        findViewById<Button>(R.id.putActivity).setOnClickListener {
            if(!isNetworkConnected()) {
                Toast.makeText(this, "Connection Failed", Toast.LENGTH_LONG).show()
            } else {
            val intent = Intent(this,PUTACTIVITY::class.java)
            startActivity(intent)}
        }

        findViewById<Button>(R.id.deleteActivity).setOnClickListener {
            if(!isNetworkConnected()) {
                Toast.makeText(this, "Connection Failed", Toast.LENGTH_LONG).show()
            } else {
            val intent = Intent(this,DELETEACTIVITY::class.java)
            startActivity(intent)}
        }

    }

    private fun isNetworkConnected(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo?.isConnected == true
    }
}