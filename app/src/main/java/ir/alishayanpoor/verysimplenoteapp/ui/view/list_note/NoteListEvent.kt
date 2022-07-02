package ir.alishayanpoor.verysimplenoteapp.ui.view.list_note

import ir.alishayanpoor.verysimplenoteapp.domain.model.Note

sealed class NoteListEvent {
    object CreateNewNote : NoteListEvent()
    data class ViewNote(val note: Note) : NoteListEvent()
}