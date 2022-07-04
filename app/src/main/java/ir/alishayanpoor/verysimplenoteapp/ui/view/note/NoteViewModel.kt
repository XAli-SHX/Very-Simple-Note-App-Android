package ir.alishayanpoor.verysimplenoteapp.ui.view.note

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.alishayanpoor.verysimplenoteapp.domain.model.Note
import ir.alishayanpoor.verysimplenoteapp.domain.repo.NoteRepository
import ir.alishayanpoor.verysimplenoteapp.domain.use_case.CreateNewNoteUseCase
import ir.alishayanpoor.verysimplenoteapp.domain.use_case.EditNoteUseCase
import ir.alishayanpoor.verysimplenoteapp.util.exhaustive
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val createNewNoteUseCase: CreateNewNoteUseCase,
    private val editNoteUseCase: EditNoteUseCase,
) : ViewModel() {

    var state by mutableStateOf(NoteState())
        private set

    val event = Channel<NoteEvent>()

    fun onAction(action: NoteAction) {
        when (action) {
            is NoteAction.Body -> enterBody(action.body)
            is NoteAction.Create -> createNewNote()
            is NoteAction.Tag -> enterTag(action.tag)
            is NoteAction.Title -> enterTitle(action.title)
            is NoteAction.SubmitTag -> submitTag()
            is NoteAction.RemoveTag -> removeTag(action.index)
            NoteAction.Delete -> deleteNote()
            NoteAction.Edit -> editNote()
        }.exhaustive
    }

    private fun removeTag(index: Int) {
        if (state.viewMode == NoteState.ViewMode.View)
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
                            id, title, body, tags
                        )
                    )
                    event.send(NoteEvent.CreatedNewNote)
                } catch (e: CreateNewNoteUseCase.InvalidNoteException) {
                    state = state.copy(
                        titleError = e.titleError,
                        bodyError = e.bodyError,
                    )
                } catch (e: Exception) {
                    event.send(NoteEvent.FailedToCreateNote(e.localizedMessage.orEmpty()))
                } finally {
                    state = state.copy(
                        isSendingNote = false
                    )
                }
            }
        }
    }

    private fun editNote() {
        state = state.copy(
            isEditingNote = true,
            titleError = null,
            bodyError = null,
        )
        viewModelScope.launch {
            state.apply {
                try {
                    editNoteUseCase(
                        Note(
                            id, title, body, tags
                        )
                    )
                    state = state.copy(
                        isEditingNote = false
                    )
                    event.send(NoteEvent.EditedNote)
                } catch (e: EditNoteUseCase.InvalidEditNoteException) {
                    state = state.copy(
                        isEditingNote = false,
                        titleError = e.titleError,
                        bodyError = e.bodyError,
                    )
                } catch (e: EditNoteUseCase.NoIdAssignedException) {
                    event.send(NoteEvent.FailedToCreateNote("No id assigned - internal error"))
                } catch (e: Exception) {
                    state = state.copy(
                        isEditingNote = false,
                    )
                    event.send(NoteEvent.FailedToCreateNote(e.localizedMessage.orEmpty()))
                }
            }
        }
    }

    private fun enterBody(body: String) {
        state = state.copy(
            body = body
        )
    }

    fun setViewMode(inViewMode: NoteState.ViewMode, note: Note?) {
        state = state.copy(
            viewMode = inViewMode,
            id = note?.id,
            title = note?.title ?: "",
            body = note?.body ?: "",
            tags = note?.tags ?: emptyList(),
        )
    }

    fun editNoteRequested() {
        state = state.copy(
            viewMode = NoteState.ViewMode.Edit
        )
    }

    fun cancelEditNote() {
        state = state.copy(
            viewMode = NoteState.ViewMode.View
        )
    }

    fun deleteNoteRequest() {
        state = state.copy(
            deleteRequested = true
        )
    }

    fun cancelDelete() {
        state = state.copy(
            deleteRequested = false,
            isDeletingNote = false,
        )
    }

    fun deleteNote() {
        viewModelScope.launch {
            state = state.copy(
                isDeletingNote = true,
                isSendingNote = false,
                isEditingNote = false,
            )
            try {
                state.id?.let {
                    noteRepository.deleteNote(it)
                } ?: kotlin.run {
                    event.send(NoteEvent.FailedToCreateNote("No id assigned - internal error"))
                }
            } catch (e: Exception) {
                event.send(NoteEvent.FailedToCreateNote(e.localizedMessage.orEmpty()))
            } finally {
                state = state.copy(
                    isDeletingNote = false,
                )
                event.send(NoteEvent.DeletedNote)
            }
        }
    }
}