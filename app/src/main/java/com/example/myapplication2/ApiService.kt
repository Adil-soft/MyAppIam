package com.example.myapplication2

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.Call as Call1
import retrofit2.Call as Call2
import retrofit2.Call as Retrofit2Call
import retrofit2.http.Header

interface ApiService {
    @FormUrlEncoded
    @POST(/* value = */ "/scar/rest/post.php")
    fun login(
        @Field("funcion") function: String,
        @Field("nombre_usuario") nombre_usuario:String,
        @Field("clave") clave: String,
        //@Field("id_clave_persona") id_clave_persona: String
    ): retrofit2.Call<String>

   @GET("perfil")
   fun getProfil(@Header("Authorization") token: String): retrofit2.Call<PerfilResponseData>


}