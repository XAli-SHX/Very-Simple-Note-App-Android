package ir.alishayanpoor.verysimplenoteapp.ui.view.new_note

data class NewNoteState(
    val title: String = "",
    val viewMode: Boolean = false,
    val body: String = "",
    val tag: String = "",
    val tags: List<String> = emptyList(),
    val isSendingNote: Boolean = false,
    val titleError: String? = null,
    val bodyError: String? = null,
)