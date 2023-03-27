package com.example.natg

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class inicio : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        var btnCafeteria = findViewById<Button>(R.id.button5)

        btnCafeteria.setOnClickListener{
            val intent: Intent = Intent(this,cafeteria::class.java)
            startActivity(intent)
        }

        var btnComida = findViewById<Button>(R.id.button4)

        btnComida.setOnClickListener{
            val intent: Intent=Intent(this,comida::class.java)
            startActivity(intent)
        }

        var btnDiversion = findViewById<Button>(R.id.button6)

        btnDiversion.setOnClickListener{
            val intent: Intent=Intent(this,diversion::class.java)
            startActivity(intent)
        }
    }
}