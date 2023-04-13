package com.lambdaschool.httpoperations.retrofit

import com.google.gson.Gson
import com.lambdaschool.httpoperations.model.Employee
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface JsonPlaceHolderApi {
    // TODO: Create API for different endpoints

    @GET("employee")
    fun getEmployees(): Call<List<Employee>>

    @GET("employee/{id}")
    fun getEmployeesById(@Path("id")employeeId:String) : Call<List<Employee>>

    @GET("employees")
    fun getEmployeesByAge(@Query("age") employeeAge: String) : Call<List<Employee>>

    @POST("employees")
    fun addNewEmployee(@Body employee: Employee): Call<Employee>

    @PUT("employees")
    fun updateEmployee(@Body employee: Employee): Call<Employee>

    @DELETE("employee/{id}")
    fun deleteEmployeeById(@Path("id") id:String):Call<Void>

    class Factory {
        companion object{
            const val BASE_URL = "http://demo0755375.mockable.io/"
            val gson = Gson()
            val logger = HttpLoggingInterceptor().apply{
                level = HttpLoggingInterceptor.Level.BODY
                level = HttpLoggingInterceptor.Level.BASIC
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(logger)
                .retryOnConnectionFailure(false)
                .readTimeout(35,TimeUnit.SECONDS)
                .build()

        fun create(): JsonPlaceHolderApi{

            return Retrofit.Builder()
                .baseUrl(JsonPlaceHolderApi.Factory.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(JsonPlaceHolderApi::class.java)
        }
    }
    }
}