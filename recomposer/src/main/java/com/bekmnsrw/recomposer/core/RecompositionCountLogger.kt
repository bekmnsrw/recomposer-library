package com.bekmnsrw.recomposer.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import com.bekmnsrw.recomposer.RecomposerConstants
import com.bekmnsrw.recomposer.core.model.RecompositionCounter

@Composable
internal inline fun RecompositionCountLogger(
    composableName: String,
    logger: (tag: String, message: String) -> Unit = { tag, message ->
        RecomposerConfig.logger.invoke(tag, message)
    }
) {
    val recompositionCounter = remember {
        RecompositionCounter(counter = RecomposerConstants.RECOMPOSITION_COUNTER_INIT_VALUE)
    }

    logger(
        RecomposerConfig.tag,
        "'$composableName' recomposed ${recompositionCounter.counter} time(s)"
    )

    SideEffect { recompositionCounter.counter++ }
}
