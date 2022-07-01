package ir.alishayanpoor.verysimplenoteapp.domain.model

data class Note(
    val title: String,
    val body: String,
    val tags: List<String>,
)