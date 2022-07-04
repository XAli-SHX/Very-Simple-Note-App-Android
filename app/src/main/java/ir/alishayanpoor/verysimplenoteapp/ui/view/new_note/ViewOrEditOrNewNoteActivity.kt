package ir.alishayanpoor.verysimplenoteapp.ui.view.new_note

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import ir.alishayanpoor.verysimplenoteapp.R
import ir.alishayanpoor.verysimplenoteapp.domain.model.Note
import ir.alishayanpoor.verysimplenoteapp.ui.component.OutlinedTextFieldValidation
import ir.alishayanpoor.verysimplenoteapp.ui.theme.VerySimpleNoteAppTheme
import ir.alishayanpoor.verysimplenoteapp.util.collectLatestLifecycleFlowWhenStarted
import ir.alishayanpoor.verysimplenoteapp.util.exhaustive
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class ViewOrEditOrNewNoteActivity : ComponentActivity() {
    companion object {
        const val KEY_EXTRA_VIEW_MODE = "viewMode"
        const val KEY_EXTRA_NOTE = "note"
    }

    private val viewModel: NewNoteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeObservers()
        if (savedInstanceState == null)
            configExtras()
        setContent {
            MainView()
        }
    }

    private fun configExtras() {
        val isInViewMode = intent.getBooleanExtra(KEY_EXTRA_VIEW_MODE, false)
        val note: Note? = intent.extras?.getParcelable(KEY_EXTRA_NOTE)
        if (isInViewMode)
            viewModel.setViewMode(NewNoteState.ViewMode.View, note)
    }

    private fun subscribeObservers() {
        collectLatestLifecycleFlowWhenStarted(viewModel.event.receiveAsFlow()) {
            when (it) {
                is NewNoteEvent.CreatedNewNote -> finishAndLetNoteListRefresh()
                is NewNoteEvent.FailedToCreateNote -> showFailedMessage(it)
                is NewNoteEvent.EditedNote -> finishAndLetNoteListRefresh()
                is NewNoteEvent.DeletedNote -> finishAndLetNoteListRefresh()
            }.exhaustive
        }
    }

    private suspend fun showFailedMessage(it: NewNoteEvent.FailedToCreateNote) {
        withContext(Dispatchers.Main) {
            Toast.makeText(baseContext, it.error, Toast.LENGTH_LONG).show()
        }
    }

    private fun finishAndLetNoteListRefresh() {
        setResult(RESULT_OK)
        finish()
    }

    @Composable
    fun MainView() {
        VerySimpleNoteAppTheme {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                EditAndDeleteIcons()
                DeleteRequestAlertDialog()
                OutlinedTextFieldValidation(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    label = { Text(text = "Title") },
                    value = viewModel.state.title,
                    isError = viewModel.state.titleError != null,
                    error = viewModel.state.titleError ?: "",
                    readOnly = isInViewMode(),
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
                    readOnly = isInViewMode(),
                    onValueChange = { viewModel.onAction(NewNoteAction.Body(it)) },
                )
                TagAdder()
                TagList()
                CreateEditNoteButton()
            }
        }
    }

    @Composable
    private fun DeleteRequestAlertDialog() {
        if (viewModel.state.deleteRequested) {
            AlertDialog(onDismissRequest = { viewModel.cancelDelete() },
                title = {
                    Row(Modifier.fillMaxWidth()) {
                        Icon(
                            Icons.Filled.Delete,
                            contentDescription = "Delete Confirmation"
                        )
                        Text(text = "Delete Note")
                    }
                }, text = {
                    Text(text = "Are you sure about that!?")
                }, buttons = {
                    Row(Modifier.fillMaxWidth()) {
                        OutlinedButton(
                            modifier = Modifier
                                .weight(1f)
                                .padding(6.dp),
                            onClick = { viewModel.cancelDelete() }) {
                            Text(text = "Cancel")
                        }
                        Button(
                            modifier = Modifier
                                .weight(1f)
                                .padding(6.dp),
                            onClick = { viewModel.deleteNote() }) {
                            if (viewModel.state.isDeletingNote) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.fillMaxHeight()
                                )
                            } else {
                                Text(text = "Delete")
                                Spacer(modifier = Modifier.width(12.dp))
                                Image(
                                    modifier = Modifier.size(24.dp),
                                    painter = painterResource(id = R.drawable.john_cena),
                                    contentDescription = "John cena is saying are you sure about that"
                                )
                            }
                        }
                    }
                }
            )
        }
    }

    @Composable
    private fun CreateEditNoteButton() {
        if (!isInViewMode()) {
            Spacer(modifier = Modifier.height(20.dp))
            if (viewModel.state.viewMode == NewNoteState.ViewMode.Edit) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { viewModel.onAction(NewNoteAction.Edit) }) {
                    if (viewModel.state.isSendingNote || viewModel.state.isEditingNote) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.fillMaxHeight()
                        )
                    } else {
                        Text(text = "Edit Note")
                    }
                }
            } else {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { viewModel.onAction(NewNoteAction.Create) }) {
                    if (viewModel.state.isSendingNote || viewModel.state.isEditingNote) {
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

    @Composable
    private fun TagList() {
        LazyRow {
            viewModel.state.tags.forEachIndexed { i: Int, it: String ->
                item {
                    Button(
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
                        shape = RoundedCornerShape(50),
                        onClick = { viewModel.onAction(NewNoteAction.RemoveTag(i)) }) {
                        Text(text = it)
                        if (!isInViewMode()) {
                            Icon(Icons.Filled.Close, contentDescription = "Remove Tag")
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                }
            }
        }
    }

    @Composable
    private fun TagAdder() {
        if (!isInViewMode()) {
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextFieldValidation(
                    modifier = Modifier
                        .weight(2f)
                        .align(Alignment.CenterVertically)
                        .padding(12.dp),
                    label = { Text(text = "Tag") },
                    value = viewModel.state.tag,
                    readOnly = isInViewMode(),
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
    }

    @Preview(showBackground = true)
    @Composable
    private fun EditAndDeleteIcons() {
        if (viewModel.state.viewMode == NewNoteState.ViewMode.Edit) {
            IconButton(
                onClick = { viewModel.cancelEditNote() }) {
                Icon(Icons.Filled.Close, contentDescription = "Cancel editing")
            }
        }
        if (viewModel.state.viewMode == NewNoteState.ViewMode.View) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                IconButton(
                    onClick = { viewModel.editNoteRequested() }) {
                    Icon(Icons.Filled.Edit, contentDescription = "Edit Note")
                }
                IconButton(
                    onClick = { viewModel.deleteNoteRequest() }) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = "Delete Note",
                    )
                }
            }
        }
    }

    private fun isInViewMode() = viewModel.state.viewMode == NewNoteState.ViewMode.View

}
