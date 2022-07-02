package ir.alishayanpoor.verysimplenoteapp.data.remote.dto


import com.google.gson.annotations.SerializedName
import ir.alishayanpoor.verysimplenoteapp.domain.model.Note

data class NewNoteDto(
    @SerializedName("title")
    val title: String,
    @SerializedName("body")
    val body: String,
    @SerializedName("tags")
    val tags: List<String>
) {
    companion object {
        fun fromNote(note: Note): NewNoteDto {
            return NewNoteDto(
                note.title,
                note.body,
                note.tags,
            )
        }
    }
}