package com.example.natg

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.natg.*
import com.example.natg.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var auth: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(R.style.Theme_NATG)
        auth = FirebaseAuth.getInstance()

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textView.setOnClickListener {
            val intent = Intent(this, registro::class.java)
            startActivity(intent)
        }
        binding.button.setOnClickListener {
            var email = findViewById<EditText>(R.id.emailEt)
            var password = findViewById<EditText>(R.id.passET)
            var inicioSesion = findViewById<Button>(R.id.button)
            var regis = findViewById<Button>(R.id.button22)
            var validar: Int = 0

            regis.setOnClickListener {
                val intent: Intent = Intent(this, registro::class.java)
                startActivity(intent)

            }


            inicioSesion.setOnClickListener {




                if (email.text.toString().isBlank()) {
                    email.error = "No puede dejar campos vacíos."
                    validar--
                } else {
                    validar++
                    email.error = null
                }

                if (password.text.toString().isBlank()) {
                    password.error = "No puede dejar campos vacíos."
                    validar--
                } else {
                    validar++
                    password.error = null
                }

                if (validar == 2) {
                    var mail = email.text.toString()
                    var contra = password.text.toString()

                    auth.signInWithEmailAndPassword(mail, contra)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                val intent: Intent = Intent(this, inicio::class.java)
                                startActivity(intent)
                            } else {
                                Toast.makeText(this, it.exception?.message ?: "Error occurred", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }

                }

            }
        }
    }
}

