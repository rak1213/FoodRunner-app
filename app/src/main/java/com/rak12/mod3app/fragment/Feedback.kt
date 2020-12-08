package com.rak12.mod3app.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.rak12.mod3app.R
import com.rak12.mod3app.activity.Confirm_Activity
import com.rak12.mod3app.activity.DashboardActivity
import com.rak12.mod3app.model.Restaurant
import org.json.JSONException
import org.json.JSONObject
import org.junit.runner.Request


class Feedback : Fragment() {

    lateinit var submitButton : Button
    lateinit var feedbackText : EditText
    lateinit var sp: SharedPreferences
    val feedbacks = arrayListOf<String>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_feedback, container, false)
        feedbackText = view.findViewById(R.id.feedbackText)
        submitButton = view.findViewById(R.id.submitFeedback)
        sp = context!!.getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE)
        val queue = Volley.newRequestQueue(context)
        val url = "http://5671486f8443.ngrok.io/feedback"
        val jsonParams = JSONObject()
        val userName = sp.getString("name", null).toString()
        val jsonObjectRequest = object : JsonObjectRequest(Method.GET,url,null,Response.Listener {
            val data = it.getJSONArray("data")
            for (i in 0 until data.length()) {
                val jsonObject = data.getJSONObject(i)
                val feedback = jsonObject.getString("feedback")
                feedbacks.add(feedback)
            }
        },Response.ErrorListener {
        }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-type"] = "application/json"
                headers["user"] = userName
                return headers
            }
        }
        queue.add(jsonObjectRequest)
        submitButton.setOnClickListener {
            jsonParams.put("feedback",feedbackText.text.toString())
            val jsonObjectRequest = object : JsonObjectRequest(Method.POST,url,jsonParams,Response.Listener {
                Toast.makeText(activity as Context, "Feedback submitted!", Toast.LENGTH_LONG)
                    .show()
            },Response.ErrorListener {  }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["user"] = userName
                    return headers
                }
            }
            queue.add(jsonObjectRequest)
            feedbackText.setText("")
            feedbackText.clearFocus()
        }
        return view
    }
}