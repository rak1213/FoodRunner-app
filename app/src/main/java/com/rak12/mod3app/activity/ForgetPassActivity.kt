
package com.rak12.mod3app.activity

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.rak12.mod3app.R
import com.rak12.mod3app.util.ConnectionManager
import com.rak12.mod3app.util.Validations
import org.json.JSONException
import org.json.JSONObject

class ForgetPassActivity : AppCompatActivity() {
    lateinit var etmobile: EditText
    lateinit var etemail: EditText
    lateinit var next: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_pass)
        title = "Forgot Password"
        etmobile = findViewById(R.id.mobile)
        etemail = findViewById(R.id.email)
        next = findViewById(R.id.next)
        next.setOnClickListener {
            val queue = Volley.newRequestQueue(this)
            /*val url = "http://13.235.250.119/v2/forgot_password/fetch_result"*/
            val url = "https://young-stream-54945.herokuapp.com/forgot"
            val jsonParams = JSONObject()
            val mobile = etmobile.text.toString()
            val email = etemail.text.toString()
            jsonParams.put("mobile_number", mobile)
            jsonParams.put("email", email)
            if (Validations.validateMobile(mobile)) {
                if (Validations.validateEmail(email)) {
                    if (ConnectionManager().checkconnectivity(this)) {
                        val jsonRequest =
                            object : JsonObjectRequest(
                                Method.POST, url, jsonParams, Response.Listener {
                                    try {
                                        val data = it.getJSONObject("data")
                                        val success = data.getBoolean("success")

                                        if (success) {

                                            val i =
                                                Intent(this, OtpVerificationActivity::class.java)
                                            i.putExtra("mobile_number", mobile)
                                            startActivity(i)
                                            finish()
                                        } else {
                                            Toast.makeText(
                                                this,
                                                "Wrong Login Credentials",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    } catch (e: JSONException) {
                                        Toast.makeText(this, "Error1212", Toast.LENGTH_SHORT).show()

                                    }
                                },
                                Response.ErrorListener {
                                    Toast.makeText(
                                        this,
                                        "Volley Error",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            ) {
                                override fun getHeaders(): MutableMap<String, String> {
                                    val headers = HashMap<String, String>()
                                    headers["Content-Type"] = "application/json"
                                    headers["token"] = "b239d60302e428"
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
                    etemail.error = "Invalid email-id"

                }
            } else {
                etmobile.error = "Enter your correct mobile number"

            }
        }
    }
}