package com.example.natg

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.natg.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(R.style.Theme_NATG)


        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textView.setOnClickListener {
            val intent = Intent(this, registro::class.java)
            startActivity(intent)
        }
        binding.button.setOnClickListener {
            var username = findViewById<EditText>(R.id.eTusername)
            var email = findViewById<EditText>(R.id.eTemail)
            var password = findViewById<EditText>(R.id.eTpass)
            var passconf = findViewById<EditText>(R.id.eTpassconf)
            var registrar = findViewById<Button>(R.id.btnRegister)
            var inicioSesion = findViewById<TextView>(R.id.txtInicio)
            var validar: Int = 0

            inicioSesion.setOnClickListener {
                val intent: Intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

            }


            registrar.setOnClickListener {


                if (username.text.toString().isBlank()) {
                    username.error = "No puede dejar campos vacíos."
                    validar--
                } else {
                    validar++
                    username.error = null

                }

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

                if (passconf.text.toString().isBlank()) {
                    passconf.error = "No puede dejar campos vacíos."
                    validar--
                } else {
                    validar++
                    passconf.error = null
                }

                if (passconf.text.toString() != password.text.toString()) {
                    passconf.error = "Las contraseñas no coinciden."
                    password.error = "Las contraseñas no coinciden."
                    validar--

                } else {
                    validar++
                    passconf.error = null
                    password.error = null

                }

                if (validar == 5) {
                    var user = username.text.toString()
                    var mail = email.text.toString()
                    var contra = password.text.toString()
                    var contraC = passconf.text.toString()

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(mail, contra)
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

