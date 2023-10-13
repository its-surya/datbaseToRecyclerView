package com.example.databasetorecyclerview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.databasetorecyclerview.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var appdatabase : estimationDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutfirst = binding.projectdetailslayout
        val layoutsecond = binding.technicaldetailslayout
        val layoutthird = binding.backupdetailslayout

        layoutsecond.visibility = View.GONE
        layoutthird.visibility = View.GONE

        appdatabase = estimationDatabase.getDataBase(this)

        var customerName : String
        var mobileNumber : String
        var customeraddress : String
        var projectname : String

        var rooftoparea : String
        var backuptype : String

        binding.next1.setOnClickListener {
            customerName = binding.customerName.text.toString()
            mobileNumber = binding.mobileNumber.text.toString()
            customeraddress = binding.customerAddress.text.toString()
            projectname = binding.projectName.text.toString()
            if(customerName.isNotEmpty() && mobileNumber.isNotEmpty() && customeraddress.isNotEmpty()
                && projectname.isNotEmpty()){
                layoutfirst.visibility = View.GONE
                layoutsecond.visibility = View.VISIBLE

            }else{
                Toast.makeText(this@MainActivity, "Data should be entered" , Toast.LENGTH_SHORT).show()

            }


        }

        binding.next2.setOnClickListener {
            rooftoparea = binding.rooftoparea.text.toString()

            if(rooftoparea.isNotEmpty()){
                layoutsecond.visibility=View.GONE
                layoutthird.visibility = View.VISIBLE

            }else{
                Toast.makeText(this@MainActivity, "Data should be entered" , Toast.LENGTH_SHORT).show()


            }

        }

        binding.submit.setOnClickListener {
            backuptype = binding.rooftoparea.text.toString()

            if(backuptype.isNotEmpty()){
                layoutthird.visibility = View.GONE
                layoutfirst.visibility = View.VISIBLE
                writeData()
                var intent = Intent(this , MainActivity2::class.java)
                startActivity(intent)

            }else{
                Toast.makeText(this@MainActivity, "Data should be entered" , Toast.LENGTH_SHORT).show()


            }


        }

    }


    private fun writeData(){

        val customerName = binding.customerName.text.toString()
        val mobileNumber = binding.mobileNumber.text.toString()
        val customeraddress = binding.customerAddress.text.toString()
        val projectname = binding.projectName.text.toString()

            val data = estimation(null, mobileNumber, customerName,customeraddress,projectname)

            GlobalScope.launch(Dispatchers.IO){
                appdatabase.EstimationDao().insert(data)

            }

            binding.customerName.text?.clear()
            binding.mobileNumber.text?.clear()
            binding.projectName.text?.clear()
            binding.customerAddress.text?.clear()


            Toast.makeText(this@MainActivity, "Data inserted" , Toast.LENGTH_SHORT).show()

        

    }

}