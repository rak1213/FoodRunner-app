package com.rak12.mod3app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rak12.mod3app.R

class FeedbackRecyclerAdapter(val context : Context,val feedbacks : List<String>) :
    RecyclerView.Adapter<FeedbackRecyclerAdapter.FeedbackRecyclerViewHolder>(){

    class FeedbackRecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val feedbackText : TextView = view.findViewById(R.id.feedbackText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedbackRecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.feedbackrecycler,parent,false)
        return FeedbackRecyclerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return feedbacks.size
    }

    override fun onBindViewHolder(holder: FeedbackRecyclerViewHolder, position: Int) {
        val feedback :String = feedbacks[position]
        holder.feedbackText.text = feedback
    }
}