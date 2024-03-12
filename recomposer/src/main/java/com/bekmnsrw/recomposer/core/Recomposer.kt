package com.bekmnsrw.recomposer.core

import androidx.compose.runtime.Composable

@Composable
fun Recomposer(
    trackingComposableArguments: Map<String, Any?>,
    composableName: String
) {
    RecompositionCountLogger(
        composableName = composableName
    )
    RecompositionReasonLogger(
        trackingComposableArguments = trackingComposableArguments,
        composableName = composableName
    )
}
