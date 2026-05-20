package com.example.FinanPlus

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.FinanPlus.R
import com.google.firebase.auth.FirebaseAuth

class RegistroActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        auth = FirebaseAuth.getInstance()

        val txtEmail = findViewById<EditText>(R.id.txtEmail)
        val txtPassword = findViewById<EditText>(R.id.txtPassword)

        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val txtIrLogin = findViewById<TextView>(R.id.txtIrLogin)

        txtIrLogin.setOnClickListener {

            finish()

        }

        btnRegister.setOnClickListener {

            val email = txtEmail.text.toString().trim()
            val password = txtPassword.text.toString().trim()

            //  VALIDACIONES

            if (email.isEmpty()) {

                txtEmail.error = "Ingresa un correo"
                txtEmail.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {

                txtPassword.error = "Ingresa una contraseña"
                txtPassword.requestFocus()
                return@setOnClickListener
            }

            if (password.length < 6) {

                txtPassword.error = "Mínimo 6 caracteres"
                txtPassword.requestFocus()
                return@setOnClickListener
            }

            //  FIREBASE REGISTER

            auth.createUserWithEmailAndPassword(email, password)

                .addOnSuccessListener {

                    Toast.makeText(
                        this,
                        "Usuario registrado",
                        Toast.LENGTH_SHORT
                    ).show()

                    finish()
                }

                .addOnFailureListener {

                    Toast.makeText(
                        this,
                        "Error: ${it.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }
    }
}

