package com.example.natg

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.security.KeyStore.TrustedCertificateEntry

class registro : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        auth = FirebaseAuth.getInstance()

        var username = findViewById<EditText>(R.id.eTusername)
        var email = findViewById<EditText>(R.id.eTemail)
        var password = findViewById<EditText>(R.id.eTpass)
        var passconf = findViewById<EditText>(R.id.eTpassconf)
        var registrar = findViewById<Button>(R.id.btnRegister)
        var inicioSesion = findViewById<TextView>(R.id.txtInicio)
        var validar:Int = 0

        inicioSesion.setOnClickListener{
            val intent: Intent = Intent(this,MainActivity::class.java)
            startActivity(intent)

        }


        registrar.setOnClickListener{


            if(username.text.toString().isBlank()){
                username.error = "No puede dejar campos vacíos."
                validar--
            }
            else{
                validar++
                username.error = null

            }

            if(email.text.toString().isBlank()){
                email.error = "No puede dejar campos vacíos."
                validar--
            }
            else{
                validar++
                email.error = null
            }

            if(password.text.toString().isBlank()){
                password.error = "No puede dejar campos vacíos."
                validar--
            }
            else{
                validar++
                password.error = null
            }

            if(passconf.text.toString().isBlank()){
                passconf.error = "No puede dejar campos vacíos."
                validar--
            }
            else{
                validar++
                passconf.error = null
            }

            if(passconf.text.toString()!=password.text.toString()){
                passconf.error = "Las contraseñas no coinciden."
                password.error = "Las contraseñas no coinciden."
                validar--

            }
            else{
                validar++
                passconf.error = null
                password.error = null

            }

            if(validar==5){
                var user=username.text.toString()
                var mail=email.text.toString()
                var contra=password.text.toString()
                var contraC=passconf.text.toString()
                //subirRegistro(user,mail,contra,contraC)

                auth.createUserWithEmailAndPassword(mail,contra)
                    .addOnCompleteListener{
                        if (it.isSuccessful) {
                            val intent: Intent = Intent(this,inicio::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this,it.exception.toString(),Toast.LENGTH_SHORT).show()

                        }
                    }
            }
        }



    }



    fun subirRegistro(user: String,mail: String,contra:String,contraC:String){




    }


}