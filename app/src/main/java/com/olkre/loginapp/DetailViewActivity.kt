package com.olkre.kreshchukMovieApp

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DetailViewActivity : ComponentActivity() {
    private lateinit var searchQueryText: EditText
    private lateinit var yearLabel: EditText
    private lateinit var image_url: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_view)
        enableEdgeToEdge()


        val searchQueryText = findViewById<TextView>(R.id.searchQueryText)
        val yearLabel = findViewById<TextView>(R.id.yearLabel)
        
        searchQueryText.text = intent.getStringExtra("title").orEmpty()
        yearLabel.text = intent.getStringExtra("year").orEmpty()
        image_url = intent.getStringExtra("image_url").orEmpty()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}