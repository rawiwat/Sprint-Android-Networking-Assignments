package com.example.httprequestassignmentrebuilt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.example.httprequestassignmentrebuilt.model.Employee
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GETACTIVITY : AppCompatActivity(),Callback<List<Employee>> {
    enum class get{
        ALL,
        PATH,
        QUERY
    }

    lateinit var retrofitInterface:JsonPlaceHolderApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_getactivity)

        val gson = Gson()

        retrofitInterface = Retrofit.Builder()
            .baseUrl(JsonPlaceHolderApi.Factory.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(JsonPlaceHolderApi::class.java)
        val type = intent.getSerializableExtra("get")as get
        if (type == get.ALL) {
            getEmployees()
            Toast.makeText(this, "get all employee", Toast.LENGTH_LONG).show()
        } else if (type == get.PATH) {
            getEmployeesById("2")
            Toast.makeText(this, "get the employee with ID 2", Toast.LENGTH_LONG).show()
        }
        else if (type == get.QUERY){
            getEmployeesForAge("45")
            Toast.makeText(this, "get employee with age of 45", Toast.LENGTH_LONG).show()
        }
    }

    private fun getEmployees(){
        retrofitInterface.getEmployees().enqueue(this)

    }

    private fun getEmployeesById(employeeId: String){
        retrofitInterface.getEmployeesById(employeeId).enqueue(this)

    }

    private fun getEmployeesForAge(age: String){
        retrofitInterface.getEmployeesByAge(age).enqueue(this)

    }
    override fun onFailure(call: Call<List<Employee>>, t: Throwable) {
        Toast.makeText(this,"Failed", Toast.LENGTH_SHORT).show()
    }

    override fun onResponse(
        call: Call<List<Employee>>,
        response: Response<List<Employee>>
    ) {
        response.body()?.let {

            val result = findViewById<TextView>(R.id.result)
            result.text = it.toString()
        }

    }

}