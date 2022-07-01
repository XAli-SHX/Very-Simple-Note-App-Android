package ir.alishayanpoor.verysimplenoteapp.ui.view.list_note

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import ir.alishayanpoor.verysimplenoteapp.ui.theme.VerySimpleNoteAppTheme
import ir.alishayanpoor.verysimplenoteapp.ui.view.new_note.NewNoteActivity
import ir.alishayanpoor.verysimplenoteapp.util.collectLatestLifecycleFlowWhenStarted
import ir.alishayanpoor.verysimplenoteapp.util.exhaustive
import kotlinx.coroutines.flow.receiveAsFlow

@AndroidEntryPoint
class NoteListActivity : ComponentActivity() {
    private val viewModel: NoteListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeObservers()
        setContent {
            VerySimpleNoteAppTheme {
                MainView()
            }
        }
    }

    private fun subscribeObservers() {
        collectLatestLifecycleFlowWhenStarted(
            viewModel.event.receiveAsFlow()
        ) { event ->
            when (event) {
                NoteListEvent.CreateNewNote -> {
                    Intent(this, NewNoteActivity::class.java).also {
                        startActivity(it)
                    }
                }
            }.exhaustive
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun MainView() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
        ) {
            if (viewModel.state.hasError()) {
                Text(text = viewModel.state.error.toString())
            } else if (viewModel.state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(40.dp),
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    viewModel.state.noteList.forEach { note ->
                        item {
                            Text(
                                text = note.title,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
                FloatingActionButton(
                    modifier = Modifier
                        .align(Alignment.BottomEnd),
                    onClick = { viewModel.addNewNote() },
                ) {
                    Icon(Icons.Filled.Add, "Add new note")
                }
            }
        }
    }
}

