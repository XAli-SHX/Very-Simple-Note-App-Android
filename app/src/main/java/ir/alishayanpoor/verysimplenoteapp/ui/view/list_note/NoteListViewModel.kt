package ir.alishayanpoor.verysimplenoteapp.ui.view.list_note

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.alishayanpoor.verysimplenoteapp.domain.repo.NoteRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {
    var state by mutableStateOf(NoteListState())
        private set
    val event = Channel<NoteListEvent>()

    init {
        loadNotes()
    }

    fun loadNotes() {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true,
                error = null
            )
            state = try {
                val notes = noteRepository.getAllNotes()
                state.copy(
                    noteList = notes,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                state.copy(
                    isLoading = false,
                    error = e.localizedMessage
                )
            }
        }
    }

    fun addNewNote() {
        viewModelScope.launch {
            event.send(NoteListEvent.CreateNewNote)
        }
    }
}