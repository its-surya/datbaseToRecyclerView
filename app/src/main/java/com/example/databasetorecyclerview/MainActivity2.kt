package com.example.databasetorecyclerview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.databasetorecyclerview.databinding.ActivityMain2Binding
import com.example.databasetorecyclerview.databinding.ActivityMainBinding

class MainActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityMain2Binding
    private var allTime : LiveData<List<estimation>>? = null
    private lateinit var appdatabase : estimationDatabase

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        appdatabase = estimationDatabase.getDataBase(this)

        binding.back.setOnClickListener {
            val intent = Intent(this@MainActivity2, HomeActivity::class.java)
            startActivity(intent)
        }

        updateRecyclerView()

    }

    fun updateRecyclerView(){

        allTime = appdatabase.EstimationDao().getAll()
        allTime!!.observe(this@MainActivity2) {
            if(!it.isNullOrEmpty()){
                binding?.recyclerView?.adapter = estimationAdapter(it)
                binding.recyclerView.layoutManager = LinearLayoutManager(this)
            }
        }
    }
}