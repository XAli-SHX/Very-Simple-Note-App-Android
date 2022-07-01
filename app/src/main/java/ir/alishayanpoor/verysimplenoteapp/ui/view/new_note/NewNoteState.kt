package ir.alishayanpoor.verysimplenoteapp.ui.view.new_note

data class NewNoteState(
    val title: String = "",
    val body: String = "",
    val tag: String = "",
    val tags: List<String> = emptyList(),
    val isSendingNote: Boolean = false,
    val error: String? = null
)