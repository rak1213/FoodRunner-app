package com.rak12.mod3app.model

import org.json.JSONArray

class Orderhistorydetails(
    val orderid: String,
    val resname: String,
    val totalcost: String,
    val date: String,
    val fooditems: JSONArray
)