package ir.alishayanpoor.verysimplenoteapp.data.repo

import ir.alishayanpoor.verysimplenoteapp.data.remote.api.VerySimpleNoteAppApi
import ir.alishayanpoor.verysimplenoteapp.domain.model.Note
import ir.alishayanpoor.verysimplenoteapp.domain.repo.NoteRepository
import javax.inject.Inject

class NoteRepoRemote @Inject constructor(
    private val api: VerySimpleNoteAppApi
) : NoteRepository {

    @Throws(Exception::class)
    override suspend fun getAllNotes(): List<Note> {
        val response = api.getAllNotes()
        if (response.isSuccessful) {
            val result = response.body()
            result?.let {
                return it.toNoteList()
            }
        }
        throw Exception("Some thing went wrong")
    }

    override suspend fun createNewNote(note: Note) {
        TODO("Not yet implemented")
    }
}