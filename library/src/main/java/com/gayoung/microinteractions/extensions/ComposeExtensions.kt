package com.gayoung.microinteractions.extensions

import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import com.gayoung.microinteractions.MicroInteractions
import com.gayoung.microinteractions.core.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun rememberMicroInteractionState(): MicroInteractionState {
    val context = LocalContext.current
    val view = LocalView.current
    
    LaunchedEffect(Unit) {
        if (!MicroInteractions.isInitialized) {
            MicroInteractions.init(context)
        }
    }
    
    return remember { MicroInteractionState(view) }
}

class MicroInteractionState(private val view: android.view.View) {
    fun trigger(interaction: MicroInteraction) {
        view.triggerMicroInteraction(interaction)
    }
}

fun Modifier.microInteraction(
    interaction: MicroInteraction,
    trigger: ComposeTrigger = ComposeTrigger.OnClick,
    enabled: Boolean = true
): Modifier = composed {
    val view = LocalView.current
    val coroutineScope = rememberCoroutineScope()
    var isAnimating by remember { mutableStateOf(false) }
    
    val animationSpec: AnimationSpec<Float> = remember(interaction) {
        when (interaction) {
            is MicroInteraction.Tap -> spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
            is MicroInteraction.Success -> spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessLow
            )
            else -> tween(300)
        }
    }
    
    val scale by animateFloatAsState(
        targetValue = if (isAnimating) 0.95f else 1f,
        animationSpec = animationSpec,
        finishedListener = { isAnimating = false }
    )
    
    val rotation by animateFloatAsState(
        targetValue = if (isAnimating && interaction is MicroInteraction.Refresh) 360f else 0f,
        animationSpec = tween(300)
    )
    
    val modifier = when (trigger) {
        ComposeTrigger.OnClick -> {
            this.clickable(
                enabled = enabled,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                if (enabled) {
                    view.triggerMicroInteraction(interaction)
                    isAnimating = true
                }
            }
        }
        ComposeTrigger.OnLongPress -> {
            this.pointerInput(enabled) {
                detectTapGestures(
                    onLongPress = {
                        if (enabled) {
                            view.triggerMicroInteraction(interaction)
                            isAnimating = true
                        }
                    }
                )
            }
        }
        ComposeTrigger.OnAppear -> {
            LaunchedEffect(Unit) {
                if (enabled) {
                    delay(100) // Small delay to ensure view is ready
                    view.triggerMicroInteraction(interaction)
                    isAnimating = true
                }
            }
            this
        }
        ComposeTrigger.Manual -> this
    }
    
    modifier
        .scale(scale)
        .rotate(rotation)
        .graphicsLayer {
            if (isAnimating && interaction is MicroInteraction.Failure) {
                translationX = ((-10..10).random()).toFloat()
            }
        }
}

// Convenience functions for common interactions
fun Modifier.tapInteraction(enabled: Boolean = true) = 
    microInteraction(MicroInteraction.Tap, enabled = enabled)

fun Modifier.successInteraction(enabled: Boolean = true) = 
    microInteraction(MicroInteraction.Success, enabled = enabled)

fun Modifier.failureInteraction(enabled: Boolean = true) = 
    microInteraction(MicroInteraction.Failure, enabled = enabled)

fun Modifier.toggleInteraction(enabled: Boolean = true) = 
    microInteraction(MicroInteraction.Toggle, enabled = enabled)

enum class ComposeTrigger {
    OnClick,
    OnLongPress,
    OnAppear,
    Manual
}

// Composable wrapper for manual triggering
@Composable
fun MicroInteractionTrigger(
    interaction: MicroInteraction,
    trigger: Boolean,
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    
    LaunchedEffect(trigger) {
        if (trigger) {
            view.triggerMicroInteraction(interaction)
        }
    }
    
    content()
}