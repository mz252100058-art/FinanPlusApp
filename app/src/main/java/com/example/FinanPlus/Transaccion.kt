package com.example.FinanPlus

data class Transaccion(
    val nombre: String = "",
    val monto: Double = 0.0,
    val tipo: String = "", // "Ingreso" o "Gasto"
    val fecha: Long = System.currentTimeMillis()
)