package com.example.databasetorecyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class estimationAdapter(
    var estimate : List<estimation>
):RecyclerView.Adapter<estimationAdapter.estimationViewHolder>()  {
    inner class estimationViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
        var id : TextView = itemView.findViewById(R.id.textview1)
        var customername : TextView = itemView.findViewById(R.id.textview2)
        var customernumber : TextView = itemView.findViewById(R.id.textview3)
        var address : TextView = itemView.findViewById(R.id.textview4)
        var projectname : TextView = itemView.findViewById(R.id.textview5)
//        var category : TextView = itemView.findViewById(R.id.textview6)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): estimationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.detailslayout,parent,false)
        return estimationViewHolder(view)
    }

    override fun getItemCount(): Int {
        return estimate.size
    }

    fun getItemAtPosition(position: Int): estimation {
        return estimate[position]
    }

    fun removeItemAtPosition(position: Int) {
        estimate = estimate.toMutableList().apply { removeAt(position) }
        notifyItemRemoved(position)
    }

    // function added by surya to edit-----------------------------
    fun editItemAtPosition(position: Int, editedItem: estimation) {
        val updatedList = estimate.toMutableList()
        updatedList[position] = editedItem
        estimate = updatedList
        notifyItemChanged(position)
    }

    //---------------------------------------------------------------
    fun setData( newData: List<estimation> ) {
        estimate = newData
        notifyDataSetChanged()
    }

    fun addItemAtPosition(position: Int, item: estimation) {
        val updatedList = estimate.toMutableList()
        updatedList.add(position, item)
        estimate = updatedList
        notifyItemInserted(position)
    }


    override fun onBindViewHolder(holder: estimationViewHolder, position: Int) {
        holder.id.text = estimate[position].id.toString()
        holder.customername.text = "Customer Name   :   "+estimate[position].customername
        holder.customernumber.text = "Mobile Number   :   "+estimate[position].customermobile
        holder.address.text = estimate[position].customeraddress
        holder.projectname.text = "Project Name   :   "+estimate[position].projectname

    }

}