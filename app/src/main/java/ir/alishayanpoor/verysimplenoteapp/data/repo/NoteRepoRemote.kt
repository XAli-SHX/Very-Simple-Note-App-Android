package ir.alishayanpoor.verysimplenoteapp.data.repo

import ir.alishayanpoor.verysimplenoteapp.data.remote.api.VerySimpleNoteAppApi
import ir.alishayanpoor.verysimplenoteapp.data.remote.dto.NewNoteDto
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

    @Throws(Exception::class)
    override suspend fun createNewNote(note: Note) {
        val response = api.newNote(NewNoteDto.fromNote(note))
        if (response.isSuccessful) {
            val result = response.body()
            result?.let {
                return
            }
        }
        throw Exception("Some thing went wrong")
    }

    @Throws(Exception::class)
    override suspend fun deleteNote(id: Int) {
        val response = api.deleteNote()
        if (response.isSuccessful) {
            val result = response.body()
            result?.let {
                return
            }
        }
        throw Exception("Some thing went wrong")
    }

    @Throws(Exception::class)
    override suspend fun editNote(id: Int, note: Note) {
        val response = api.editNote(NewNoteDto.fromNote(note))
        if (response.isSuccessful) {
            val result = response.body()
            result?.let {
                return
            }
        }
        throw Exception("Some thing went wrong")
    }
}