package ir.alishayanpoor.verysimplenoteapp.ui.view.new_note

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.alishayanpoor.verysimplenoteapp.domain.model.Note
import ir.alishayanpoor.verysimplenoteapp.domain.repo.NoteRepository
import ir.alishayanpoor.verysimplenoteapp.domain.use_case.CreateNewNoteUseCase
import ir.alishayanpoor.verysimplenoteapp.util.exhaustive
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewNoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val createNewNoteUseCase: CreateNewNoteUseCase
) : ViewModel() {

    var state by mutableStateOf(NewNoteState())
        private set

    val event = Channel<NewNoteEvent>()

    fun onAction(action: NewNoteAction) {
        when (action) {
            is NewNoteAction.Body -> enterBody(action.body)
            is NewNoteAction.Create -> createNewNote()
            is NewNoteAction.Tag -> enterTag(action.tag)
            is NewNoteAction.Title -> enterTitle(action.title)
            is NewNoteAction.SubmitTag -> submitTag()
            is NewNoteAction.RemoveTag -> removeTag(action.index)
        }.exhaustive
    }

    private fun removeTag(index: Int) {
        if (state.viewMode)
            return
        val prevTags = state.tags.toMutableList()
        prevTags.removeAt(index)
        state = state.copy(
            tags = prevTags.toList()
        )
    }

    private fun enterTag(tag: String) {
        state = state.copy(
            tag = tag
        )
    }

    private fun enterTitle(title: String) {
        state = state.copy(title = title)
    }

    private fun submitTag() {
        if (state.tag.isBlank())
            return
        if (state.tags.any { it == state.tag })
            return
        val prevTags = state.tags.toMutableList()
        prevTags.add(state.tag)
        state = state.copy(
            tags = prevTags.toList(),
            tag = ""
        )
    }

    private fun createNewNote() {
        state = state.copy(
            isSendingNote = true,
            titleError = null,
            bodyError = null,
        )
        viewModelScope.launch {
            state.apply {
                try {
                    createNewNoteUseCase(
                        Note(
                            title, body, tags
                        )
                    )
                    state = state.copy(
                        isSendingNote = false
                    )
                    event.send(NewNoteEvent.CreatedNewNote)
                } catch (e: CreateNewNoteUseCase.InvalidNoteException) {
                    state = state.copy(
                        isSendingNote = false,
                        titleError = e.titleError,
                        bodyError = e.bodyError,
                    )
                } catch (e: Exception) {
                    state = state.copy(
                        isSendingNote = false,
                    )
                    event.send(NewNoteEvent.FailedToCreateNote(e.localizedMessage))
                }
            }
        }
    }

    private fun enterBody(body: String) {
        state = state.copy(
            body = body
        )
    }

    fun setViewMode(inViewMode: Boolean, note: Note?) {
        state = state.copy(
            viewMode = inViewMode,
            title = note?.title ?: "",
            body = note?.body ?: "",
            tags = note?.tags ?: emptyList(),
        )
    }
}