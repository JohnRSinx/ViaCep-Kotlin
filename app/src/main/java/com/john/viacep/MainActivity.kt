package com.john.viacep

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.john.viacep.api.Api
import com.john.viacep.databinding.ActivityMainBinding
import com.john.viacep.model.Address
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //Config Retrofit
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://viacep.com.br/ws/")
            .build()
            .create(Api::class.java)

        binding.btSearchCep.setOnClickListener {
            val cep = binding.editCep.text.toString()
            if (cep.isEmpty()) {
                Toast.makeText(this,"Preencha o cep!", Toast.LENGTH_SHORT).show()
            }else {
                retrofit.setAddress(cep).enqueue(object : Callback<Address>{
                    override fun onResponse(call: Call<Address>, response: Response<Address>) {
                      if (response.code() == 200) {
                          val logradouro = response.body()?.logradouro.toString()
                          val neighborhood = response.body()?.bairro.toString()
                          val city = response.body()?.localidade.toString()
                          val state = response.body()?.uf.toString()
                          setForms(logradouro,neighborhood,city,state)
                      }else {
                          Toast.makeText(applicationContext,"Cep Inválido!", Toast.LENGTH_SHORT).show()
                      }
                    }

                    override fun onFailure(call: Call<Address>, t: Throwable) {
                        Toast.makeText(applicationContext,"Erro inesperado!", Toast.LENGTH_SHORT).show()
                    }

                })
            }
        }
    }
    private fun setForms(logradouro: String, neighborhood: String,city: String,state: String,) {
        binding.editLogradouro.setText(logradouro)
        binding.editNeighbordhood.setText(neighborhood)
        binding.editCity.setText(city)
        binding.editState.setText(state)
    }
}