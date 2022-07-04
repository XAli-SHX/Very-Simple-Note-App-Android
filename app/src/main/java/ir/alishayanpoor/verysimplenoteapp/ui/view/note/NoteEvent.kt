package ir.alishayanpoor.verysimplenoteapp.ui.view.note

sealed class NoteEvent {
    object CreatedNewNote : NoteEvent()
    object EditedNote : NoteEvent()
    object DeletedNote : NoteEvent()
    data class FailedToCreateNote(val error: String) : NoteEvent()
}