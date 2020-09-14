package com.rak12.mod3app.activity

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.room.Room
import com.rak12.mod3app.R
import com.rak12.mod3app.database.Database
import com.rak12.mod3app.database.MIGRATION_1_2

class Confirm_Activity : AppCompatActivity() {
    lateinit var okButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_)

        okButton = findViewById(R.id.btnOk)

        okButton.setOnClickListener {
            var checkdeleted = DeleteAll(this).execute().get()
            if (checkdeleted) {


                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "ERROR", Toast.LENGTH_LONG).show()
            }


        }

    }

    override fun onBackPressed() {
        var checkdeleted = DeleteAll(this).execute().get()
        if (checkdeleted) {


            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "ERROR", Toast.LENGTH_LONG).show()
        }
    }

    class DeleteAll(val context: Context) : AsyncTask<Void, Void, Boolean>() {


        val db2 = Room.databaseBuilder(context, Database::class.java, "CartOrders")
            .addMigrations(
                MIGRATION_1_2
            ).build()

        override fun doInBackground(vararg params: Void?): Boolean {


            db2.restdao().deleteAllEntries()
            db2.close()
            return true
        }
    }


}