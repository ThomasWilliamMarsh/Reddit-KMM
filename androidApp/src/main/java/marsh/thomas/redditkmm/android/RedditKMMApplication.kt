package marsh.thomas.redditkmm.android

import android.app.Application
import marsh.thomas.redditkmm.android.di.appModule
import marsh.thomas.redditkmm.common.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level

class RedditKMMApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@RedditKMMApplication)
            modules(appModule())
        }
    }
}