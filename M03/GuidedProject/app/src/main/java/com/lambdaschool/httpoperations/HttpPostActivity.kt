package com.lambdaschool.httpoperations

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.lambdaschool.httpoperations.model.Employee
import com.lambdaschool.httpoperations.retrofit.JsonPlaceHolderApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HttpPostActivity : AppCompatActivity(), Callback<Employee> {

    lateinit var employeesService: JsonPlaceHolderApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_http_get)
        title = "Post Request: New Employee David"

        // TODO: create the API object
        employeesService = JsonPlaceHolderApi.Factory.create()
        addnewEmployee()
    }

    private fun addnewEmployee(){
        // TODO: Write the call to add a new employee
    val  employee = Employee("Zoink",5,40,"Snack")
    employeesService.addNewEmployee(employee).enqueue(this)
    }

    override fun onResponse(call: Call<Employee>, response: Response<Employee>) {
        response.body()?.let{
            Toast.makeText(this, it.toString(),Toast.LENGTH_SHORT).show()
        }

    }

    override fun onFailure(call: Call<Employee>, t: Throwable) {
        Toast.makeText(this,"Failure",Toast.LENGTH_SHORT).show()
    }
}
