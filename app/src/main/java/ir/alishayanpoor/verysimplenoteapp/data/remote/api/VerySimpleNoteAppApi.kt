package ir.alishayanpoor.verysimplenoteapp.data.remote.api

import com.google.gson.JsonObject
import ir.alishayanpoor.verysimplenoteapp.data.remote.dto.NewNoteDto
import ir.alishayanpoor.verysimplenoteapp.data.remote.dto.NoteListDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

const val BASE_URL = "https://api.alishayanpoor.ir/Very-Simple-Note-App/api/v1/"

interface VerySimpleNoteAppApi {
    @GET("notes")
    suspend fun getAllNotes(): Response<NoteListDto>

    @POST("note")
    suspend fun newNote(
        @Body newNoteDto: NewNoteDto
    ): Response<JsonObject>

    @POST("note/edit/{id}")
    suspend fun editNote(
        @Body newNoteDto: NewNoteDto
    ): Response<JsonObject>

    @POST("note/delete/{id}")
    suspend fun deleteNote(): Response<JsonObject>
}