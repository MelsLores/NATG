package com.example.natg

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {


        Thread.sleep(500)
        setTheme(R.style.Theme_NATG)


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var btnCrear = findViewById<Button>(R.id.button2)

        btnCrear.setOnClickListener{
            val intent: Intent=Intent(this,registro::class.java)
            startActivity(intent)
        }


    }
}