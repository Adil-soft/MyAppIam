package com.example.myapplication2

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory


lateinit var retrofit: Retrofit
lateinit var service: ApiService
lateinit var user: Usuario
private var nombre_usuario: String=""
private var clave: String=""
private var jsonuser : String=""


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val nombreU = findViewById<EditText>(R.id.editTextTextPersonName)
        val contrasena = findViewById<EditText>(R.id.editTextTextPassword)
        val loginbutton = findViewById<Button>(R.id.button)
        service= CreateApiService()
        loginbutton.setOnClickListener(){
            it.hideKeyboard()
            //convert to string and delete posible spaces:
             nombre_usuario= nombreU.text.toString().trim()
            clave = contrasena.text.toString().trim()

            if(nombre_usuario.isNotEmpty() && clave.isNotEmpty()){
                if(clave.length==4){
                    println("hello world")
                    //comprobar conexion de red:
                    if(isOnline()){
                         jsonuser = nombre_usuario
                        try {
                             jsonuser= JSONObject(jsonuser).toString()
                            // Now, jsonObject contains your JSON data as a JsonObject
                        } catch (e: java.lang.Exception) {
                            // Handle any parsing errors here
                        }
                        runLogin(jsonuser, clave)
                    }else{
                        Toast.makeText(this@MainActivity, "Fallo De Red", Toast.LENGTH_SHORT).show()
                    }

                }else{
                    Toast.makeText(this@MainActivity, "contraseña incorrecta", Toast.LENGTH_SHORT).show()
                }

            }else{
                Toast.makeText(this@MainActivity, "nombre de usuario o clave vacia", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun View.hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }
    private fun isOnline(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }
    //definir el metodo createApiservice:
    private fun CreateApiService(): ApiService{
        retrofit = Retrofit.Builder()
            .baseUrl("https://adilappiam.000webhostapp.com")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        return retrofit.create(ApiService::class.java)

    }
    private fun runLogin( usuario: String, clave: String){
        val call = service.login("login", usuario, clave)
        call.enqueue(object : Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {

                    if(response.isSuccessful && (response.body() != null)) {
                            try {
                                val jsonUser= JSONObject(response.body())
                                val jsonclaveid= jsonUser.optInt("clave_id")
                                val jsonclave=jsonUser.optString("clave")
                                val jsonnombreusuario=jsonUser.optString("nombre_usuario")
                                val jsonclaveidpersona=jsonUser.optInt("id_clave_persona")
                                println(jsonclave)
                                println(jsonnombreusuario)
                                println(jsonclaveidpersona)
                                user = Usuario(jsonclaveid, jsonclave, jsonnombreusuario,jsonclaveidpersona )
                                Toast.makeText(this@MainActivity, "login correcto", Toast.LENGTH_SHORT).show()
                                //ir a profile:
                                // Supongamos que 'navController' es tu NavController y 'nombre' y 'clase' son los datos obtenidos de la autenticación
                                // Supongamos que 'navController' es tu NavController y 'nombre' y 'clase' son los datos obtenidos de la autenticación
                                //val bundle = bundleOf("nombreUsuario" to nombre, "claseUsuario" to clase)


                                // IntentFilter intent = Intent(this@MainActivity, Profile::class.java)
                               //intent.putExtra("user_id", userId) // Pasa el ID de usuario como un extra en el intent
                                //startActivity(intent)
                            }catch (e:Exception){
                                Log.d("login", e.toString())
                                Toast.makeText(this@MainActivity, response.body(), Toast.LENGTH_SHORT).show()
                            }
                        }

                }
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("login",t.toString())
                }
        })

        }
}


