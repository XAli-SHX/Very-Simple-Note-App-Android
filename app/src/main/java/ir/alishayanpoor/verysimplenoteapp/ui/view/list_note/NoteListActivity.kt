package ir.alishayanpoor.verysimplenoteapp.ui.view.list_note

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import dagger.hilt.android.AndroidEntryPoint
import ir.alishayanpoor.verysimplenoteapp.ui.theme.VerySimpleNoteAppTheme
import ir.alishayanpoor.verysimplenoteapp.ui.view.new_note.ViewOrEditOrNewNoteActivity
import ir.alishayanpoor.verysimplenoteapp.util.collectLatestLifecycleFlowWhenStarted
import ir.alishayanpoor.verysimplenoteapp.util.exhaustive
import kotlinx.coroutines.flow.receiveAsFlow

@AndroidEntryPoint
class NoteListActivity : ComponentActivity() {
    private val viewModel: NoteListViewModel by viewModels()
    private val newNoteResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                viewModel.loadNotes()
            }
        }

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
                    Intent(this, ViewOrEditOrNewNoteActivity::class.java).also {
                        it.putExtra(ViewOrEditOrNewNoteActivity.KEY_EXTRA_VIEW_MODE, false)
                        newNoteResultLauncher.launch(it)
                    }
                }
                is NoteListEvent.ViewNote -> {
                    Intent(this, ViewOrEditOrNewNoteActivity::class.java).also {
                        it.putExtra(ViewOrEditOrNewNoteActivity.KEY_EXTRA_VIEW_MODE, true)
                        it.putExtra(ViewOrEditOrNewNoteActivity.KEY_EXTRA_NOTE, event.note)
                        newNoteResultLauncher.launch(it)
                    }
                }
            }.exhaustive
        }
    }

    @Composable
    fun MainView() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
        ) {
            SwipeRefresh(
                modifier = Modifier.fillMaxSize(),
                state = rememberSwipeRefreshState(false),
                onRefresh = { viewModel.loadNotes() }) {
                if (viewModel.state.hasError()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = viewModel.state.error.toString()
                        )
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { viewModel.loadNotes() }
                        ) {
                            Text(text = "Retry")
                        }
                    }
                } else if (viewModel.state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(40.dp),
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                    ) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            viewModel.state.noteList.forEachIndexed { index, note ->
                                item {
                                    Box(
                                        modifier = Modifier
                                            .clickable { viewModel.editNote(index) }
                                            .border(
                                                width = 0.8.dp,
                                                color = Color.Gray.copy(alpha = 0.5f),
                                                shape = RoundedCornerShape(12.dp)
                                            )
                                    ) {
                                        Text(
                                            text = note.title,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(12.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(12.dp))
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
    }
}

