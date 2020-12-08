package com.rak12.mod3app.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.rak12.mod3app.R


class Getregistered : Fragment() {
   lateinit var t1:TextView
    lateinit var t2:TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_getregistered, container, false)
        t1=view.findViewById(R.id.RegisterText1)
        t2=view.findViewById(R.id.RegisterText2)
      t1.setOnClickListener{
          val u = Uri.parse("tel:" + "+917780975241")

          val i =  Intent(Intent.ACTION_DIAL, u)

          try
          {

              startActivity(i)
          }
          catch (s: SecurityException)
          {

              Toast.makeText(activity as Context, "An error occurred", Toast.LENGTH_LONG)
                  .show()
          }
      }
        t2.setOnClickListener{
            val emailIntent = Intent(Intent.ACTION_SEND)

            emailIntent.putExtra(Intent.EXTRA_EMAIL,"guptarakshit2000@gmail.com")
            emailIntent.type = "text/html"
            startActivity(emailIntent)

        }
        return view
    }




}