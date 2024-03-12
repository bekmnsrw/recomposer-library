package com.bekmnsrw.recomposer.highlighter

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.dp
import com.bekmnsrw.recomposer.RecomposerConstants.RECOMPOSER_HIGHLIGHTER
import com.bekmnsrw.recomposer.RecomposerConstants.RECOMPOSER_HIGHLIGHTER_DELAY
import com.bekmnsrw.recomposer.RecomposerConstants.RECOMPOSITION_COUNTER_INIT_VALUE
import kotlinx.coroutines.delay
import kotlin.math.min

@Stable
fun Modifier.recomposerHighlighter(): Modifier = this.then(
    other = recomposerModifier
)

private val recomposerModifier: Modifier = Modifier.composed(
    inspectorInfo = debugInspectorInfo {
        name = RECOMPOSER_HIGHLIGHTER
    }
) {
    val numberOfRecompositions = remember {
        arrayOf(RECOMPOSITION_COUNTER_INIT_VALUE)
    }

    numberOfRecompositions[0]++

    val numberOfRecompositionsAtLastTimeout = remember {
        mutableLongStateOf(value = RECOMPOSITION_COUNTER_INIT_VALUE)
    }

    LaunchedEffect(numberOfRecompositions[0]) {
        delay(timeMillis = RECOMPOSER_HIGHLIGHTER_DELAY)
        numberOfRecompositionsAtLastTimeout.longValue = numberOfRecompositions[0]
    }

    Modifier.drawWithCache {
        onDrawWithContent {
            drawContent()

            val numberOfRecompositionsSinceTimeout = numberOfRecompositions[0] - numberOfRecompositionsAtLastTimeout.longValue
            val hasValidBorderParams = size.minDimension > 0F

            if (!hasValidBorderParams || numberOfRecompositionsSinceTimeout <= RECOMPOSITION_COUNTER_INIT_VALUE)
                return@onDrawWithContent

            val (color, strokeWidthPx) = when (numberOfRecompositionsSinceTimeout) {
                1L -> BLUE to 1F
                2L -> GREEN to 2.dp.toPx()
                else -> lerp(
                    start = YELLOW.copy(alpha = 0.8F),
                    stop = RED.copy(alpha = 0.5F),
                    fraction = min(
                        a = 1F,
                        b = (numberOfRecompositionsSinceTimeout - 1L).toFloat() / 100F
                    )
                ) to numberOfRecompositionsSinceTimeout.toInt().dp.toPx()
            }

            val shouldFillArea = (2 * strokeWidthPx) > size.minDimension

            val rectTopLeft = when (shouldFillArea) {
                true -> Offset.Zero
                false -> {
                    val halfStrokeWidthPx = strokeWidthPx / 2
                    Offset(halfStrokeWidthPx, halfStrokeWidthPx)
                }
            }

            val size = when (shouldFillArea) {
                true -> size
                false -> Size(
                    width = size.width - strokeWidthPx,
                    height = size.height - strokeWidthPx
                )
            }

            val style = when (shouldFillArea) {
                true -> Fill
                false -> Stroke(strokeWidthPx)
            }

            drawRect(
                brush = SolidColor(color),
                topLeft = rectTopLeft,
                size = size,
                style = style
            )
        }
    }
}
