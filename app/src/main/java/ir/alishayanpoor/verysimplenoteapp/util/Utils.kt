package ir.alishayanpoor.verysimplenoteapp.util

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

fun <T> T.log(tag: String = "Ali"): T {
    Utils.log(this.toString(), tag)
    return this
}

fun <T> T.jsonLog(tag: String = "Ali"): T {
    Utils.jsonLog(this, tag)
    return this
}

val <T> T.exhaustive: T
    get() = this

inline fun <reified T> String?.parseNavigationData(): T? {
    val data = this ?: return null
    return try {
        Gson().fromJson(data, T::class.java)
    } catch (e: JsonParseException) {
        null
    }
}

fun <T> ComponentActivity.collectLatestLifecycleFlowWhenStarted(
    flow: Flow<T>,
    collect: suspend (T) -> Unit
): Job {
    return lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest(collect)
        }
    }
}

//infix fun <T> Boolean.then(param: T): T? = if (this) param else null

object Utils {
    fun log(message: String?, tag: String = "Ali") {
        Log.i(tag, message ?: "NULL")
    }

    fun logDiv(tag: String = "Ali") {
        Log.i(tag, "----------------------------------------")
    }

    fun jsonLog(message: Any?, tag: String = "Ali") {
        log(GsonBuilder().setPrettyPrinting().create().toJson(message), tag)
    }
}
