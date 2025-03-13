package com.example.whoisonmywifi.network

import android.se.omapi.SEService.OnConnectedListener
import com.example.whoisonmywifi.model.ConnectedDevices
import com.example.whoisonmywifi.model.DevicesResponse
import com.example.whoisonmywifi.model.RouterDetails
import com.example.whoisonmywifi.model.UpdateStatus
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.http.GET
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url


public const val WEPBA_DEVICES_URL =
    "http://14.142.150.124:6200/api/v2/devices"

public const val WEBPA_URL =
    "http://14.142.150.124:6100/api/v2/"

// TODO: need to implement token mechanism
public const val TOKEN ="dXNlcjpwYXNz"

val interceptor : HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

val client = OkHttpClient.Builder().addInterceptor { chain ->
    val newRequest: Request = chain.request().newBuilder()
        .addHeader("Authorization", "Basic "+ TOKEN)
        .build()
    chain.proceed(newRequest)
}.build()


object HTTPLogger {
    fun getLogger(): OkHttpClient {
        /*
         * OKHTTP interceptor to log all API calls
         */
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
        return client
    }
}


private val retroJson = Json { ignoreUnknownKeys = true }
val retrofit: Retrofit = Retrofit.Builder()
    .client(HTTPLogger.getLogger())
    .baseUrl(WEBPA_URL)
    .addConverterFactory(retroJson.asConverterFactory("application/json".toMediaType()))
    .build()





interface WebPAService {
    @GET
    suspend fun getDevices(
        @Url url:String,
        @Header("Authorization")header:String
    ): DevicesResponse

    @GET
    suspend fun getConnectedDevices(
        @Url url:String,
        @Header("Authorization")header:String,
        @Query("names") names:String
    ): ConnectedDevices

    @GET
    suspend fun getDetailsForRouter(
        @Url url:String,
        @Header("Authorization")header:String,
        @Query("names") names:String
    ): RouterDetails

    @PATCH
    suspend fun updateSSID(
        @Url url:String,
        @Header("Authorization")header:String,
        @Body routerDetails: RouterDetails
    ): UpdateStatus
}

object WebPAAPI {

    val retrofitService:WebPAService by lazy {
        retrofit.create(WebPAService::class.java)
    }
}
