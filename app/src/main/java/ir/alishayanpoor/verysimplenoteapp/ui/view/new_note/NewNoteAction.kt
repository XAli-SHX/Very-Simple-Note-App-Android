package ir.alishayanpoor.verysimplenoteapp.ui.view.new_note

sealed class NewNoteAction {
    data class Title(val title: String) : NewNoteAction()
    data class Body(val body: String) : NewNoteAction()
    data class Tag(val tag: String) : NewNoteAction()
    object Create : NewNoteAction()
}