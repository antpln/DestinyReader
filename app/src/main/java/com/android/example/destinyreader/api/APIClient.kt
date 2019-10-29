package com.android.example.destinyreader.api

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers


const val ASSETS_BASE_URL = "https://www.bungie.net"


    private  val BASE_URL : String = "https://www.bungie.net/Platform/Destiny2/"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    interface DestinyAPIService {
        @Headers("x-api-key: c1afedf42a994801852af8acf6b355d5")

        @GET("Destiny2/Manifest")
        fun getManifest():
                Call<ManifestResponse>
    }

object DestinyAPI {
    val retrofitService : DestinyAPIService by lazy {
        retrofit.create(DestinyAPIService::class.java)
    }
}
