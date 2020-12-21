package com.rak12.mod3app.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.rak12.mod3app.R
import com.rak12.mod3app.util.ConnectionManager
import com.rak12.mod3app.util.Validations
import org.json.JSONException
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {
    lateinit var etname: EditText
    lateinit var toolbar: Toolbar
    lateinit var etmobile: EditText
    lateinit var etemail: EditText
    lateinit var etlocation: EditText
    lateinit var etpassword: EditText
    lateinit var etconpass: EditText
    lateinit var register: Button
    lateinit var sp: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        title = "Register Yourself"
        sp = getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE)

        etname = findViewById(R.id.name)
        etmobile = findViewById(R.id.mobile)
        etemail = findViewById(R.id.email)
        etlocation = findViewById(R.id.address)
        etpassword = findViewById(R.id.pass)
        etconpass = findViewById(R.id.conpass)
        register = findViewById(R.id.register)

        register.setOnClickListener {

            val queue = Volley.newRequestQueue(this)
            val url1 = "http://13.235.250.119/v2/register/fetch_result"
            val url = "https://young-stream-54945.herokuapp.com/signup"
            val jsonParams = JSONObject()
            val name = etname.text.toString()
            val mobile = etmobile.text.toString()
            val pass = etpassword.text.toString()
            val location = etlocation.text.toString()
            val email = etemail.text.toString()
            jsonParams.put("mobile_number", mobile)
            jsonParams.put("name", name)
            jsonParams.put("address", location)
            jsonParams.put("email", email)
            jsonParams.put("password", pass)
            if (Validations.validateNameLength(name)) {
                if (Validations.validateEmail(email)) {
                    if (Validations.validateMobile(mobile)) {
                        if (Validations.validatePasswordLength(pass)) {
                            if (Validations.matchPassword(pass, etconpass.text.toString())) {
                                if (ConnectionManager().checkconnectivity(this)) {
                                    /*val jsonRequest =
                                        object : JsonObjectRequest(
                                            Method.POST,
                                            url,
                                            jsonParams,
                                            Response.Listener {
                                                try {
                                                    val data = it.getJSONObject("data")
                                                    val success =
                                                        data.getBoolean("success")

                                                    if (success) {

                                                        val data1 =
                                                            data.getJSONObject("data")
                                                        sp.edit().putString(
                                                            "user_id",
                                                            data1.getString("user_id")
                                                        ).apply()
                                                        sp.edit().putString(
                                                            "name",
                                                            data1.getString("name")
                                                        ).apply()
                                                        sp.edit().putString(
                                                            "email",
                                                            data1.getString("email")
                                                        ).apply()
                                                        sp.edit().putString(
                                                            "address",
                                                            data1.getString("address")
                                                        ).apply()
                                                        sp.edit().putString(
                                                            "mobile_number",
                                                            data1.getString("mobile_number")
                                                        ).apply()
                                                        Toast.makeText(
                                                            this,
                                                            "REGISTRATION SUCCESS",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                        val i = Intent(
                                                            this,
                                                            DashboardActivity::class.java
                                                        )
                                                        startActivity(i)
                                                        finish()
                                                    } else {
                                                        Toast.makeText(
                                                            this,
                                                            "SOMETHING WENT WRONG",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                } catch (e: JSONException) {
                                                    Toast.makeText(
                                                        this,
                                                        "Error1212",
                                                        Toast.LENGTH_SHORT
                                                    ).show()

                                                }
                                            },
                                            Response.ErrorListener { }) {
                                            override fun getHeaders(): MutableMap<String, String> {
                                                val headers = HashMap<String, String>()
                                                headers["Content-Type"] =
                                                    "application/json"
                                                headers["token"] = "b239d60302e428"
                                                return headers
                                            }
                                        }
                                    queue.add(jsonRequest)*/
                                    val jsonRequest =
                                        object : JsonObjectRequest(
                                            Method.POST,
                                            url,
                                            jsonParams,
                                            Response.Listener {
                                                try {
                                                    val data = it.getJSONObject("data")
                                                    val success =
                                                        data.getBoolean("success")

                                                    if (success) {

                                                        val data1 =
                                                            data.getJSONObject("data")
                                                        sp.edit().putString(
                                                            "user_id",
                                                            data1.getString("_id")
                                                        ).apply()
                                                        sp.edit().putString(
                                                            "name",
                                                            data1.getString("name")
                                                        ).apply()
                                                        sp.edit().putString(
                                                            "email",
                                                            data1.getString("email")
                                                        ).apply()
                                                        sp.edit().putString(
                                                            "address",
                                                            data1.getString("address")
                                                        ).apply()
                                                        sp.edit().putString(
                                                            "mobile_number",
                                                            data1.getString("mobile_number")
                                                        ).apply()
                                                        saveprefrences()
                                                        Toast.makeText(
                                                            this,
                                                            "REGISTRATION SUCCESS",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                        val i = Intent(
                                                            this,
                                                            DashboardActivity::class.java
                                                        )
                                                        startActivity(i)
                                                        finish()
                                                    } else {
                                                        Toast.makeText(
                                                            this,
                                                            "SOMETHING WENT WRONG",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                } catch (e: JSONException) {
                                                    Toast.makeText(
                                                        this,
                                                        "Error1212",
                                                        Toast.LENGTH_SHORT
                                                    ).show()

                                                }
                                            },
                                            Response.ErrorListener { }) {
                                            override fun getHeaders(): MutableMap<String, String> {
                                                val headers = HashMap<String, String>()
                                                headers["Content-Type"] = "application/json"
                                                return headers
                                            }
                                        }
                                    queue.add(jsonRequest)
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
                            } else {
                                etpassword.error = "password dont match"
                                etconpass.error = "password dont match"

                            }
                        } else {
                            etpassword.error = "password should be more than of length 4 "
                        }
                    } else {
                        etmobile.error = "Invalid mobile number"

                    }
                } else {
                    etemail.error = "Invalid email-id"
                }

            } else {
                etname.error = "Name should be of atleast 3 characters"
            }
        }
    }

    fun saveprefrences() {
        sp.edit().putBoolean("Isloggedin", true).apply()
    }

}