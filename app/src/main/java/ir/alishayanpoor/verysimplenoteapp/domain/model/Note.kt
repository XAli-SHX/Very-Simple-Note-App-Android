package ir.alishayanpoor.verysimplenoteapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Note(
    val title: String,
    val body: String,
    val tags: List<String>,
) : Parcelable