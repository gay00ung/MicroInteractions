package com.gayoung.microinteractions.extensions

import android.view.View
import android.widget.*
import com.gayoung.microinteractions.MicroInteractions
import com.gayoung.microinteractions.core.MicroInteraction
import com.gayoung.microinteractions.core.MicroInteractionBuilder

fun View.addMicroInteraction(
    interaction: MicroInteraction,
    trigger: InteractionTrigger = InteractionTrigger.OnClick
): MicroInteractionBuilder {
    val builder = MicroInteractionBuilder(
        interaction,
        this,
        MicroInteractions.getHapticEngine(),
        MicroInteractions.getSoundEngine(),
        MicroInteractions.getAnimationEngine(),
        MicroInteractions.getConfig(),
        MicroInteractions.getCurrentTheme()
    )
    
    when (trigger) {
        InteractionTrigger.OnClick -> {
            setOnClickListener {
                MicroInteractions.trackInteraction(interaction)
                builder.execute()
            }
        }
        InteractionTrigger.OnLongClick -> {
            setOnLongClickListener {
                MicroInteractions.trackInteraction(interaction)
                builder.execute()
                true
            }
        }
        InteractionTrigger.OnTouch -> {
            setOnTouchListener { _, event ->
                if (event.action == android.view.MotionEvent.ACTION_DOWN) {
                    MicroInteractions.trackInteraction(interaction)
                    builder.execute()
                }
                false
            }
        }
        InteractionTrigger.Manual -> {
            // No automatic trigger
        }
    }
    
    return builder
}

fun View.triggerMicroInteraction(interaction: MicroInteraction): MicroInteractionBuilder {
    val builder = MicroInteractionBuilder(
        interaction,
        this,
        MicroInteractions.getHapticEngine(),
        MicroInteractions.getSoundEngine(),
        MicroInteractions.getAnimationEngine(),
        MicroInteractions.getConfig(),
        MicroInteractions.getCurrentTheme()
    )
    
    MicroInteractions.trackInteraction(interaction)
    builder.execute()
    
    return builder
}

// Control-specific extensions
fun Button.addTapInteraction() = addMicroInteraction(MicroInteraction.Tap)

fun Button.showSuccess() = triggerMicroInteraction(MicroInteraction.Success)

fun Button.showFailure() = triggerMicroInteraction(MicroInteraction.Failure)

fun Switch.addToggleInteraction() {
    setOnCheckedChangeListener { _, _ ->
        triggerMicroInteraction(MicroInteraction.Toggle)
    }
}

fun CompoundButton.addToggleInteraction() {
    setOnCheckedChangeListener { _, _ ->
        triggerMicroInteraction(MicroInteraction.Toggle)
    }
}

fun SeekBar.addSlideInteraction() {
    setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            if (fromUser) {
                triggerMicroInteraction(MicroInteraction.Swipe)
            }
        }
        
        override fun onStartTrackingTouch(seekBar: SeekBar?) {}
        override fun onStopTrackingTouch(seekBar: SeekBar?) {}
    })
}

fun EditText.addEditingInteraction() {
    setOnFocusChangeListener { _, hasFocus ->
        if (hasFocus) {
            triggerMicroInteraction(MicroInteraction.Tap)
        }
    }
}

fun androidx.swiperefreshlayout.widget.SwipeRefreshLayout.addRefreshInteraction() {
    setOnRefreshListener {
        (getChildAt(0) as? View)?.triggerMicroInteraction(MicroInteraction.Refresh)
    }
}

enum class InteractionTrigger {
    OnClick,
    OnLongClick,
    OnTouch,
    Manual
}