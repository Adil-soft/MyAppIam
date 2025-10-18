package com.example.myapplication2
//modelo de datos para inicio de sesion:
data class Usuario(
    val clave_id: Int,
    val clave: String,
    val nombre_usuario: String,
    val id_clave_persona: Int
)
//modelo de datos para mostrar perfil:
data class PerfilResponseData(
    val nombre: String,
    val apellido: String,
    val dni: String,
    val skqr: String
)