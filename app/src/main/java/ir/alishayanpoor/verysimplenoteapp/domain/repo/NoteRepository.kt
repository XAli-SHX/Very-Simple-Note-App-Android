package ir.alishayanpoor.verysimplenoteapp.domain.repo

import ir.alishayanpoor.verysimplenoteapp.domain.model.Note

interface NoteRepository {
    suspend fun getAllNotes(): List<Note>
    suspend fun createNewNote(note: Note)
    suspend fun deleteNote(id: Int)
    suspend fun editNote(id: Int, note: Note)
}