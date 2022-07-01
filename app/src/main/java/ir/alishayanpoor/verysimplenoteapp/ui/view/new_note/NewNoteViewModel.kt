package ir.alishayanpoor.verysimplenoteapp.ui.view.new_note

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.alishayanpoor.verysimplenoteapp.domain.repo.NoteRepository
import ir.alishayanpoor.verysimplenoteapp.util.exhaustive
import javax.inject.Inject

@HiltViewModel
class NewNoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {

    var state by mutableStateOf(NewNoteState())
        private set

    fun onAction(action: NewNoteAction) {
        when (action) {
            is NewNoteAction.Body -> enterBody(action.body)
            is NewNoteAction.Create -> createNewNote()
            is NewNoteAction.Tag -> enterTag(action.tag)
            is NewNoteAction.Title -> enterTitle(action.title)
            is NewNoteAction.SubmitTag -> submitTag(action.tag)
            is NewNoteAction.RemoveTag -> removeTag(action.tag)
        }.exhaustive
    }

    private fun removeTag(tag: String) {
        TODO("Not yet implemented")
    }

    private fun enterTag(tag: String) {
        TODO("Not yet implemented")
    }

    private fun enterTitle(title: String) {
        state = state.copy(title = title)
    }

    private fun submitTag(tag: String) {
        val prevTags = state.tags.toMutableList()
        prevTags.add(tag)
        state = state.copy(
            tags = prevTags.toList()
        )
    }

    private fun createNewNote() {

    }

    private fun enterBody(body: String) {
        state = state.copy(
            body = body
        )
    }
}