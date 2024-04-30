package com.john.viacep.api

import com.john.viacep.model.Address
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {
    @GET("ws/{cep}/json/")
    fun setAddress(@Path("cep")cep: String) : Call<Address>
}