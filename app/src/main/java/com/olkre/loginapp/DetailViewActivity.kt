package com.olkre.kreshchukMovieApp

import android.media.Image
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.squareup.picasso.Picasso

class DetailViewActivity : ComponentActivity() {
    private lateinit var titleText: TextView
    private lateinit var yearLabel: TextView
    private lateinit var imageUrl: String
    private lateinit var backButton: Button
    private lateinit var posterView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_view)
        enableEdgeToEdge()


        this.titleText = findViewById<TextView>(R.id.titleText)
        this.yearLabel = findViewById<TextView>(R.id.yearLabel)
        this.backButton = findViewById<Button>(R.id.backButton)
        this.posterView = findViewById<ImageView>(R.id.posterView)

        titleText.text = intent.getStringExtra("title").orEmpty()
        yearLabel.text = intent.getStringExtra("year").orEmpty()
        imageUrl = intent.getStringExtra("imageUrl").orEmpty()

        if (imageUrl.isNullOrBlank() || imageUrl == "N/A") {
            posterView.setImageResource(R.drawable.poster_placeholder)
        } else {
            Picasso.get()
                .load(imageUrl)
                .fit()
                .centerInside()
                .into(posterView)
        }
        
        this.backButton.setOnClickListener {
            finish()
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}