package ir.alishayanpoor.verysimplenoteapp.ui.view.list_note

sealed class NoteListEvent {
    object CreateNewNote : NoteListEvent()
}