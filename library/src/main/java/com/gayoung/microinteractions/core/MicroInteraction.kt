package com.gayoung.microinteractions.core

import android.view.View
import com.gayoung.microinteractions.themes.MicroInteractionTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

sealed class MicroInteraction(
    val name: String,
    open val feedback: FeedbackType
) {
    // Basic interactions
    object Tap : MicroInteraction("tap", FeedbackType.combined(
        FeedbackType.haptic(HapticType.LIGHT),
        FeedbackType.sound(SoundType.TAP),
        FeedbackType.animation(AnimationType.SCALE)
    ))
    
    object LongPress : MicroInteraction("longPress", FeedbackType.combined(
        FeedbackType.haptic(HapticType.MEDIUM),
        FeedbackType.animation(AnimationType.PULSE)
    ))
    
    object Swipe : MicroInteraction("swipe", FeedbackType.combined(
        FeedbackType.haptic(HapticType.LIGHT),
        FeedbackType.sound(SoundType.WHOOSH),
        FeedbackType.animation(AnimationType.SLIDE)
    ))
    
    // State interactions
    object Success : MicroInteraction("success", FeedbackType.combined(
        FeedbackType.haptic(HapticType.SUCCESS),
        FeedbackType.sound(SoundType.SUCCESS),
        FeedbackType.animation(AnimationType.BOUNCE)
    ))
    
    object Failure : MicroInteraction("failure", FeedbackType.combined(
        FeedbackType.haptic(HapticType.ERROR),
        FeedbackType.sound(SoundType.ERROR),
        FeedbackType.animation(AnimationType.SHAKE)
    ))
    
    object Warning : MicroInteraction("warning", FeedbackType.combined(
        FeedbackType.haptic(HapticType.WARNING),
        FeedbackType.sound(SoundType.WARNING),
        FeedbackType.animation(AnimationType.FLASH)
    ))
    
    object Loading : MicroInteraction("loading", FeedbackType.animation(AnimationType.PULSE))
    
    // Action interactions
    object Toggle : MicroInteraction("toggle", FeedbackType.combined(
        FeedbackType.haptic(HapticType.SELECTION),
        FeedbackType.sound(SoundType.CLICK),
        FeedbackType.animation(AnimationType.SCALE)
    ))
    
    object Refresh : MicroInteraction("refresh", FeedbackType.combined(
        FeedbackType.haptic(HapticType.LIGHT),
        FeedbackType.animation(AnimationType.ROTATE)
    ))
    
    object Delete : MicroInteraction("delete", FeedbackType.combined(
        FeedbackType.haptic(HapticType.MEDIUM),
        FeedbackType.sound(SoundType.POP),
        FeedbackType.animation(AnimationType.FADE)
    ))
    
    object Favorite : MicroInteraction("favorite", FeedbackType.combined(
        FeedbackType.haptic(HapticType.LIGHT),
        FeedbackType.sound(SoundType.POP),
        FeedbackType.animation(AnimationType.ELASTIC)
    ))
    
    object Share : MicroInteraction("share", FeedbackType.combined(
        FeedbackType.haptic(HapticType.LIGHT),
        FeedbackType.sound(SoundType.WHOOSH),
        FeedbackType.animation(AnimationType.SCALE)
    ))
    
    // Custom interaction
    data class Custom(
        val customName: String,
        override val feedback: FeedbackType
    ) : MicroInteraction(customName, feedback)
}

class MicroInteractionBuilder(
    private val interaction: MicroInteraction,
    private val view: View,
    private val hapticEngine: HapticEngine,
    private val soundEngine: SoundEngine,
    private val animationEngine: AnimationEngine,
    private val config: MicroInteractionConfig,
    private val theme: MicroInteractionTheme?
) {
    private var delay: Long = 0
    private var intensity: Float = config.defaultIntensity
    private var condition: (() -> Boolean)? = null
    private var completion: (() -> Unit)? = null
    
    fun withDelay(delayMs: Long): MicroInteractionBuilder {
        this.delay = delayMs
        return this
    }
    
    fun withIntensity(intensity: Float): MicroInteractionBuilder {
        this.intensity = intensity.coerceIn(0f, 1f)
        return this
    }
    
    fun withCondition(condition: () -> Boolean): MicroInteractionBuilder {
        this.condition = condition
        return this
    }
    
    fun withCompletion(completion: () -> Unit): MicroInteractionBuilder {
        this.completion = completion
        return this
    }
    
    fun execute() {
        if (condition?.invoke() == false) {
            completion?.invoke()
            return
        }
        
        CoroutineScope(Dispatchers.Main).launch {
            if (delay > 0) {
                delay(delay)
            }
            
            val feedbackToUse = when (interaction) {
                is MicroInteraction.Tap -> theme?.tapFeedback ?: interaction.feedback
                is MicroInteraction.Success -> theme?.successFeedback ?: interaction.feedback
                is MicroInteraction.Failure -> theme?.failureFeedback ?: interaction.feedback
                is MicroInteraction.Warning -> theme?.warningFeedback ?: interaction.feedback
                is MicroInteraction.Toggle -> theme?.toggleFeedback ?: interaction.feedback
                else -> interaction.feedback
            }
            
            executeFeedback(feedbackToUse)
        }
    }
    
    private fun executeFeedback(feedback: FeedbackType) {
        when (feedback) {
            is FeedbackType.Haptic -> {
                if (config.isHapticEnabled) {
                    hapticEngine.performHaptic(feedback.type, intensity)
                }
            }
            is FeedbackType.Sound -> {
                if (config.isSoundEnabled) {
                    soundEngine.playSound(feedback.type, intensity * config.soundVolume)
                }
            }
            is FeedbackType.Animation -> {
                if (config.isAnimationEnabled) {
                    animationEngine.performAnimation(
                        view,
                        feedback.type,
                        (config.animationDuration * 1000).toLong(),
                        intensity,
                        completion
                    )
                }
            }
            is FeedbackType.Combined -> {
                feedback.feedbacks.forEach { executeFeedback(it) }
            }
        }
    }
}

data class MicroInteractionConfig(
    var isHapticEnabled: Boolean = true,
    var isSoundEnabled: Boolean = true,
    var isAnimationEnabled: Boolean = true,
    var defaultIntensity: Float = 0.7f,
    var soundVolume: Float = 0.7f,
    var animationDuration: Float = 0.3f,
    var isAdaptiveFeedbackEnabled: Boolean = true,
    var respectSystemSettings: Boolean = true,
    var debugMode: Boolean = false
)