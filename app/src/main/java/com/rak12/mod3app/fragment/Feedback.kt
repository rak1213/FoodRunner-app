    package com.rak12.mod3app.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.rak12.mod3app.R
import com.rak12.mod3app.adapter.FeedbackRecyclerAdapter
import org.json.JSONObject


class Feedback : Fragment() {

    lateinit var submitButton : Button
    lateinit var feedbackText : EditText
    lateinit var feedbackRecycler : RecyclerView
    lateinit var layoutManager : RecyclerView.LayoutManager
    lateinit var adapter : FeedbackRecyclerAdapter
    lateinit var previousFeedback : TextView
    lateinit var sp: SharedPreferences
    val feedbacks = arrayListOf<String>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_feedback, container, false)
        feedbackRecycler = view.findViewById(R.id.feedbackRecycler)
        feedbackText = view.findViewById(R.id.feedbackText)
        submitButton = view.findViewById(R.id.submitFeedback)
        previousFeedback = view.findViewById(R.id.previousFeedback)
        previousFeedback.visibility = View.GONE
        sp = requireContext().getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE)
        layoutManager = LinearLayoutManager(activity)
        val queue = Volley.newRequestQueue(context)
        val url = "https://young-stream-54945.herokuapp.com/feedback"
        val jsonParams = JSONObject()
        val userId = sp.getString("user_id", null).toString()
        val jsonObjectRequest = object : JsonObjectRequest(Method.GET,url,null,Response.Listener {
            val data = it.getJSONArray("data")
            if(data.length()!=0)
            {
                previousFeedback.visibility = View.VISIBLE
                for (i in 0 until data.length()) {
                    val jsonObject = data.getJSONObject(i)
                    println(jsonObject)
                    val feedback = jsonObject.getString("feedback")
                    feedbacks.add(feedback)
                }
                adapter = FeedbackRecyclerAdapter(activity as Context, feedbacks)
                feedbackRecycler.adapter = adapter
                feedbackRecycler.layoutManager = layoutManager
            }
        },Response.ErrorListener {
        }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-type"] = "application/json"
                headers["user_id"] = userId
                return headers
            }
        }
        queue.add(jsonObjectRequest)
        submitButton.setOnClickListener {
            if(feedbackText.text.isNotEmpty())
            {
                jsonParams.put("feedback",feedbackText.text.toString())
                val jObjectRequest = object : JsonObjectRequest(Method.POST,url,jsonParams,Response.Listener {
                    val data = it.getJSONObject("data")
                    val success = data.getBoolean("success")
                    if(success)
                    {
                        feedbackText.setText("")
                        println(it)
                        Toast.makeText(context,"Feedback Submitted Successfully",Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(context,"Something Went Wrong",Toast.LENGTH_SHORT).show()
                    }
                },Response.ErrorListener {  }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["user_id"] = userId
                        return headers
                    }
                }
                queue.add(jObjectRequest)
            }
            else{
                Toast.makeText(context,"Feedback Field is left blank",Toast.LENGTH_SHORT).show()
            }
            feedbackText.clearFocus()
        }
        return view
    }
}