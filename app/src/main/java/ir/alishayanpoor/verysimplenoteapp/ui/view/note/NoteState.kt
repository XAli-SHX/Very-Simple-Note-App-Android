package ir.alishayanpoor.verysimplenoteapp.ui.view.note

data class NoteState(
    val id: Int? = null,
    val title: String = "",
    val viewMode: ViewMode = ViewMode.New,
    val deleteRequested: Boolean = false,
    val body: String = "",
    val tag: String = "",
    val tags: List<String> = emptyList(),
    val isSendingNote: Boolean = false,
    val isDeletingNote: Boolean = false,
    val isEditingNote: Boolean = false,
    val titleError: String? = null,
    val bodyError: String? = null,
) {
    enum class ViewMode {
        View, Edit, New
    }
}