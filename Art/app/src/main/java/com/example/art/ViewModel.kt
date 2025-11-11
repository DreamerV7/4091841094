package com.example.art

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


sealed class ArtworkEvent {
    object NextArtwork : ArtworkEvent()
    object PreviousArtwork : ArtworkEvent()
    data class NavigateToArtwork(val index: Int) : ArtworkEvent()
}


data class ArtworkState(
    val currentArtworkIndex: Int = 0,
    val artworks: List<Artwork> = emptyList(),
    val currentArtwork: Artwork? = null
)

class ArtworkViewModel : ViewModel() {


    private val _uiState = MutableStateFlow(ArtworkState())


    val uiState: StateFlow<ArtworkState> = _uiState.asStateFlow()

    init {
        loadArtworks()
    }

    private fun loadArtworks() {
        val artworks = listOf(
            Artwork(R.drawable.art3, "Звездная ночь", "Винсент Ван Гог", "1889"),
            Artwork(R.drawable.art2, "Постоянство памяти", "Сальвадор Дали", "1931"),
            Artwork(R.drawable.art4, "Девушка с жемчужной сережкой", "Ян Вермеер", "1665"),
            Artwork(R.drawable.art1, "Большая волна в Канагаве", "Кацусика Хокусай", "1831")
        )

        _uiState.update { currentState ->
            currentState.copy(
                artworks = artworks,
                currentArtwork = artworks.first()
            )
        }
    }


    fun onEvent(event: ArtworkEvent) {
        when (event) {
            is ArtworkEvent.NextArtwork -> showNextArtwork()
            is ArtworkEvent.PreviousArtwork -> showPreviousArtwork()
            is ArtworkEvent.NavigateToArtwork -> navigateToArtwork(event.index)
        }
    }

    private fun showNextArtwork() {
        _uiState.update { currentState ->
            val nextIndex = (currentState.currentArtworkIndex + 1) % currentState.artworks.size
            currentState.copy(
                currentArtworkIndex = nextIndex,
                currentArtwork = currentState.artworks[nextIndex]
            )
        }
    }

    private fun showPreviousArtwork() {
        _uiState.update { currentState ->
            val previousIndex = if (currentState.currentArtworkIndex == 0) {
                currentState.artworks.size - 1
            } else {
                currentState.currentArtworkIndex - 1
            }
            currentState.copy(
                currentArtworkIndex = previousIndex,
                currentArtwork = currentState.artworks[previousIndex]
            )
        }
    }

    private fun navigateToArtwork(index: Int) {
        _uiState.update { currentState ->
            if (index in currentState.artworks.indices) {
                currentState.copy(
                    currentArtworkIndex = index,
                    currentArtwork = currentState.artworks[index]
                )
            } else {
                currentState
            }
        }
    }
}