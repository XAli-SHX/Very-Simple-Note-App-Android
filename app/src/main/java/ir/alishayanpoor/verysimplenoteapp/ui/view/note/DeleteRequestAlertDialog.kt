package ir.alishayanpoor.verysimplenoteapp.ui.view.note

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ir.alishayanpoor.verysimplenoteapp.R

@Composable
fun DeleteRequestAlertDialog(
    viewModel: NoteViewModel
) {
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