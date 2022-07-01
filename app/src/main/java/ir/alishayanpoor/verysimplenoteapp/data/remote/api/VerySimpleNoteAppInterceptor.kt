package ir.alishayanpoor.verysimplenoteapp.data.remote.api

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class VerySimpleNoteAppInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json;charset=utf-8")
            .build()

        return chain.proceed(request)
    }
}