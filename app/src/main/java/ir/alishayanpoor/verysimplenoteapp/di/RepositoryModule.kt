package ir.alishayanpoor.verysimplenoteapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.alishayanpoor.verysimplenoteapp.data.remote.api.VerySimpleNoteAppApi
import ir.alishayanpoor.verysimplenoteapp.data.repo.NoteRepoRemote
import ir.alishayanpoor.verysimplenoteapp.domain.repo.NoteRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun noteRepo(
        verySimpleNoteAppApi: VerySimpleNoteAppApi
    ): NoteRepository {
        return NoteRepoRemote(verySimpleNoteAppApi)
    }
}