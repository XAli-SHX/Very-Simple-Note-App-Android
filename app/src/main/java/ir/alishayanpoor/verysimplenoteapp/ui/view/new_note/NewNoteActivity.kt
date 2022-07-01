package ir.alishayanpoor.verysimplenoteapp.ui.view.new_note

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import ir.alishayanpoor.verysimplenoteapp.ui.theme.VerySimpleNoteAppTheme

@AndroidEntryPoint
class NewNoteActivity : ComponentActivity() {
    private val viewModel: NewNoteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainView()
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun MainView() {
        VerySimpleNoteAppTheme {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    label = { Text(text = "Title") },
                    value = viewModel.state.title,
                    onValueChange = { viewModel.onAction(NewNoteAction.Title(it)) }
                )
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    label = { Text(text = "Body") },
                    value = viewModel.state.body,
                    onValueChange = { viewModel.onAction(NewNoteAction.Body(it)) }
                )
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        label = { Text(text = "Tag", color = MaterialTheme.colors.primary) },
                        value = viewModel.state.title,
                        onValueChange = { viewModel.onAction(NewNoteAction.Tag(it)) },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            unfocusedBorderColor = MaterialTheme.colors.primary,
                            focusedBorderColor = MaterialTheme.colors.primaryVariant,
                            placeholderColor = Color.Gray
                        )
                    )
                    Button(onClick = { /*TODO*/ }) {

                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = { viewModel.onAction(NewNoteAction.Create) }) {
                    Text(text = "Create Note")
                }
            }
        }
    }
}
