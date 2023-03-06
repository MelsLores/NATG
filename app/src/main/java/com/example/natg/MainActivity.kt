package com.example.natg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {


        Thread.sleep(500)
        setTheme(R.style.Theme_NATG)


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}