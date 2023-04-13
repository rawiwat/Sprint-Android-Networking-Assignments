package com.lambdaschool.httpoperations

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lambdaschool.httpoperations.model.Employee
import com.lambdaschool.httpoperations.retrofit.JsonPlaceHolderApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HttpPutActivity : AppCompatActivity(), Callback<Employee> {

    lateinit var employeesService:JsonPlaceHolderApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_http_get)
        title = "Put Request: Update existing Employee Steve"

        // TODO: Create the API object
        employeesService = JsonPlaceHolderApi.Factory.create()
        updateEmployee()
    }

    private fun updateEmployee(){
        // TODO: Write the call to update an employee
        val employee = Employee("Brian",6,43,"Instructor")
        employeesService.updateEmployee(employee).enqueue(this)
    }

    override fun onResponse(call: Call<Employee>, response: Response<Employee>) {
        response.body()?.let{}
    }

    override fun onFailure(call: Call<Employee>, t: Throwable) {
        TODO("Not yet implemented")
    }

}
