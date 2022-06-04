package dev.novak.regroup.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


const val MAPS_BASE_API = "https://maps.googleapis.com/maps/"



object MapsApiClient {

    fun getInstance(): Retrofit {
        val mOkHttpClient = OkHttpClient
            .Builder()
            .build()

        return Retrofit.Builder()
            .baseUrl(MAPS_BASE_API)
            .addConverterFactory(GsonConverterFactory.create())
            .client(mOkHttpClient)
            .build()
    }

}