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


            val apiKey = BuildConfig.OMDB_API_KEY
            val queue = Volley.newRequestQueue(this)


            val url = "https://www.omdbapi.com/?apikey=$apiKey&s=${searchInput.text.toString()}"



            val request = JsonObjectRequest(
                Request.Method.POST,
                url,
                null,
                { response ->
                    // Read the fields the server actually returns
                    AlertDialog.Builder(this)
                        .setTitle(url)
                        .setMessage(response.toString())
                        .setPositiveButton("OK", null)
                        .show()

                     


                    val intent = Intent(this, DetailViewActivity::class.java)
                    intent.putExtra("searchQuery", searchInput.text.toString())
                    intent.putExtra("searchResult", response.toString())
                    startActivity(intent)
                },
                { error ->
                    Log.e("DetailViewActivity", "Search request failed", error)
                    AlertDialog.Builder(this)
                        .setTitle("The server did not return valid response")
                        .setMessage(error.message)
                        .setPositiveButton("OK", null)
                        .show()
                }
            )

            request.setShouldCache(false)

            queue.add(request)

        }

    }

}
