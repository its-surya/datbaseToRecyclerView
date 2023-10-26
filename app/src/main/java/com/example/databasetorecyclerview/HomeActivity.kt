package com.example.databasetorecyclerview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.databasetorecyclerview.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.newProjects.setOnClickListener {

            var intent = Intent(this , MainActivity::class.java)
            startActivity(intent)
        }

        binding.myProjects.setOnClickListener {

            var intent= Intent(this, MainActivity2::class.java)
            startActivity(intent)

        }





    }
}