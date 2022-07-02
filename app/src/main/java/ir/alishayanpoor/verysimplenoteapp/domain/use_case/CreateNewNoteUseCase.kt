package ir.alishayanpoor.verysimplenoteapp.domain.use_case

import ir.alishayanpoor.verysimplenoteapp.data.remote.api.VerySimpleNoteAppApi
import ir.alishayanpoor.verysimplenoteapp.data.remote.dto.NewNoteDto
import ir.alishayanpoor.verysimplenoteapp.domain.model.Note
import javax.inject.Inject

class CreateNewNoteUseCase @Inject constructor(
    private val verySimpleNoteAppApi: VerySimpleNoteAppApi
) {
    @Throws(InvalidNoteException::class, Exception::class)
    suspend operator fun invoke(note: Note) {
        val ex = InvalidNoteException()
        checkNoteErrors(note, ex)
        if (ex.hasError)
            throw ex
        verySimpleNoteAppApi.newNote(NewNoteDto.fromNote(note))
    }

    private fun checkNoteErrors(
        note: Note,
        ex: InvalidNoteException
    ) {
        note.apply {
            if (title.isBlank()) {
                ex.hasError = true
                ex.titleError = "Title could not be empty"
            }
            if (body.isBlank()) {
                ex.hasError = true
                ex.bodyError = "Body could not be empty"
            }
        }
    }


    data class InvalidNoteException(
        var titleError: String? = null,
        var bodyError: String? = null,
        var hasError: Boolean = false
    ) : Exception()
}