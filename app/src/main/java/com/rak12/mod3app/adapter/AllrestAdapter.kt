package com.rak12.mod3app.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.os.AsyncTask
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room

import com.rak12.mod3app.R
import com.rak12.mod3app.activity.RestaurantDetails

import com.rak12.mod3app.database.*
import com.rak12.mod3app.model.Names
import com.rak12.mod3app.model.Restaurant
import com.squareup.picasso.Picasso


class AllrestAdapter(val context: Context, val data: List<Names>) :
    RecyclerView.Adapter<AllrestAdapter.AllrestadapterViewHolder>() {
    class AllrestadapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val card: CardView = view.findViewById(R.id.cardRestaurant)
        var restname: TextView = view.findViewById(R.id.txtRestaurantName)
        var restrating: TextView = view.findViewById(R.id.txtRestaurantRating)
        var cost: TextView = view.findViewById(R.id.txtCostForTwo)
        var img: ImageView = view.findViewById(R.id.imgRestaurantThumbnail)
        var imgfav: ImageView = view.findViewById(R.id.imgIsFav)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllrestadapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_rest, parent, false)
        return AllrestadapterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: AllrestadapterViewHolder, position: Int) {
        var info: Names = data[position]
        holder.restname.text = info.name
        holder.cost.text = "Rs ${info.cost}/person"
        holder.restrating.text = info.rating
        Picasso.get().load(info.img).error(R.drawable.food).into(holder.img)
        val restaurantEntity =
            RestaurantEntity(info.id, info.name, info.rating, info.cost, info.img)
        holder.card.setOnClickListener {
            val i = Intent(context, RestaurantDetails::class.java)
            i.putExtra("rid", info.id)
            i.putExtra("nameofrest", info.name)
            context.startActivity(i)
        }
        val checkfav = DBAsynctask(context, restaurantEntity, 1).execute().get()
        if (checkfav) {
            holder.imgfav.setBackgroundResource(R.drawable.ic_action_favcolouers)
        } else {
            holder.imgfav.setBackgroundResource(R.drawable.ic_action_fav)
        }
        holder.imgfav.setOnClickListener {
            if (!DBAsynctask(context, restaurantEntity, 1).execute().get()) {
                val addtofav = DBAsynctask(context, restaurantEntity, 2).execute().get()
                if (addtofav) {
                    Toast.makeText(context, "ADDED TO FAVS", Toast.LENGTH_SHORT).show()

                    holder.imgfav.setBackgroundResource(R.drawable.ic_action_favcolouers)
                } else {
                    Toast.makeText(context, "Some error occured", Toast.LENGTH_SHORT).show()

                }
            } else {
                val removefav = DBAsynctask(context, restaurantEntity, 3).execute().get()
                if (removefav) {
                    Toast.makeText(context, "REMOVED FROM FAVS", Toast.LENGTH_SHORT).show()

                    holder.imgfav.setBackgroundResource(R.drawable.ic_action_fav)
                } else {
                    Toast.makeText(context, "Some error occured", Toast.LENGTH_SHORT).show()

                }


            }
        }

    }

    class DBAsynctask(val context: Context, val restaurantEntity: RestaurantEntity, val mode: Int) :
        AsyncTask<Void, Void, Boolean>() {
        val db =
            Room.databaseBuilder(context, Database::class.java, "db").addMigrations(MIGRATION_1_2)
                .build()

        override fun doInBackground(vararg params: Void?): Boolean {
            when (mode) {
                1 -> {
                    val restaurantEntity1: RestaurantEntity =
                        db.restdao().getbyid(restaurantEntity.id)
                    db.close()
                    return restaurantEntity1 != null
                }
                2 -> {
                    db.restdao().insert(restaurantEntity)
                    db.close()
                    return true
                }
                3 -> {
                    db.restdao().delete(restaurantEntity)
                    db.close()
                    return true
                }
            }
            return false
        }


    }
}