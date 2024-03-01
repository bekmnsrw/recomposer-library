package com.bekmnsrw.recomposer

import android.app.Application
import android.util.Log
import com.bekmnsrw.recomposer.core.RecomposerConfig

class RecomposerApp : Application() {
    override fun onCreate() {
        super.onCreate()

        /* Logcat output customization */
        RecomposerConfig.init(
            tag = "CustomLogcatTag",
            logger = { tag, message ->
                /* You can override default Android Logger or use another one (e.g. Timber) */
                Log.e(tag, message)
            }
        )
    }
}
