package com.example.httprequestassignmentrebuilt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import java.io.Serializable

class GETPICKERACTIVITY : AppCompatActivity() {

    enum class get: Serializable {
        ALL,
        PATH,
        QUERY
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_getpickeractivity)

        findViewById<Button>(R.id.getAll).setOnClickListener {
            val intent = Intent(this, GETACTIVITY::class.java)
            intent.putExtra("get",get.ALL)
            startActivity(intent)
        }

        findViewById<Button>(R.id.getPath).setOnClickListener {
            val intent = Intent(this, GETACTIVITY::class.java)
            intent.putExtra("get",get.PATH)
            startActivity(intent)
        }

        findViewById<Button>(R.id.getQuery).setOnClickListener {
            val intent = Intent(this, GETACTIVITY::class.java)
            intent.putExtra("get",get.QUERY)
            startActivity(intent)
        }

    }
}