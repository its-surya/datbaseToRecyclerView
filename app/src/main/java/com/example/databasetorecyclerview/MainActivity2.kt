package com.example.databasetorecyclerview

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.databasetorecyclerview.databinding.ActivityMain2Binding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding
    private var allTime: LiveData<List<estimation>>? = null
    private lateinit var appdatabase: estimationDatabase
    private lateinit var estAdapter: estimationAdapter  // Your adapter instance

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        appdatabase = estimationDatabase.getDataBase(this)
        estAdapter = estimationAdapter(emptyList()) // Initialize with an empty list

        binding.back.setOnClickListener {

            val intent = Intent(this@MainActivity2, HomeActivity::class.java)
            startActivity(intent)

        }

        val estRV: RecyclerView = binding.recyclerView // Get the RecyclerView from your layout
        estRV.adapter = estAdapter
        estRV.layoutManager = LinearLayoutManager(this)

        // Add swipe-to-delete functionality using ItemTouchHelper

        val itemTouchHelper =
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }


                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    val deletedItem = estAdapter.getItemAtPosition(position)

                    lifecycleScope.launch(Dispatchers.IO) {
                        // Delete the item from the database
                        appdatabase.EstimationDao().delete(deletedItem)

                        withContext(Dispatchers.Main) {
                            // Show Snackbar with "Undo" action
                            Snackbar.make(
                                binding.root,
                                "Deleted " + deletedItem.customername,
                                Snackbar.LENGTH_LONG
                            )
                                .setAction("Undo") {
                                    lifecycleScope.launch(Dispatchers.IO) {
                                        // Re-insert the deleted item into the database
                                        appdatabase.EstimationDao().insert(deletedItem)
//                                        appdatabase.EstimationDao().update(estimation())

                                        // Update the UI by adding the item back to the adapter
                                        withContext(Dispatchers.Main) {
                                            estAdapter.addItemAtPosition(position, deletedItem)
                                        }
                                    }
                                }
                                .show()

                            // Remove the item from the adapter
                            withContext(Dispatchers.Main) {
                                estAdapter.removeItemAtPosition(position)
                            }
                        }
                    }
                }
            })

        itemTouchHelper.attachToRecyclerView(estRV)


        // below edit left


        val itemTouchHelper2 =
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }


                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    val deletedItem = estAdapter.getItemAtPosition(position)

                    lifecycleScope.launch(Dispatchers.IO) {
                        // update the item from the database
                        var edit = deletedItem.projectname

                        var intent = Intent(this@MainActivity2 , MainActivity::class.java)
//                        intent.putExtra("edit", edit)
                        startActivity(intent)
//                        appdatabase.EstimationDao().update(estimation())

//                        withContext(Dispatchers.Main) {
//                            // Show Snackbar with "Undo" action
//
//                            Snackbar.make(
//                                binding.root,
//                                "Deleted " + deletedItem.customername,
//                                Snackbar.LENGTH_LONG
//                            )
//                                .setAction("Undo") {
//                                    lifecycleScope.launch(Dispatchers.IO) {
//                                        // Re-insert the deleted item into the database
//                                        appdatabase.EstimationDao().insert(deletedItem)
////                                        appdatabase.EstimationDao().update(estimation())
//
//                                        // Update the UI by adding the item back to the adapter
//                                        withContext(Dispatchers.Main) {
//                                            estAdapter.addItemAtPosition(position, deletedItem)
//                                        }
//                                    }
//                                }
//                                .show()

                            // Remove the item from the adapter
//                            withContext(Dispatchers.Main) {
//                                estAdapter.removeItemAtPosition(position)
//                            }
//                        }
                    }
                }
            })

        itemTouchHelper2.attachToRecyclerView(estRV)
        //above edit left



        updateRecyclerView()

    }

    private fun updateRecyclerView() {
        allTime = appdatabase.EstimationDao().getAll()
        allTime!!.observe(this@MainActivity2) {
            if (!it.isNullOrEmpty()) {
                // Update the data in your adapter
                estAdapter.setData(it)
            }
        }
    }
}