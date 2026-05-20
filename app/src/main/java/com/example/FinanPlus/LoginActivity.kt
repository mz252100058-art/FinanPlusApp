package com.example.FinanPlus

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        // --- CAMBIO 1: CERRAR SESIÓN AL ENTRAR ---
        // Agregamos esta línea para que, cada vez que se abra la app,
        // Firebase olvide al usuario anterior y obligue a loguearse de nuevo.
        auth.signOut()

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val etEmail = findViewById<EditText>(R.id.txtEmail)
        val etPassword = findViewById<EditText>(R.id.txtPassword)
        val tvGoToRegister = findViewById<TextView>(R.id.btnRegister)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "¡Bienvenido!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Por favor, ingresa correo y contraseña", Toast.LENGTH_SHORT).show()
            }
        }

        // --- CAMBIO 2: CONECTAR CON REGISTER ---
        // Antes tenías un Toast de "próximamente", ahora lo cambiamos
        // para que realmente abra tu RegisterActivity.
        tvGoToRegister.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }
    }

    // --- CAMBIO 3: ELIMINAR EL SALTO AUTOMÁTICO ---
    // He borrado (comentado) la lógica de "onStart" que tenías antes.
    // Al quitarla, la app ya no revisará si hay sesión activa al arrancar
    // y siempre se detendrá en esta pantalla de Login.
    /*
    public override fun onStart() {
        super.onStart()
        // Borramos el salto al MainActivity para que siempre pida Login
    }
    */
}