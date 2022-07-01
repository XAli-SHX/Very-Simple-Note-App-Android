package ir.alishayanpoor.verysimplenoteapp.data.remote.dto


import com.google.gson.annotations.SerializedName

data class NoteListDto(
    @SerializedName("success")
    val success: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: Data?
) {
    data class Data(
        @SerializedName("notes")
        val notes: List<Note>
    ) {
        data class Note(
            @SerializedName("id")
            val id: Int,
            @SerializedName("title")
            val title: String,
            @SerializedName("body")
            val body: String,
            @SerializedName("created_at")
            val createdAt: String,
            @SerializedName("updated_at")
            val updatedAt: String,
            @SerializedName("tags")
            val tags: List<Tag>
        ) {
            data class Tag(
                @SerializedName("tag")
                val tag: String
            )
        }
    }
}