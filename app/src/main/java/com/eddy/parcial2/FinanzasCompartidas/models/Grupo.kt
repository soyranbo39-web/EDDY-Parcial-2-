package com.eddy.parcial2.FinanzasCompartidas.models

data class Grupo(
    val id: String = "",
    val nombre: String = "",
    val codigo: String = "",
    val miembros: Map<String, Boolean> = emptyMap() // uid -> true
)
