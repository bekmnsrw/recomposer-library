package com.bekmnsrw.recomposer.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.bekmnsrw.recomposer.RecomposerConstants
import com.bekmnsrw.recomposer.core.model.RecompositionCounter
import com.bekmnsrw.recomposer.core.model.Ref

@Composable
internal fun RecompositionReasonLogger(
    trackingComposableArguments: Map<String, Any?>,
    composableName: String,
    logger: (tag: String, message: String) -> Unit = { tag, message ->
        RecomposerConfig.logger.invoke(tag, message)
    }
) {
    LaunchedEffect(Unit) {
        logger(
            RecomposerConfig.tag,
            "Recomposer is tracking '$composableName'"
        )
    }

    val recompositionCounter = remember { RecompositionCounter(counter = RecomposerConstants.RECOMPOSITION_COUNTER_INIT_VALUE) }
    val recompositionFlag = remember { Ref(value = false) }

    SideEffect { recompositionCounter.counter++ }

    val changeLog = StringBuilder()

    for ((key, newArgValue) in trackingComposableArguments) {
        var recompositionTrigger by remember { mutableStateOf(value = false) }
        val prevArgValue = remember(recompositionTrigger) { newArgValue }

        val recompositionReason = when {
            newArgValue != prevArgValue -> "'$key' value changed: '$prevArgValue' -> '$newArgValue'"
            newArgValue !== prevArgValue -> "'$key' instance changed, but value is the same: '$prevArgValue' -> '$newArgValue'"
            else -> null
        }

        if (recompositionReason != null) {
            changeLog.append("\n\t $recompositionReason")
            recompositionFlag.value = true
            recompositionTrigger = !recompositionTrigger
        }
    }

    when {
        changeLog.isNotEmpty() -> logger(
            RecomposerConfig.tag,
            "'$composableName' recomposed because $changeLog"
        )

        recompositionCounter.counter >= 1L && !recompositionFlag.value -> logger(
            RecomposerConfig.tag,
            "'$composableName' recomposed, but reason is unknown.\n" +
                    "Are you sure you added all params to `trackingComposableArguments`?"
        )

        else -> recompositionFlag.value = false
    }
}
