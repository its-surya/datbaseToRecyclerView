package com.example.databasetorecyclerview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.example.databasetorecyclerview.databinding.ActivityMainBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
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
        val hoursofuse = binding.hoursofuse

        layoutsecond.visibility = View.GONE
        layoutthird.visibility = View.GONE
        hoursofuse.visibility = View.GONE

        appdatabase = estimationDatabase.getDataBase(this)

        var customerName : String
        var mobileNumber : String
        var customeraddress : String
        var projectname : String
        var rooftoparea : String
        var backuptype  = "None"

        binding.next1.setOnClickListener {
            customerName = binding.customerName.text.toString()
            mobileNumber = binding.mobileNumber.text.toString()
            customeraddress = binding.customerAddress.text.toString()
            projectname = binding.projectName.text.toString()

            var check1 : Boolean = false
            if( !checkMobile(mobileNumber) ){
                check1 = true
                binding.mobileNumber.error="Enter a valid mobile number"
//                Toast.makeText(this, "Enter a valid mobile number", Toast.LENGTH_SHORT).show()
            }

            if((customerName.isNotEmpty()  && customeraddress.isNotEmpty()
                        && projectname.isNotEmpty()) && !check1
            ){
                layoutfirst.visibility = View.GONE
                layoutsecond.visibility = View.VISIBLE
            }
        }

        binding.next2.setOnClickListener {
            rooftoparea = binding.rooftoparea.text.toString()

            if(rooftoparea.isNotEmpty()){
                layoutsecond.visibility=View.GONE
                layoutthird.visibility = View.VISIBLE

            }else{
                binding.rooftoparea.error="Enter the data"
            }

        }


        binding.backuptype.setOnClickListener {

            val dialog = BottomSheetDialog(this)


            val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog, null)

            val btnNone = view.findViewById<Button>(R.id.None)
            val btnDeisal = view.findViewById<Button>(R.id.Deisal)
            val btnInverter = view.findViewById<Button>(R.id.Inverter)
            

            btnNone.setOnClickListener {
                backuptype  = "None"
                binding.backuptype.setText(backuptype)
                hoursofuse.visibility = View.GONE
                dialog.dismiss()
            }

            btnDeisal.setOnClickListener {
                backuptype = "Deisal"
                binding.backuptype.setText(backuptype)
                hoursofuse.visibility = View.VISIBLE

                dialog.dismiss()
            }

            btnInverter.setOnClickListener {
                backuptype = "Inverter"
                binding.backuptype.setText(backuptype)
                hoursofuse.visibility = View.VISIBLE

                dialog.dismiss()

            }

            dialog.setCancelable(false)

            dialog.setContentView(view)

            dialog.show()

        }

        binding.submit.setOnClickListener {


            var hoursofuse = binding.hoursofuse.text.toString()

            if( backuptype.isNotEmpty() && hoursofuse.isNotEmpty() ){
                layoutthird.visibility = View.GONE
                layoutfirst.visibility = View.VISIBLE
                writeData()
                var intent = Intent(this , MainActivity2::class.java)
                startActivity(intent)

            }else{
                Toast.makeText(this@MainActivity, "Data should be entered" , Toast.LENGTH_SHORT).show()

            }


        }

        binding.pdTextView.setOnClickListener{
            if (layoutfirst.isVisible)
            layoutfirst.visibility=View.GONE
            else
                layoutfirst.visibility = View.VISIBLE
        }

        binding.tdTextView.setOnClickListener{
            if (layoutsecond.isVisible)
                layoutsecond.visibility=View.GONE
            else
                layoutsecond.visibility=View.VISIBLE
        }

        binding.bdTextView.setOnClickListener{
            if (layoutthird.isVisible)
                layoutthird.visibility=View.GONE
            else
                layoutthird.visibility=View.VISIBLE
        }


    }



    private fun checkMobile(mobileNumber: String): Boolean {
        if(mobileNumber.length < 10 ){
            return false
        }
        return true
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