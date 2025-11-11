package com.example.art

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel: ArtworkViewModel by viewModels()

    private lateinit var artworkImage: ImageView
    private lateinit var artworkTitle: TextView
    private lateinit var artistName: TextView
    private lateinit var artworkYear: TextView
    private lateinit var previousButton: Button
    private lateinit var nextButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupClickListeners()
        observeUiState()
    }

    private fun initViews() {
        artworkImage = findViewById(R.id.artwork_image)
        artworkTitle = findViewById(R.id.artwork_title)
        artistName = findViewById(R.id.artist_name)
        artworkYear = findViewById(R.id.artwork_year)
        previousButton = findViewById(R.id.previous_button)
        nextButton = findViewById(R.id.next_button)
    }

    private fun setupClickListeners() {
        // Явная отправка событий (UDF)
        previousButton.setOnClickListener {
            viewModel.onEvent(ArtworkEvent.PreviousArtwork)
        }

        nextButton.setOnClickListener {
            viewModel.onEvent(ArtworkEvent.NextArtwork)
        }
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                updateUi(state)
            }
        }
    }

    private fun updateUi(state: ArtworkState) {
        val currentArtwork = state.currentArtwork

        if (currentArtwork != null) {

            artworkImage.setImageResource(currentArtwork.imageResId)
            artworkImage.contentDescription = "${currentArtwork.title} by ${currentArtwork.artist}"
            artworkTitle.text = currentArtwork.title
            artistName.text = currentArtwork.artist
            artworkYear.text = currentArtwork.year
        }
    }
}