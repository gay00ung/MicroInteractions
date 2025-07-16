package com.gayoung.microinteractions

import android.content.Context
import android.util.Log
import com.gayoung.microinteractions.core.*
import com.gayoung.microinteractions.themes.DefaultTheme
import com.gayoung.microinteractions.themes.EnergeticTheme
import com.gayoung.microinteractions.themes.MicroInteractionTheme
import com.gayoung.microinteractions.themes.SubtleTheme

object MicroInteractions {
    
    private lateinit var context: Context
    private lateinit var hapticEngine: HapticEngine
    private lateinit var soundEngine: SoundEngine
    private lateinit var animationEngine: AnimationEngine
    private var config = MicroInteractionConfig()
    private var currentTheme: MicroInteractionTheme? = null
    private var analyticsHandler: ((MicroInteraction) -> Unit)? = null
    internal var isInitialized = false
    
    fun init(context: Context) {
        this.context = context.applicationContext
        hapticEngine = HapticEngine(this.context)
        soundEngine = SoundEngine(this.context)
        animationEngine = AnimationEngine()
        isInitialized = true
    }
    
    fun configure(configuration: MicroInteractionConfig.() -> Unit) {
        ensureInitialized()
        config.apply(configuration)
    }
    
    fun applyTheme(theme: MicroInteractionTheme) {
        ensureInitialized()
        currentTheme = theme
        config.apply {
            defaultIntensity = theme.defaultIntensity
            animationDuration = theme.animationDuration
        }
    }
    
    fun setAnalyticsHandler(handler: (MicroInteraction) -> Unit) {
        analyticsHandler = handler
    }
    
    fun enableDebugMode(enabled: Boolean) {
        config.debugMode = enabled
        if (enabled) {
            Log.d("MicroInteractions", "Debug mode enabled")
        }
    }
    
    fun preloadSound(fileName: String): Boolean {
        ensureInitialized()
        return soundEngine.preloadSound(fileName)
    }
    
    internal fun getHapticEngine(): HapticEngine {
        ensureInitialized()
        return hapticEngine
    }
    
    internal fun getSoundEngine(): SoundEngine {
        ensureInitialized()
        return soundEngine
    }
    
    internal fun getAnimationEngine(): AnimationEngine {
        ensureInitialized()
        return animationEngine
    }
    
    internal fun getConfig(): MicroInteractionConfig {
        ensureInitialized()
        return config
    }
    
    internal fun getCurrentTheme(): MicroInteractionTheme? = currentTheme
    
    internal fun trackInteraction(interaction: MicroInteraction) {
        analyticsHandler?.invoke(interaction)
        if (config.debugMode) {
            Log.d("MicroInteractions", "Interaction triggered: ${interaction.name}")
        }
    }
    
    private fun ensureInitialized() {
        if (!isInitialized) {
            throw IllegalStateException("MicroInteractions not initialized. Call MicroInteractions.init(context) first.")
        }
    }
    
    object Presets {
        fun applySubtle() {
            applyTheme(SubtleTheme())
        }
        
        fun applyEnergetic() {
            applyTheme(EnergeticTheme())
        }
        
        fun applySilent() {
            configure {
                isSoundEnabled = false
            }
        }
        
        fun applyAccessible() {
            configure {
                isHapticEnabled = true
                isSoundEnabled = true
                isAnimationEnabled = true
                defaultIntensity = 0.8f
                soundVolume = 0.9f
                animationDuration = 0.5f
            }
        }
        
        fun applyBatterySaving() {
            configure {
                isHapticEnabled = false
                isSoundEnabled = false
                isAnimationEnabled = true
                defaultIntensity = 0.5f
                animationDuration = 0.2f
                isAdaptiveFeedbackEnabled = true
            }
        }
    }
}