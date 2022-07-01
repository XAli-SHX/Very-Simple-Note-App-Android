package ir.alishayanpoor.verysimplenoteapp.ui.view.list_note

import ir.alishayanpoor.verysimplenoteapp.domain.model.Note

data class NoteListState(
    val noteList: List<Note> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
) {
    fun hasError() = error != null
}