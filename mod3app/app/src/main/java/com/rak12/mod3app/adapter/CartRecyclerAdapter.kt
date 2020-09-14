package com.rak12.mod3app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rak12.mod3app.R
import com.rak12.mod3app.database.CartEntity

class CartRecyclerAdapter(val context: Context, val foodOrderList: List<CartEntity>) :
    RecyclerView.Adapter<CartRecyclerAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CartViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cartsingle, parent, false)

        return CartViewHolder(view)
    }

    override fun getItemCount(): Int {
        println(foodOrderList.size)
        return foodOrderList.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {

        val item = foodOrderList[position]

        holder.foodName.text = item.foodname
        holder.foodPrice.text = "Rs.${item.price}"

    }

    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var foodName: TextView = view.findViewById(R.id.txtFoodItemName)
        var foodPrice: TextView = view.findViewById(R.id.txtFoodItemPrice)

    }
}

