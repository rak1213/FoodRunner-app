package com.rak12.mod3app.adapter

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room

import com.rak12.mod3app.R
import com.rak12.mod3app.activity.RestaurantDetails
import com.rak12.mod3app.database.Database
import com.rak12.mod3app.database.MIGRATION_1_2
import com.rak12.mod3app.database.RestaurantEntity
import com.squareup.picasso.Picasso

class FavAdapter(val context: Context, val list: List<RestaurantEntity>) :
    RecyclerView.Adapter<FavAdapter.FavViewholde>() {
    class FavViewholde(view: View) : RecyclerView.ViewHolder(view) {
        var restname: TextView = view.findViewById(R.id.txtRestaurantName)
        var restrating: TextView = view.findViewById(R.id.txtRestaurantRating)
        var cost: TextView = view.findViewById(R.id.txtCostForTwo)
        var img: ImageView = view.findViewById(R.id.imgRestaurantThumbnail)
        var imgfav: ImageView = view.findViewById(R.id.imgIsFav)
        var card: CardView = view.findViewById(R.id.cardRestaurant)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewholde {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_rest, parent, false)
        return FavViewholde(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: FavViewholde, position: Int) {
        var info: RestaurantEntity = list[position]
        holder.restname.text = info.name
        holder.cost.text = "Rs ${info.cost_for_one}/person"
        holder.restrating.text = info.rating
        Picasso.get().load(info.image_url).error(R.drawable.food).into(holder.img)
        val restaurantEntity =
            RestaurantEntity(info.id, info.name, info.rating, info.cost_for_one, info.image_url)
        val checkfav = AllrestAdapter.DBAsynctask(context, restaurantEntity, 1).execute().get()
        if (checkfav) {
            holder.imgfav.setBackgroundResource(R.drawable.ic_action_favcolouers)
        } else {
            holder.imgfav.setBackgroundResource(R.drawable.ic_action_fav)
        }
        holder.card.setOnClickListener {
            val i = Intent(context, RestaurantDetails::class.java)
            i.putExtra("rid", info.id)
            i.putExtra("nameofrest", info.name)
            context.startActivity(i)
        }


    }


}
