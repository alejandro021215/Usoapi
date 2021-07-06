package com.example.usoapi

import android.app.VoiceInteractor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.usoapi.adaptador.AdaptadorPersonaje
import com.example.usoapi.model.Personaje
import org.json.JSONArray
import org.json.JSONException

class MainActivity : AppCompatActivity() {
    lateinit var personajesAdapter:AdaptadorPersonaje
    lateinit var personajes:ArrayList<Personaje>
    lateinit var recyclerPersonaje:RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerPersonaje = findViewById(R.id.recyclerPersonajes)

        personajes = ArrayList<Personaje>()

        personajesAdapter = AdaptadorPersonaje(personajes,this)

        recyclerPersonaje.layoutManager = GridLayoutManager(this,1)
        recyclerPersonaje.adapter = personajesAdapter

        obtenerDatos()

    }

    fun obtenerDatos(){
        val queue = Volley.newRequestQueue(this)
        val url = "https://rickandmortyapi.com/api/character/"

        val resposeDatosPersonajes = JsonObjectRequest(Request.Method.GET,
            url,
            null,
        Response.Listener { response ->
            val listaPersonajes = JSONArray(response.getJSONArray("results").toString())

            for (index in 0..listaPersonajes.length()-1){
                try {
                    val personajeJSON = listaPersonajes.getJSONObject(index)
                    val nombre = personajeJSON.getString("name")
                    val status = personajeJSON.getString("status")
                    val  especie = personajeJSON.getString("species")
                    val imagen = personajeJSON.getString("image")
                    val personaje = Personaje(nombre,status,especie,imagen)
                    personajes.add(personaje)

                }catch (ex:JSONException){
                    Log.d("respuesta",ex.toString())
                }

            }
            Log.d("respuesta",response.toString())
        },
        Response.ErrorListener { error ->
            Log.e("respuesta",error.toString())

        })
        queue.add(resposeDatosPersonajes)
        personajesAdapter.notifyDataSetChanged()
    }

}