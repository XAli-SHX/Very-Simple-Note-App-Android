package ir.alishayanpoor.verysimplenoteapp.data.remote.dto


import com.google.gson.annotations.SerializedName

data class NewNoteDto(
    @SerializedName("title")
    val title: String,
    @SerializedName("body")
    val body: String,
    @SerializedName("tags")
    val tags: List<String>
)