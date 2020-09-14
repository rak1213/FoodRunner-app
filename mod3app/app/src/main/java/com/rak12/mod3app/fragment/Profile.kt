package com.rak12.mod3app.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.rak12.mod3app.R


class Profile : Fragment() {
    lateinit var name: TextView
    lateinit var phone: TextView
    lateinit var email: TextView
    lateinit var address: TextView

    lateinit var sp: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        name = view.findViewById(R.id.txtUserName)

        phone = view.findViewById(R.id.txtPhone)
        email = view.findViewById(R.id.txtEmail)
        address = view.findViewById(R.id.txtAddress)
        sp = (activity as Context).getSharedPreferences(
            getString(R.string.preference_file),
            Context.MODE_PRIVATE
        )
        name.text = sp.getString("name", "")
        email.text = sp.getString("email", "")
        address.text = sp.getString("address", "")
        phone.text = sp.getString("mobile_number", "")



        return view
    }


}

