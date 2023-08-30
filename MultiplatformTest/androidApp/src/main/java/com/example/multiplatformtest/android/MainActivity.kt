package com.example.multiplatformtest.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.multiplatformtest.User
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val scope = rememberCoroutineScope()
            val state by viewModel.uiState.collectAsState()

            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    LaunchedEffect(Unit) {
                        scope.launch {
                            viewModel.getUsers()
                        }
                    }

                    Content(
                        state = state,
                        onRefreshClicked = { viewModel.getUsers() },
                        onAddClicked = { viewModel.addUser() },
                        onDeleteClicked = { viewModel.deleteUser(it) },
                        onUpdateClicked = { viewModel.updateUser(it) }
                    )
                }
            }
        }
    }
}

@Composable
fun Content(
    state: MainActivityUiState,
    onRefreshClicked: () -> Unit,
    onAddClicked: () -> Unit,
    onDeleteClicked: (user: User) -> Unit,
    onUpdateClicked: (user: User) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        UpperContent(
            state = state,
            onItemEditClicked = onUpdateClicked,
            onItemDeleteClicked = onDeleteClicked
        )

        LowerContent(
            onRefreshClicked = onRefreshClicked,
            onAddClicked = onAddClicked
        )
    }
}

@Composable
fun ColumnScope.UpperContent(
    state: MainActivityUiState,
    onItemEditClicked: (user: User) -> Unit,
    onItemDeleteClicked: (user: User) -> Unit
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        when (state) {
            is MainActivityUiState.Loading -> {
                CircularProgressIndicator()
            }

            is MainActivityUiState.Error -> {
                Text(text = "Error: ${state.errorMessage}")
            }

            is MainActivityUiState.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    itemsIndexed(state.users) { index, user ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "${user.id}: ${user.fullName}")

                            Spacer(modifier = Modifier.weight(1f))

                            IconButton(
                                onClick = { onItemEditClicked(user) }
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Edit,
                                    contentDescription = null
                                )
                            }
                            IconButton(
                                onClick = { onItemDeleteClicked(user) }
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Delete,
                                    contentDescription = null
                                )
                            }
                        }

                        if (index < state.users.lastIndex) {
                            Divider(color = Color.Black, thickness = 1.dp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LowerContent(
    onRefreshClicked: () -> Unit,
    onAddClicked: () -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onRefreshClicked
        ) {
            Text(text = "Fetch Users")
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onAddClicked
        ) {
            Text(text = "Create New User")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun previewSuccess() {
    Column {
        UpperContent(
            MainActivityUiState.Success(
                listOf(
                    User(1, "Test", "User"),
                    User(2, "Test", "User")
                )
            ), {}, {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun previewError() {
    Column {
        UpperContent(
            MainActivityUiState.Error("Failed to load users"), {}, {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun previewLoading() {
    Column {
        UpperContent(MainActivityUiState.Loading, {}, {})
    }
}

@Preview(showBackground = true)
@Composable
fun previewButtons() {
    Column {
        LowerContent({}, {})
    }
}
