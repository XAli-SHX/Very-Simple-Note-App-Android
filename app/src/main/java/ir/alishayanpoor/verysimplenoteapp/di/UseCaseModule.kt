package ir.alishayanpoor.verysimplenoteapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.alishayanpoor.verysimplenoteapp.data.remote.api.VerySimpleNoteAppApi
import ir.alishayanpoor.verysimplenoteapp.domain.use_case.CreateNewNoteUseCase
import ir.alishayanpoor.verysimplenoteapp.domain.use_case.EditNoteUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Singleton
    @Provides
    fun createNewNoteUseCase(verySimpleNoteAppApi: VerySimpleNoteAppApi): CreateNewNoteUseCase {
        return CreateNewNoteUseCase(verySimpleNoteAppApi)
    }

    @Singleton
    @Provides
    fun editNoteUseCase(verySimpleNoteAppApi: VerySimpleNoteAppApi): EditNoteUseCase {
        return EditNoteUseCase(verySimpleNoteAppApi)
    }
}