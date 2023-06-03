package com.example.natg.io

import com.example.natg.io.response.DistanceResponse
import com.example.natg.io.response.getEm

import com.google.gson.Gson

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiService {/*
    @GET("google-maps/distance")
    @Headers("Accept: application/json")
    fun getDistance(@Query("from") from: String,@Query("to") to:String): Call<DistanceResponse>

    companion object Factory{
        private const val BASE_URL = "https://fos.com.pe/api/"

        fun create() ApiService{
            val interceptor = HttpLoggingInterceptor()
            interceptor.level=HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            val retrofit=Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }
    }

*/
/*
@GET("/v2/directions/driving-car")
    suspend fun getRoute(@Query("api_key")key:String,@Query("start",encoded=true) start:String,@Query("end",encoded=true) end:String): Response<RouteResponse>

*/

    @GET
    suspend fun getH(@Url url:String):Response<getEm>

}
