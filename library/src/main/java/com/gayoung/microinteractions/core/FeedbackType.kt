package com.gayoung.microinteractions.core

sealed class FeedbackType {
    data class Haptic(val type: HapticType) : FeedbackType()
    data class Sound(val type: SoundType) : FeedbackType()
    data class Animation(val type: AnimationType) : FeedbackType()
    data class Combined(val feedbacks: List<FeedbackType>) : FeedbackType()
    
    companion object {
        fun haptic(type: HapticType) = Haptic(type)
        fun sound(type: SoundType) = Sound(type)
        fun animation(type: AnimationType) = Animation(type)
        fun combined(vararg feedbacks: FeedbackType) = Combined(feedbacks.toList())
    }
}