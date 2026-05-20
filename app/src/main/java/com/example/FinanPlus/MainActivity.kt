package com.example.FinanPlus

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private val db = Firebase.firestore
    private var menuAbierto = false

    // RECYCLERVIEW
    private lateinit var rvTransactions: RecyclerView

    // LISTA
    private val listaTransacciones =
        mutableListOf<Transaccion>()

    // ADAPTER
    private lateinit var adapter: TransaccionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // VISTAS
        val fabMain =
            findViewById<FloatingActionButton>(R.id.fabMain)

        val layoutIngreso =
            findViewById<LinearLayout>(R.id.layoutIngreso)

        val layoutGasto =
            findViewById<LinearLayout>(R.id.layoutGasto)

        rvTransactions =
            findViewById(R.id.rvTransactions)

        // CONFIGURAR RECYCLERVIEW
        adapter = TransaccionAdapter(listaTransacciones)

        rvTransactions.layoutManager =
            LinearLayoutManager(this)

        rvTransactions.adapter = adapter

        // CARGAR DATOS
        actualizarSaldo()
        cargarTransacciones()

        // ABRIR Y CERRAR MENU
        fabMain.setOnClickListener {

            if (!menuAbierto) {

                layoutIngreso.visibility = View.VISIBLE
                layoutGasto.visibility = View.VISIBLE

                menuAbierto = true

            } else {

                layoutIngreso.visibility = View.GONE
                layoutGasto.visibility = View.GONE

                menuAbierto = false
            }
        }

        // BOTON INGRESO
        layoutIngreso.setOnClickListener {

            mostrarDialogo("Ingreso")
        }

        // BOTON GASTO
        layoutGasto.setOnClickListener {

            mostrarDialogo("Gasto")
        }
    }

    // DIALOGO
    private fun mostrarDialogo(tipo: String) {

        val dialog = BottomSheetDialog(this)

        val view = layoutInflater.inflate(
            R.layout.activity_agg_transaccion,
            null
        )

        dialog.setContentView(view)

        val titulo =
            view.findViewById<TextView>(R.id.tvTituloDialogo)

        val etNombre =
            view.findViewById<EditText>(R.id.etNombreGasto)

        val etMonto =
            view.findViewById<EditText>(R.id.etMontoGasto)

        val btnGuardar =
            view.findViewById<Button>(R.id.btnGuardarGasto)

        // CAMBIAR SEGUN TIPO
        if (tipo == "Ingreso") {

            titulo.text = "Agregar Ingreso"

            btnGuardar.text = "Guardar Ingreso"

            btnGuardar.setBackgroundColor(
                Color.parseColor("#39B54A")
            )

        } else {

            titulo.text = "Agregar Gasto"

            btnGuardar.text = "Guardar Gasto"

            btnGuardar.setBackgroundColor(
                Color.parseColor("#FF3B30")
            )
        }

        btnGuardar.setOnClickListener {

            val nombre =
                etNombre.text.toString().trim()

            val montoTexto =
                etMonto.text.toString().trim()

            val monto =
                montoTexto.toDoubleOrNull()

            val userId =
                Firebase.auth.currentUser?.uid

            // VALIDACIONES
            if (nombre.isEmpty()) {

                Toast.makeText(
                    this,
                    "Escribe un nombre",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            if (monto == null || monto <= 0) {

                Toast.makeText(
                    this,
                    "Monto inválido",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            if (userId == null) {

                Toast.makeText(
                    this,
                    "Usuario no autenticado",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            // DATOS
            val transaccion = hashMapOf(
                "nombre" to nombre,
                "monto" to monto,
                "fecha" to Timestamp.now(),
                "tipo" to tipo,
                "idUser" to userId
            )

            // COLECCION
            val coleccion =
                if (tipo == "Ingreso")
                    "ingresos"
                else
                    "gastos"

            // GUARDAR EN FIRESTORE
            db.collection(coleccion)
                .add(transaccion)
                .addOnSuccessListener {

                    Toast.makeText(
                        this,
                        "$tipo guardado correctamente",
                        Toast.LENGTH_SHORT
                    ).show()

                    // ACTUALIZAR DATOS
                    actualizarSaldo()
                    cargarTransacciones()

                    dialog.dismiss()
                }
                .addOnFailureListener {

                    Toast.makeText(
                        this,
                        "Error al guardar",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }

        dialog.show()
    }

    // ACTUALIZAR SALDO
    private fun actualizarSaldo() {

        val tvTotalBalance =
            findViewById<TextView>(R.id.tvTotalBalance)

        val userId =
            Firebase.auth.currentUser?.uid ?: return

        var totalIngresos = 0.0
        var totalGastos = 0.0

        // INGRESOS
        db.collection("ingresos")
            .whereEqualTo("idUser", userId)
            .get()
            .addOnSuccessListener { ingresos ->

                for (doc in ingresos) {

                    val monto =
                        doc.getDouble("monto") ?: 0.0

                    totalIngresos += monto
                }

                // GASTOS
                db.collection("gastos")
                    .whereEqualTo("idUser", userId)
                    .get()
                    .addOnSuccessListener { gastos ->

                        for (doc in gastos) {

                            val monto =
                                doc.getDouble("monto") ?: 0.0

                            totalGastos += monto
                        }

                        val saldo =
                            totalIngresos - totalGastos

                        tvTotalBalance.text =
                            "$%.2f".format(saldo)
                    }
            }
    }

    // CARGAR TRANSACCIONES
    private fun cargarTransacciones() {

        val userId =
            Firebase.auth.currentUser?.uid ?: return

        listaTransacciones.clear()

        // INGRESOS
        db.collection("ingresos")
            .whereEqualTo("idUser", userId)
            .get()
            .addOnSuccessListener { ingresos ->

                for (doc in ingresos) {

                    val transaccion = Transaccion(
                        nombre = doc.getString("nombre") ?: "",
                        monto = doc.getDouble("monto") ?: 0.0,
                        tipo = "Ingreso"
                    )

                    listaTransacciones.add(transaccion)
                }

                // GASTOS
                db.collection("gastos")
                    .whereEqualTo("idUser", userId)
                    .get()
                    .addOnSuccessListener { gastos ->

                        for (doc in gastos) {

                            val transaccion = Transaccion(
                                nombre = doc.getString("nombre") ?: "",
                                monto = doc.getDouble("monto") ?: 0.0,
                                tipo = "Gasto"
                            )

                            listaTransacciones.add(transaccion)
                        }

                        // ACTUALIZAR LISTA
                        adapter.notifyDataSetChanged()
                    }
            }
    }
}