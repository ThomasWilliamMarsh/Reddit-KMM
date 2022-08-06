package marsh.thomas.redditkmm.common

import android.util.Patterns
import io.ktor.client.engine.android.*
import marsh.thomas.redditkmm.common.util.ValidUrlUseCase
import org.koin.dsl.module

actual fun platformModule() = module {
    single { Android.create() }
    single { ValidUrlUseCase { Patterns.WEB_URL.matcher(it).matches() }}
}