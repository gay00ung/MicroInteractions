package com.microinteractions.example

import android.app.Application
import com.microinteractions.MicroInteractions

class ExampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Initialize MicroInteractions
        MicroInteractions.init(this)
        
        // Configure settings
        MicroInteractions.configure {
            isHapticEnabled = true
            isSoundEnabled = true
            isAnimationEnabled = true
            defaultIntensity = 0.7f
            debugMode = BuildConfig.DEBUG
        }
        
        // Set up analytics handler (optional)
        MicroInteractions.setAnalyticsHandler { interaction ->
            // Log interaction to your analytics service
            println("Interaction: ${interaction.name}")
        }
    }
}