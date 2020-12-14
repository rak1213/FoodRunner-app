package com.rak12.mod3app.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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

class OtpVerificationActivity : AppCompatActivity() {
    lateinit var etOtp: EditText
    lateinit var etNewPassword: EditText
    lateinit var etConfirmNewPassword: EditText
    lateinit var btnSubmit: Button
    lateinit var sp: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_verification)
        etOtp = findViewById(R.id.etOtp)
        etNewPassword = findViewById(R.id.etNewPassword)
        etConfirmNewPassword = findViewById(R.id.etConfirmNewPassword)
        btnSubmit = findViewById(R.id.btnSubmit)
        sp = getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE)
        val number = intent.getStringExtra("mobile_number")
        btnSubmit.setOnClickListener {
            val queue = Volley.newRequestQueue(this)
            /*val url = "http://13.235.250.119/v2/reset_password/fetch_result"*/
            val url = "http://c38be6ca55f9.ngrok.io/reset_password"
            val jsonParams = JSONObject()
            val otp = etOtp.text.toString()
            val pass = etNewPassword.text.toString()
            val conpass = etConfirmNewPassword.text.toString()
            jsonParams.put("mobile_number", number)
            jsonParams.put("password", pass)
            jsonParams.put("otp", otp)
            if (otp != null) {
                if (Validations.matchPassword(pass, conpass)) {
                    if (ConnectionManager().checkconnectivity(this)) {
                        val jsonRequest =
                            object : JsonObjectRequest(
                                Method.POST, url, jsonParams, Response.Listener {
                                    try {
                                        val data = it.getJSONObject("data")
                                        val success = data.getBoolean("success")

                                        if (success) {
                                            val msg = data.getString("successMessage")
                                            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                                            val i = Intent(this, LoginActivity::class.java)
                                            sp.edit().clear().apply()
                                            startActivity(i)
                                            finish()
                                        } else {
                                            val msg = data.getString("errMessage")
                                            Toast.makeText(
                                                this,
                                                msg,
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
                    etNewPassword.error = "Passwords dont match"
                    etConfirmNewPassword.error = "Passwords dont match"
                    Toast.makeText(this, "Passwords dont match", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                etOtp.error = "enter the otp sent"
            }
        }
    }
}