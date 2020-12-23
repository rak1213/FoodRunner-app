package com.rak12.mod3app.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rak12.mod3app.R
import com.rak12.mod3app.activity.TrackOrderActivity
import com.rak12.mod3app.model.Fooditems
import com.rak12.mod3app.model.Orderhistorydetails
import java.text.SimpleDateFormat
import java.util.*

class OrderhisAdapter(val context: Context, val orderhistorylist: ArrayList<Orderhistorydetails>) :
    RecyclerView.Adapter<OrderhisAdapter.VH>() {
    lateinit var lm: RecyclerView.LayoutManager
    lateinit var itemAdapter: ItemAdapter

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val reshisresname: TextView = view.findViewById(R.id.txtreshistoryresname)
        val date: TextView = view.findViewById(R.id.txtdate)
        val itemsdisplayrecycler: RecyclerView = view.findViewById(R.id.recyclerhistory)
        val trackorder:Button=view.findViewById(R.id.trackorder)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.orderhistorycustomrow, parent, false)
        return VH(view)
    }

    override fun getItemCount(): Int {
        return orderhistorylist.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        lm = LinearLayoutManager(context)
        var info: Orderhistorydetails = orderhistorylist[position]
        holder.reshisresname.text = info.resname
        holder.date.text = extractdate(info.date)
        holder.trackorder.setOnClickListener{
            val i=Intent(context,TrackOrderActivity::class.java)
            context.startActivity(i)
        }
        val fooditemslist = ArrayList<Fooditems>()
        for (i in 0 until info.fooditems.length()) {
            var item = info.fooditems.getJSONObject(i)
            var fooditem = Fooditems(
                item.getString("foodName"),
                item.getString("foodPrice")

            )
            fooditemslist.add(fooditem)
            itemAdapter = ItemAdapter(context, fooditemslist)
            holder.itemsdisplayrecycler.adapter = itemAdapter
            holder.itemsdisplayrecycler.layoutManager = lm

        }
    }

    private fun extractdate(dateString: String): String? {
        val format = SimpleDateFormat("dd-MM-yy HH:mm:ss", Locale.ENGLISH)
        val date: Date = format.parse(dateString) as Date

        val output = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        return output.format(date)
    }
}