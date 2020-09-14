package com.rak12.mod3app.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.audiofx.BassBoost
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.rak12.mod3app.R
import com.rak12.mod3app.adapter.CartRecyclerAdapter
import com.rak12.mod3app.adapter.Descriptionadapter
import com.rak12.mod3app.database.CartEntity
import com.rak12.mod3app.database.Database
import com.rak12.mod3app.database.MIGRATION_1_2
import com.rak12.mod3app.util.ConnectionManager
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class CartActivity : AppCompatActivity() {
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerOrders: RecyclerView
    lateinit var toolbar: Toolbar
    lateinit var recyclerAdapter: CartRecyclerAdapter
    lateinit var sp: SharedPreferences
    lateinit var btnPlaceOrder: Button
    lateinit var txtResName: TextView
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        toolbar = findViewById(R.id.carttoolbar)
        sp = getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE)
        setuptoolbar()
        title = "My Cart"
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder)
        var j = 0
        var display = listOf<CartEntity>()
        recyclerOrders = findViewById(R.id.recyclerOrders)
        layoutManager = LinearLayoutManager(this)
        txtResName = findViewById(R.id.txtResName)
        txtResName.text = "${intent.getStringExtra("nameofrest")}"
        display = Display(this).execute().get()

        val foodItemsArray = JSONArray()
        for (i in 0 until display.size) {
            j = j + (display[i].price.toInt())
            val obj = JSONObject()
            obj.put("food_item_id", display[i].foodid.toString())
            foodItemsArray.put(obj)

        }


        recyclerAdapter = CartRecyclerAdapter(this, display)
        recyclerOrders.layoutManager = layoutManager
        recyclerOrders.adapter = recyclerAdapter
        btnPlaceOrder.text = "PLACE ORDER (Rs $j)"
        val queue = Volley.newRequestQueue(this)
        val userId = sp.getString("user_id", "")
        val resId = sp.getInt("rid", 1000)
        val url = "http://13.235.250.119/v2/place_order/fetch_result/"

        val jsonParams = JSONObject()
        jsonParams.put("user_id", userId)
        jsonParams.put("restaurant_id", resId.toString())
        jsonParams.put("total_cost", j.toString())
        jsonParams.put("food", foodItemsArray)
        println(jsonParams)
        if (ConnectionManager().checkconnectivity(this)) {
            btnPlaceOrder.setOnClickListener {

                val jsonObjectRequest =
                    object :
                        JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {
                            try {

                                val success = it.getJSONObject("data").getBoolean("success")

                                if (success) {

                                    val intent = Intent(this, Confirm_Activity::class.java)
                                    startActivity(intent)
                                    finish()

                                } else {

                                    Toast.makeText(
                                        this,
                                        "First Add Some Items In Your Cart",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val intent = Intent(this, DashboardActivity::class.java)
                                    startActivity(intent)
                                    finish()

                                }
                            } catch (e: JSONException) {

                                Toast.makeText(
                                    this,
                                    "Some unexpected error occurred",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }

                        }, Response.ErrorListener {

                            Toast.makeText(
                                this,
                                "Volley Error Occurred",
                                Toast.LENGTH_SHORT
                            ).show()

                        }) {
                        override fun getHeaders(): MutableMap<String, String> {
                            val headers = HashMap<String, String>()
                            headers["Content-type"] = "application/json"
                            headers["token"] = "b239d60302e428"

                            return headers
                        }

                    }
                queue.add(jsonObjectRequest)

            }
        } else {
            val alert = AlertDialog.Builder(this)
            alert.setTitle("Error")
            alert.setMessage("INTERNET connection not found")
            alert.setPositiveButton("open settings") { text, listener ->
                val i = Intent(Settings.ACTION_WIFI_SETTINGS)
                startActivity(i)
                this?.finish()


            }
            alert.setNegativeButton("exit") { text, listener ->
                ActivityCompat.finishAffinity(this)

            }
            alert.create().show()

        }


    }

    class Display(val context: Context) : AsyncTask<Void, Void, List<CartEntity>>() {
        override fun doInBackground(vararg params: Void?): List<CartEntity> {
            val db =
                Room.databaseBuilder(context, Database::class.java, "CartOrders").addMigrations(
                    MIGRATION_1_2
                ).build()
            return db.restdao().getallfooditem()

        }
    }

    fun setuptoolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


    }

    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return true
    }
}