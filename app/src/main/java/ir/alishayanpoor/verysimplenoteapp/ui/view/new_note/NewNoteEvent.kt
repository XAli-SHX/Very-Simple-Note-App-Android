package ir.alishayanpoor.verysimplenoteapp.ui.view.new_note

sealed class NewNoteEvent {
    object CreatedNewNote : NewNoteEvent()
    object EditedNote : NewNoteEvent()
    object DeletedNote : NewNoteEvent()
    data class FailedToCreateNote(val error: String) : NewNoteEvent()
}