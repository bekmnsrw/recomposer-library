package com.bekmnsrw.recomposer.core

import android.util.Log
import com.bekmnsrw.recomposer.RecomposerConstants.RECOMPOSER

object RecomposerConfig {

    private const val DEFAULT_LOGCAT_TAG = RECOMPOSER

    var tag: String = DEFAULT_LOGCAT_TAG
    var logger: (tag: String, message: String) -> Unit = ::defaultLogger

    fun init(
        tag: String = DEFAULT_LOGCAT_TAG,
        logger: (tag: String, message: String) -> Unit = ::defaultLogger
    ) {
        RecomposerConfig.tag = tag
        RecomposerConfig.logger = logger
    }
}

internal fun defaultLogger(tag: String, message: String) { Log.d(tag, message) }
