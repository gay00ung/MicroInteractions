package com.microinteractions.themes

import androidx.compose.ui.graphics.Color
import com.microinteractions.core.FeedbackType

interface MicroInteractionTheme {
    val tapFeedback: FeedbackType
    val successFeedback: FeedbackType
    val failureFeedback: FeedbackType
    val warningFeedback: FeedbackType
    val toggleFeedback: FeedbackType
    
    val successColor: Color
    val failureColor: Color
    val warningColor: Color
    
    val animationDuration: Float
    val defaultIntensity: Float
}