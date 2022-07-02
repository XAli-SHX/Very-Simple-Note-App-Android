package ir.alishayanpoor.verysimplenoteapp.ui.view.new_note

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import ir.alishayanpoor.verysimplenoteapp.domain.model.Note
import ir.alishayanpoor.verysimplenoteapp.ui.component.OutlinedTextFieldValidation
import ir.alishayanpoor.verysimplenoteapp.ui.theme.VerySimpleNoteAppTheme
import ir.alishayanpoor.verysimplenoteapp.util.collectLatestLifecycleFlowWhenStarted
import ir.alishayanpoor.verysimplenoteapp.util.exhaustive
import kotlinx.coroutines.flow.receiveAsFlow

@AndroidEntryPoint
class ViewOrNewNoteActivity : ComponentActivity() {
    companion object {
        const val KEY_EXTRA_VIEW_MODE = "viewMode"
        const val KEY_EXTRA_NOTE = "note"
    }

    private val viewModel: NewNoteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeObservers()
        configExtras()
        setContent {
            MainView()
        }
    }

    private fun configExtras() {
        val isInViewMode = intent.getBooleanExtra(KEY_EXTRA_VIEW_MODE, false)
        val note: Note? = intent.extras?.getParcelable(KEY_EXTRA_NOTE)
        viewModel.setViewMode(isInViewMode, note)
    }

    private fun subscribeObservers() {
        collectLatestLifecycleFlowWhenStarted(viewModel.event.receiveAsFlow()) {
            when (it) {
                is NewNoteEvent.CreatedNewNote -> finishAndLetNoteListRefresh()
                is NewNoteEvent.FailedToCreateNote ->
                    Toast.makeText(this, it.error, Toast.LENGTH_LONG).show()
            }.exhaustive
        }
    }

    private fun finishAndLetNoteListRefresh() {
        setResult(RESULT_OK)
        finish()
    }

    @Preview(showBackground = true)
    @Composable
    fun MainView() {
        VerySimpleNoteAppTheme {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                OutlinedTextFieldValidation(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    label = { Text(text = "Title") },
                    value = viewModel.state.title,
                    isError = viewModel.state.titleError != null,
                    error = viewModel.state.titleError ?: "",
                    readOnly = viewModel.state.viewMode,
                    onValueChange = { viewModel.onAction(NewNoteAction.Title(it)) },
                )
                OutlinedTextFieldValidation(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    label = { Text(text = "Body") },
                    value = viewModel.state.body,
                    isError = viewModel.state.bodyError != null,
                    error = viewModel.state.bodyError ?: "",
                    readOnly = viewModel.state.viewMode,
                    onValueChange = { viewModel.onAction(NewNoteAction.Body(it)) },
                )
                if (!viewModel.state.viewMode) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextFieldValidation(
                            modifier = Modifier
                                .weight(2f)
                                .align(Alignment.CenterVertically)
                                .padding(12.dp),
                            label = { Text(text = "Tag") },
                            value = viewModel.state.tag,
                            readOnly = viewModel.state.viewMode,
                            onValueChange = { viewModel.onAction(NewNoteAction.Tag(it)) },
                        )

                        Button(
                            modifier = Modifier
                                .weight(1f)
                                .align(Alignment.CenterVertically),
                            onClick = { viewModel.onAction(NewNoteAction.SubmitTag) }) {
                            Text(text = "Add Tag")
                        }
                    }
                }
                LazyRow {
                    viewModel.state.tags.forEachIndexed { i: Int, it: String ->
                        item {
                            Button(
                                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
                                shape = RoundedCornerShape(50),
                                onClick = { viewModel.onAction(NewNoteAction.RemoveTag(i)) }) {
                                Text(text = it)
                                if (!viewModel.state.viewMode) {
                                    Icon(Icons.Filled.Close, contentDescription = "Remove Tag")
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                        }
                    }
                }
                if (!viewModel.state.viewMode) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        modifier = Modifier
                            .fillMaxWidth(),
                        onClick = { viewModel.onAction(NewNoteAction.Create) }) {
                        if (viewModel.state.isSendingNote) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.fillMaxHeight()
                            )
                        } else {
                            Text(text = "Create Note")
                        }
                    }
                }
            }
        }
    }
}
