// ui/GameScreen.kt
package com.example.unscramble.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.unscramble.R
import com.example.unscramble.data.MAX_NO_OF_WORDS
import com.example.unscramble.ui.theme.UnscrambleTheme

@Composable
fun GameScreen(
    gameViewModel: GameViewModel = viewModel() // Листинг 6.19
) {
    // Листинг 6.20
    val gameUiState by gameViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.medium_padding)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.medium_padding))
    ) {

        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        // Листинг 6.21, 6.26, 6.30, 6.34, 6.36, 6.37
        GameLayout(
            currentScrambledWord = gameUiState.currentScrambledWord,
            userGuess = gameViewModel.userGuess,
            isGuessWrong = gameUiState.isGuessedWordWrong,
            wordCount = gameUiState.currentWordCount,
            onUserGuessChanged = { gameViewModel.updateUserGuess(it) },
            onKeyboardDone = { gameViewModel.checkUserGuess() },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(dimensionResource(R.dimen.medium_padding))
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.medium_padding)),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.medium_padding))
        ) {
            // Листинг 6.39
            OutlinedButton(
                onClick = { gameViewModel.skipWord() },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(text = stringResource(R.string.skip))
            }

            // Листинг 6.33
            Button(
                onClick = { gameViewModel.checkUserGuess() },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(text = stringResource(R.string.submit))
            }
        }

        // Листинг 6.38
        GameStatus(score = gameUiState.score, modifier = Modifier.padding(20.dp))

        // Листинг 6.43
        if (gameUiState.isGameOver) {
            FinalScoreDialog(
                score = gameUiState.score,
                onPlayAgain = { gameViewModel.resetGame() }
            )
        }
    }
}

// Листинг 6.22, 6.25, 6.29, 6.35
@Composable
fun GameLayout(
    currentScrambledWord: String,
    isGuessWrong: Boolean,
    userGuess: String,
    wordCount: Int,
    onUserGuessChanged: (String) -> Unit,
    onKeyboardDone: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.medium_padding)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.medium_padding)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Листинг 6.36
            Text(
                text = stringResource(R.string.word_count, wordCount, MAX_NO_OF_WORDS),
                style = MaterialTheme.typography.titleMedium
            )

            // Листинг 6.23
            Text(
                text = currentScrambledWord,
                fontSize = 45.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Text(
                text = stringResource(R.string.instructions),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            // Листинг 6.24, 6.37
            OutlinedTextField(
                value = userGuess,
                onValueChange = onUserGuessChanged,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    if (isGuessWrong) {
                        Text(stringResource(R.string.wrong_guess))
                    } else {
                        Text(stringResource(R.string.enter_your_word))
                    }
                },
                isError = isGuessWrong
            )
        }
    }
}

@Composable
fun GameStatus(score: Int, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Text(
            text = stringResource(R.string.score, score),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun FinalScoreDialog(
    score: Int,
    onPlayAgain: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = { /* Do nothing */ },
        title = { Text(text = "Congratulations!") },
        text = { Text(text = stringResource(R.string.final_score, score)) },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = { /* Exit the app */ }) {
                Text(text = "Exit")
            }
        },
        confirmButton = {
            TextButton(onClick = onPlayAgain) {
                Text(text = "Play Again")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    UnscrambleTheme {
        GameScreen()
    }
}