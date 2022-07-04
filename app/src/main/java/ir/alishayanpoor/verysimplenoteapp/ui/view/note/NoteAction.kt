package ir.alishayanpoor.verysimplenoteapp.ui.view.note

sealed class NoteAction {
    data class Title(val title: String) : NoteAction()
    data class Body(val body: String) : NoteAction()
    data class Tag(val tag: String) : NoteAction()
    object SubmitTag : NoteAction()
    data class RemoveTag(val index: Int) : NoteAction()
    object Create : NoteAction()
    object Edit : NoteAction()
    object Delete : NoteAction()
}