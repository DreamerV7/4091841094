package com.example.unscramble.ui

import com.example.unscramble.data.SCORE_INCREASE
import com.example.unscramble.data.getUnscrambledWord
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GameViewModelTest {

    private lateinit var viewModel: GameViewModel

    @Before
    fun setUp() {
        viewModel = GameViewModel()
    }

    @Test
    fun gameViewModel_CorrectWordGuessed_ScoreUpdatedAndErrorFlagUnset() {
        var currentGameUiState = viewModel.uiState.value
        val correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)

        viewModel.updateUserGuess(correctPlayerWord)
        viewModel.checkUserGuess()

        currentGameUiState = viewModel.uiState.value
        assertFalse(currentGameUiState.isGuessedWordWrong)
        assertEquals(SCORE_AFTER_FIRST_CORRECT_ANSWER, currentGameUiState.score)
    }

    @Test
    fun gameViewModel_IncorrectGuess_ErrorFlagSet() {
        val initialGameUiState = viewModel.uiState.value
        val initialScore = initialGameUiState.score

        val incorrectPlayerWord = "wrongword"
        viewModel.updateUserGuess(incorrectPlayerWord)
        viewModel.checkUserGuess()

        val currentGameUiState = viewModel.uiState.value
        assertTrue(currentGameUiState.isGuessedWordWrong)
        assertEquals(initialScore, currentGameUiState.score)
        assertEquals(initialGameUiState.currentWordCount, currentGameUiState.currentWordCount)
    }

    @Test
    fun gameViewModel_Initialization_FirstWordLoaded() {
        val initialGameUiState = viewModel.uiState.value

        assertFalse(initialGameUiState.currentScrambledWord.isEmpty())
        assertTrue(initialGameUiState.currentScrambledWord.isNotBlank())
        assertEquals(1, initialGameUiState.currentWordCount)
        assertEquals(0, initialGameUiState.score)
        assertFalse(initialGameUiState.isGuessedWordWrong)
        assertFalse(initialGameUiState.isGameOver)
    }

    @Test
    fun gameViewModel_AllWordsGuessed_UiStateUpdatedCorrectly() {
        var currentState = viewModel.uiState.value

        while (currentState.currentWordCount < MAX_NO_OF_WORDS && !currentState.isGameOver) {
            val correctWord = getUnscrambledWord(currentState.currentScrambledWord)
            viewModel.updateUserGuess(correctWord)
            viewModel.checkUserGuess()
            currentState = viewModel.uiState.value
        }

        val correctWord = getUnscrambledWord(currentState.currentScrambledWord)
        viewModel.updateUserGuess(correctWord)
        viewModel.checkUserGuess()

        val finalState = viewModel.uiState.value
        assertTrue(finalState.isGameOver)
        assertEquals(MAX_NO_OF_WORDS * SCORE_INCREASE, finalState.score)
        assertEquals(MAX_NO_OF_WORDS, finalState.currentWordCount - 1) // -1 потому что последнее слово не увеличивает счетчик
        assertFalse(finalState.isGuessedWordWrong)
    }

    @Test
    fun gameViewModel_WordSkipped_ScoreUnchangedAndWordCountIncreased() {
        val initialGameUiState = viewModel.uiState.value
        val initialScore = initialGameUiState.score
        val initialWordCount = initialGameUiState.currentWordCount

        viewModel.skipWord()

        val currentGameUiState = viewModel.uiState.value
        assertEquals(initialScore, currentGameUiState.score)
        assertEquals(initialWordCount + 1, currentGameUiState.currentWordCount)
        assertFalse(currentGameUiState.isGuessedWordWrong)
        assertFalse(currentGameUiState.currentScrambledWord.isEmpty())
    }

    @Test
    fun gameViewModel_GameReset_StateIsInitial() {
        val correctWord = getUnscrambledWord(viewModel.uiState.value.currentScrambledWord)
        viewModel.updateUserGuess(correctWord)
        viewModel.checkUserGuess()

        viewModel.resetGame()

        val resetState = viewModel.uiState.value
        assertEquals(1, resetState.currentWordCount)
        assertEquals(0, resetState.score)
        assertFalse(resetState.isGuessedWordWrong)
        assertFalse(resetState.isGameOver)
        assertFalse(resetState.currentScrambledWord.isEmpty())
    }

    @Test
    fun gameViewModel_UserGuessUpdated_CorrectlyStored() {
        // Given
        val testGuess = "testguess"

        // When
        viewModel.updateUserGuess(testGuess)

        // Then - проверяем, что ввод сохранился (нужен доступ к userGuess)
        assertEquals(testGuess, viewModel.userGuess)
    }

    companion object {
        private const val SCORE_AFTER_FIRST_CORRECT_ANSWER = SCORE_INCREASE
        private const val MAX_NO_OF_WORDS = 10
    }
}