package com.rak12.mod3app.activity

import android.app.Activity
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
import android.app.Dialog
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.nio.channels.AsynchronousFileChannel.open
import java.nio.channels.FileChannel.open


class CartActivity : AppCompatActivity() , PaymentResultListener {
    val TAG:String = CartActivity::class.toString()
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerOrders: RecyclerView
    lateinit var toolbar: Toolbar
    lateinit var recyclerAdapter: CartRecyclerAdapter
    lateinit var sp: SharedPreferences
    lateinit var btnPlaceOrder: Button
    lateinit var txtResName: TextView
    lateinit var paymentdialog:Dialog
    lateinit var  cod:ImageView
    lateinit var gpay:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        Checkout.preload(applicationContext)

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
            obj.put("food_item_name", display[i].foodname.toString())
            obj.put("food_item_price",display[i].price.toString())
            foodItemsArray.put(obj)

        }


        recyclerAdapter = CartRecyclerAdapter(this, display)
        recyclerOrders.layoutManager = layoutManager
        recyclerOrders.adapter = recyclerAdapter
        btnPlaceOrder.text = "PLACE ORDER (Rs $j)"
        val queue = Volley.newRequestQueue(this)
        val userId = sp.getString("user_id", "")
        val resId = sp.getInt("rid", 1000)
        val url1 = "http://13.235.250.119/v2/place_order/fetch_result/"
        val url = "https://young-stream-54945.herokuapp.com/order"

        val jsonParams = JSONObject()
        jsonParams.put("user_id", userId)
        jsonParams.put("restaurant_name", txtResName.text.toString())
        jsonParams.put("total_cost", j.toString())
        jsonParams.put("food", foodItemsArray)
        println(jsonParams)
        if (ConnectionManager().checkconnectivity(this)) {
            btnPlaceOrder.setOnClickListener {

                paymentdialog= Dialog(this)
                paymentdialog.setContentView(R.layout.paymentmethods)
                paymentdialog.setCancelable(true)
                paymentdialog.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
                paymentdialog.create()
                paymentdialog.show()
                cod=paymentdialog.findViewById(R.id.cod)
                gpay=paymentdialog.findViewById(R.id.gpay)
                cod.setOnClickListener {
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
                gpay.setOnClickListener {

                    val jsonObjectRequest =
                        object :
                            JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {
                                try {

                                    val success = it.getJSONObject("data").getBoolean("success")

                                    if (success) {
                                        startPayment(j)

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
     fun startPayment(j:Int) {

        /*
        *  You need to pass current activity in order to let Razorpay create CheckoutActivity
        * */
        val activity: Activity = this

        Checkout().setKeyID("rzp_test_PHpomGQKceY0Ra")
        try {
            val options = JSONObject()
//            options.put("name","Rak12")
//            options.put("description","Demoing Charges")
//            //You can omit the image option to fetch the image from dashboard
//            options.put("image","https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            options.put("theme.color", "#3399cc")
           options.put("currency","INR")
//            options.put("order_id", "order_DBJOWzybf0sJbb")
          options.put("amount",j*100)//pass amount in currency subunits

            val prefill = JSONObject()
            //prefill.put("amount",j)
            prefill.put("email",sp.getString("email", ""))
            prefill.put("contact",sp.getString("mobile_number", ""))
            options.put("prefill",prefill)

            Checkout().open(this,options)
        }catch (e: Exception){
            Toast.makeText(activity,"Error in payment: "+ e.message,Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(p0: String?) {


//        webView.visibility = View.GONE

//        outerBox?.visibility = View.VISIBLE
        Toast.makeText(this, "Error",Toast.LENGTH_LONG).show()

    }

    override fun onPaymentError(p0: Int, p1: String?) {

        Toast.makeText(this, "Payment Successful",Toast.LENGTH_LONG).show()
    }
}