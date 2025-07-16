package com.gayoung.microinteractions.themes

import androidx.compose.ui.graphics.Color
import com.gayoung.microinteractions.core.*

class DefaultTheme : MicroInteractionTheme {
    override val tapFeedback = FeedbackType.combined(
        FeedbackType.haptic(HapticType.LIGHT),
        FeedbackType.sound(SoundType.TAP),
        FeedbackType.animation(AnimationType.SCALE)
    )
    
    override val successFeedback = FeedbackType.combined(
        FeedbackType.haptic(HapticType.SUCCESS),
        FeedbackType.sound(SoundType.SUCCESS),
        FeedbackType.animation(AnimationType.BOUNCE)
    )
    
    override val failureFeedback = FeedbackType.combined(
        FeedbackType.haptic(HapticType.ERROR),
        FeedbackType.sound(SoundType.ERROR),
        FeedbackType.animation(AnimationType.SHAKE)
    )
    
    override val warningFeedback = FeedbackType.combined(
        FeedbackType.haptic(HapticType.WARNING),
        FeedbackType.sound(SoundType.WARNING),
        FeedbackType.animation(AnimationType.FLASH)
    )
    
    override val toggleFeedback = FeedbackType.combined(
        FeedbackType.haptic(HapticType.SELECTION),
        FeedbackType.sound(SoundType.CLICK),
        FeedbackType.animation(AnimationType.SCALE)
    )
    
    override val successColor = Color(0xFF4CAF50)
    override val failureColor = Color(0xFFF44336)
    override val warningColor = Color(0xFFFF9800)
    
    override val animationDuration = 0.3f
    override val defaultIntensity = 0.7f
}

class SubtleTheme : MicroInteractionTheme {
    override val tapFeedback = FeedbackType.combined(
        FeedbackType.haptic(HapticType.LIGHT),
        FeedbackType.animation(AnimationType.SCALE)
    )
    
    override val successFeedback = FeedbackType.combined(
        FeedbackType.haptic(HapticType.SOFT),
        FeedbackType.animation(AnimationType.FADE)
    )
    
    override val failureFeedback = FeedbackType.combined(
        FeedbackType.haptic(HapticType.LIGHT),
        FeedbackType.animation(AnimationType.SHAKE)
    )
    
    override val warningFeedback = FeedbackType.combined(
        FeedbackType.haptic(HapticType.LIGHT),
        FeedbackType.animation(AnimationType.PULSE)
    )
    
    override val toggleFeedback = FeedbackType.haptic(HapticType.SELECTION)
    
    override val successColor = Color(0xFF81C784)
    override val failureColor = Color(0xFFE57373)
    override val warningColor = Color(0xFFFFB74D)
    
    override val animationDuration = 0.4f
    override val defaultIntensity = 0.5f
}

class EnergeticTheme : MicroInteractionTheme {
    override val tapFeedback = FeedbackType.combined(
        FeedbackType.haptic(HapticType.MEDIUM),
        FeedbackType.sound(SoundType.CLICK),
        FeedbackType.animation(AnimationType.ELASTIC)
    )
    
    override val successFeedback = FeedbackType.combined(
        FeedbackType.haptic(HapticType.HEAVY),
        FeedbackType.sound(SoundType.SUCCESS),
        FeedbackType.animation(AnimationType.ELASTIC)
    )
    
    override val failureFeedback = FeedbackType.combined(
        FeedbackType.haptic(HapticType.ERROR),
        FeedbackType.sound(SoundType.ERROR),
        FeedbackType.animation(AnimationType.SHAKE)
    )
    
    override val warningFeedback = FeedbackType.combined(
        FeedbackType.haptic(HapticType.WARNING),
        FeedbackType.sound(SoundType.WARNING),
        FeedbackType.animation(AnimationType.FLASH)
    )
    
    override val toggleFeedback = FeedbackType.combined(
        FeedbackType.haptic(HapticType.RIGID),
        FeedbackType.sound(SoundType.POP),
        FeedbackType.animation(AnimationType.BOUNCE)
    )
    
    override val successColor = Color(0xFF2E7D32)
    override val failureColor = Color(0xFFD32F2F)
    override val warningColor = Color(0xFFF57C00)
    
    override val animationDuration = 0.25f
    override val defaultIntensity = 0.9f
}