package ir.alishayanpoor.verysimplenoteapp.ui.view.new_note

data class NewNoteState(
    val id: Int? = null,
    val title: String = "",
    val viewMode: ViewMode = ViewMode.New,
    val body: String = "",
    val tag: String = "",
    val tags: List<String> = emptyList(),
    val isSendingNote: Boolean = false,
    val titleError: String? = null,
    val bodyError: String? = null,
) {
    enum class ViewMode {
        View, Edit, New
    }
}