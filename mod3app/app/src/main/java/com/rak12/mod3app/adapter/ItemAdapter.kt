package com.rak12.mod3app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rak12.mod3app.R
import com.rak12.mod3app.model.Fooditems

class ItemAdapter(val context: Context, val itemlist: ArrayList<Fooditems>) :
    RecyclerView.Adapter<ItemAdapter.Vh>() {
    class Vh(view: View) : RecyclerView.ViewHolder(view) {
        val itemname: TextView = view.findViewById(R.id.txthistoryname)
        val itemprice: TextView = view.findViewById(R.id.txthistorycost)
        val sno: TextView = view.findViewById(R.id.txthistorysno)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.historyitemonerow, parent, false)
        return Vh(view)
    }

    override fun getItemCount(): Int {
        return itemlist.size
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        val ele = itemlist[position]
        holder.itemname.text = ele.food_name
        holder.itemprice.text = ele.food_cost

    }
}