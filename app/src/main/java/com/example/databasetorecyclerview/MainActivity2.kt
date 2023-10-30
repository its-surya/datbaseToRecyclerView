package com.example.databasetorecyclerview

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.databasetorecyclerview.databinding.ActivityMain2Binding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                // Get the swiped item's position

                val position = viewHolder.adapterPosition

                // Get the swiped item

                val deletedItem = estAdapter.getItemAtPosition(position)
                // Delete the item from your database asynchronously
                GlobalScope.launch(Dispatchers.IO) {
                    appdatabase.EstimationDao().delete(deletedItem)
                }

                // Show a Snackbar with an "Undo" action
                Snackbar.make(binding.root, "Deleted " + deletedItem.customername, Snackbar.LENGTH_LONG)
                    .setAction(
                        "Undo",
                        View.OnClickListener {
                            GlobalScope.launch(Dispatchers.Main) {
                                // Re-insert the deleted item into the database asynchronously
                                appdatabase.EstimationDao().insert(deletedItem)

                                // Update the UI by adding the item back to the adapter
                                estAdapter.addItemAtPosition(position, deletedItem)
                            }
                        }
                    )
                    .show()

                // Remove the item from the adapter
                estAdapter.removeItemAtPosition(position)
            }
        })
        itemTouchHelper.attachToRecyclerView(estRV)


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