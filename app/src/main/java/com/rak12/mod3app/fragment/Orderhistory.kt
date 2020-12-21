package com.rak12.mod3app.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.rak12.mod3app.R
import com.rak12.mod3app.adapter.OrderhisAdapter
import com.rak12.mod3app.model.Orderhistorydetails
import com.rak12.mod3app.util.ConnectionManager
import org.json.JSONException

class Orderhistory : Fragment() {

    lateinit var recycler1: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var pl: RelativeLayout
    lateinit var noorders: RelativeLayout
    lateinit var sp: SharedPreferences
    lateinit var oha: OrderhisAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_orderhistory, container, false)
        recycler1 = view.findViewById(R.id.recyclerorderhistory)
        pl = view.findViewById(R.id.rlLoading2)
        noorders = view.findViewById(R.id.NOORDERS)
        sp = (activity as Context).getSharedPreferences(
            getString(R.string.preference_file),
            Context.MODE_PRIVATE
        )
        val userId = sp.getString("user_id", null).toString()
        layoutManager = LinearLayoutManager(activity)
        pl.visibility = View.VISIBLE
        var orderHistoryList = ArrayList<Orderhistorydetails>()


        val queue = Volley.newRequestQueue(activity as Context)

        /*val url = "http://13.235.250.119/v2/orders/fetch_result/$userId"*/
        val url = "https://young-stream-54945.herokuapp.com/order"


        if (ConnectionManager().checkconnectivity(activity as Context)) {

            val jsonObjectRequest =
                object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                    try {

                        val data = it.getJSONObject("data")

                        val success = data.getBoolean("success")

                        if (success) {

                            val ordersArray = data.getJSONArray("data")
                            if (ordersArray.length() == 0) {
                                pl.visibility = View.GONE
                                noorders.visibility = View.VISIBLE
                            } else {
                                for (i in 0 until ordersArray.length()) {
                                    pl.visibility = View.GONE
                                    var order1 = ordersArray.getJSONObject(i)
                                    var fooditems = order1.getJSONArray("food_items")

                                    var entry = Orderhistorydetails(
                                        order1.getString("order_id"),
                                        order1.getString("restaurant_name"),
                                        order1.getString("total_cost"),
                                        order1.getString("order_placed_at"),
                                        fooditems
                                    )
                                    orderHistoryList.add(entry)

                                    oha = OrderhisAdapter(activity as Context, orderHistoryList)
                                    recycler1.layoutManager = layoutManager
                                    recycler1.adapter = oha
                                }


                            }

                        }
                    } catch (e: JSONException) {

                        Toast.makeText(
                            activity as Context,
                            "Some unexpected error occurred",
                            Toast.LENGTH_SHORT
                        ).show()
                        println("44444444")
                        println(e)

                    }

                }, Response.ErrorListener {
                    Toast.makeText(activity as Context, "Volley Error Occurred", Toast.LENGTH_SHORT)
                        .show()
                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["user_id"] = userId

                        return headers
                    }
                }

            queue.add(jsonObjectRequest)
        } else {
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("error")
            dialog.setMessage("Internet Conn not found")
            dialog.setPositiveButton("Open Settings") { text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit") { text, listener ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }


        return view
    }


}