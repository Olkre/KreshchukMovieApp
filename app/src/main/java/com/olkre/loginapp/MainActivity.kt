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
import android.content.Intent
import android.net.Uri

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


        // handle login action
        this.searchButton.setOnClickListener {
            //Log.i("MainActivity", "Login button clicked")
            if(searchInput.text.toString().trim().isEmpty()) {
                Toast.makeText(this, "Please include a search term", Toast.LENGTH_SHORT).show()
            }

            val apiKey = BuildConfig.OMDB_API_KEY
            val queue = Volley.newRequestQueue(this)


            val query = Uri.encode(searchInput.text.toString().trim())
            val url = "https://www.omdbapi.com/?apikey=$apiKey&s=$query"


            val request = JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                { response ->

                    if (response.has("Error")) {
                        Toast.makeText(this, "Encountered an error: ${response.getString("Error")}", Toast.LENGTH_SHORT).show()
                    } else {
                        // get the first item from results
                        val results = response.getJSONArray("Search")
                        val firstResult = results.getJSONObject(0)
                        Toast.makeText(this, "Found ${results.length()-1} results", Toast.LENGTH_SHORT).show()

                        val title = firstResult.getString("Title")
                        val year = firstResult.getString("Year")
                        var imageUrl = firstResult.optString("Poster")

                        // uncomment to trigger the placeholder image
                        //imageUrl = "N/A"


                        val intent = Intent(this, DetailViewActivity::class.java)
                        intent.putExtra("title", title)
                        intent.putExtra("year", year)
                        intent.putExtra("imageUrl", imageUrl)
                        startActivity(intent)
                    }
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
