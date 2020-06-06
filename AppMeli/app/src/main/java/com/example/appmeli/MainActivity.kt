package com.example.appmeli

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.searchButton)

        //Intent representa la intención que tiene una app de realizar una tarea.
        button.setOnClickListener{
            val intent = Intent(this, ProductActivity::class.java)
            startActivity(intent)
        }
    }
}
