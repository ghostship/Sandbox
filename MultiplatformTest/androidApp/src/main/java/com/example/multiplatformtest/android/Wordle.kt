package com.example.multiplatformtest.android

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.multiplatformtest.android.Config.GUESS_COUNT
import com.example.multiplatformtest.android.Config.LETTER_COUNT

@Composable
fun WordleScreen(viewModel: WordleViewModel = viewModel()) {
    val gameUiState by viewModel.uiState.collectAsState()

    ContentView(
        guesses = gameUiState.guesses,
        onLetterClick = { viewModel.onLetterClicked(it) },
        onSubmitClick = { viewModel.onSubmitClick() },
        onDeleteClick = { viewModel.onDeleteClick() }
    )
}

@Composable
fun ContentView(
    guesses: List<List<AppState.Letter>>,
    onLetterClick: (letter: Char) -> Unit,
    onSubmitClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        for (i in 0 until GUESS_COUNT) {
            GuessRow(guess = guesses[i])

            if (i < GUESS_COUNT - 1) {
                Spacer(Modifier.height(10.dp))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Keyboard(
            onLetterClick = onLetterClick,
            onSubmitClick = onSubmitClick,
            onDeleteClick = onDeleteClick
        )
    }
}

@Composable
fun GuessRow(guess: List<AppState.Letter>) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        for (i in 0 until LETTER_COUNT) {

            guess.getOrNull(i)?.let {
                val bgColor = when (it.state) {
                    AppState.LetterState.EMPTY -> Color.White
                    AppState.LetterState.CORRECT -> Color.Green
                    AppState.LetterState.INCORRECT -> Color.Gray
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(30.dp, 25.dp)
                        .border(1.dp, Color.Black, RoundedCornerShape(10.dp))
                        .background(bgColor, RoundedCornerShape(10.dp))
                ) {
                    Text(text = it.letter?.toString()?.uppercase() ?: "")
                }

                if (i < LETTER_COUNT - 1) {
                    Spacer(Modifier.width(2.dp))
                }
            }
        }
    }
}

@Composable
fun Keyboard(
    onLetterClick: (letter: Char) -> Unit,
    onSubmitClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val letters = listOf(
        "qwertyuiop",
        "asdfghjkl",
        "zxcvbnm"
    )

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(onClick = onSubmitClick) {
            Text("Guess")
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            letters.forEach { row ->
                Row {
                    row.forEachIndexed { index, letter ->
                        Button(
                            onClick = { onLetterClick(letter) },
                            modifier = Modifier.width(40.dp)
                        ) {
                            Text(text = letter.toString())
                        }

                        if (index < row.lastIndex) {
                            Spacer(modifier = Modifier.width(6.dp))
                        }
                    }
                }
            }
        }


        Button(onClick = onDeleteClick) {
            Text("Backspace")
        }
    }
}

@Preview(device = "spec:parent=pixel_5,orientation=landscape")
@Composable
fun ContentView_Preview() {
    val guess1 = listOf(
        AppState.Letter.Guess('a', AppState.LetterState.CORRECT),
        AppState.Letter.Guess('f', AppState.LetterState.INCORRECT),
        AppState.Letter.Guess('g', AppState.LetterState.INCORRECT),
        AppState.Letter.Guess('p', AppState.LetterState.INCORRECT),
        AppState.Letter.Empty,
        AppState.Letter.Empty,
        AppState.Letter.Empty,
        AppState.Letter.Empty,
        AppState.Letter.Empty,
        AppState.Letter.Empty,
        AppState.Letter.Empty,
        AppState.Letter.Empty,
        AppState.Letter.Empty,
        AppState.Letter.Empty,
        AppState.Letter.Empty,
        AppState.Letter.Empty,
        AppState.Letter.Empty,
        AppState.Letter.Empty
    )

    ContentView(
        guesses = listOf(
            guess1,
            guess1,
            guess1,
            guess1,
            guess1,
            guess1
        ),
        onLetterClick = {},
        onSubmitClick = {},
        onDeleteClick = {}
    )
}

private object Config {
    const val LETTER_COUNT = 18
    const val GUESS_COUNT = 6
}

data class AppState(
    val guesses: List<List<Letter>>
) {

    sealed class Letter(
        open val letter: Char?,
        open val state: LetterState
    ) {
        data class Guess(override val letter: Char, override val state: LetterState) :
            Letter(letter, state)

        object Empty : Letter(null, LetterState.EMPTY)
    }

    enum class LetterState {
        EMPTY,
        CORRECT,
        INCORRECT
    }
}