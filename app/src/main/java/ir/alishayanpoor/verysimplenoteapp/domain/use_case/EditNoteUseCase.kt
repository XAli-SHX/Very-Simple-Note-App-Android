package ir.alishayanpoor.verysimplenoteapp.domain.use_case

import ir.alishayanpoor.verysimplenoteapp.data.remote.api.VerySimpleNoteAppApi
import ir.alishayanpoor.verysimplenoteapp.data.remote.dto.NewNoteDto
import ir.alishayanpoor.verysimplenoteapp.domain.model.Note
import javax.inject.Inject

class EditNoteUseCase @Inject constructor(
    private val verySimpleNoteAppApi: VerySimpleNoteAppApi
) {
    @Throws(InvalidEditNoteException::class, Exception::class)
    suspend operator fun invoke(note: Note) {
        val ex = InvalidEditNoteException()
        if (note.id == null) {
            ex.hasError = true
            throw NoIdAssignedException
        }
        checkNoteErrors(note, ex)
        if (ex.hasError)
            throw ex
        verySimpleNoteAppApi.editNote(note.id, NewNoteDto.fromNote(note))
    }

    private fun checkNoteErrors(
        note: Note,
        ex: InvalidEditNoteException
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


    data class InvalidEditNoteException(
        var titleError: String? = null,
        var bodyError: String? = null,
        var hasError: Boolean = false,
    ) : Exception()

    object NoIdAssignedException : Exception()
}