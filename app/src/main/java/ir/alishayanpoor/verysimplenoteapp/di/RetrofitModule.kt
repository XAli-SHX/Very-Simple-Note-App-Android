package ir.alishayanpoor.verysimplenoteapp.di

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.alishayanpoor.verysimplenoteapp.data.remote.api.BASE_URL
import ir.alishayanpoor.verysimplenoteapp.data.remote.api.VerySimpleNoteAppApi
import ir.alishayanpoor.verysimplenoteapp.data.remote.api.VerySimpleNoteAppInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {

    @Provides
    @Singleton
    fun verySimpleNoteAppApi(
        interceptor: VerySimpleNoteAppInterceptor
    ): VerySimpleNoteAppApi {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(interceptor)
            .build()
        val gson = GsonBuilder()
            .setLenient()
            .create()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
            .create(VerySimpleNoteAppApi::class.java)
    }
}