package com.olkre.kreshchukMovieApp

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import android.content.Intent

class MainActivity : ComponentActivity() {
    private lateinit var searchInput: EditText
    private lateinit var searchButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // UI elements references
        this.searchInput = findViewById(R.id.searchInput)
        this.searchButton = findViewById(R.id.searchButton)

        this.searchInput.setText("Amazing Spiderman")

        // internet connection
        val queue = Volley.newRequestQueue(this)

        // handle login action
        this.searchButton.setOnClickListener {
            Log.i("MainActivity", "Login button clicked")

            val intent = Intent(this, DetailViewActivity::class.java)
            intent.putExtra("searchQuery", searchInput.text.toString())
            startActivity(intent)
            finish()

//
//            val url = "https://jwuclasses.com/csis2023/old/login/"
//            val body = JSONObject().apply {
//                put("title", searchInput.text.toString())
//            }
//
//            // check if the body properties arent empty
//            if(body.getString("email") == "") {
//                AlertDialog.Builder(this)
//                    .setTitle("The search can't be empty")
//                    .setPositiveButton("OK", null)
//                    .show()
//            } else {
//                val request = JsonObjectRequest(
//                    Request.Method.POST, url, body,
//                    { response ->
//                        // check if the result part of the response is true
//                        val result = response.getString("success")
//                        // show toast is success and alert if failed or wrong creds
//                        if(result == "true") {
//                            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
//
//                        }
//                        else {
//                            AlertDialog.Builder(this)
//                                .setTitle("Wrong credentials for " + response.getString("email"))
//                                .setPositiveButton("OK", null)
//                                .show()
//                        }
//
//                    },
//                    // catch when the server is unreachable or broken
//                    {
//                        error ->
//                        AlertDialog.Builder(this)
//                            .setTitle("The server did not return valid response")
//                            .setMessage(error.message)
//                            .setPositiveButton("OK", null)
//                            .show()
//                    },
//                )
//                request.setShouldCache(false)
//
//                queue.add(request)
//            }
        }

    }

}
