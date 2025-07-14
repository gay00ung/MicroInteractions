package com.microinteractions.example

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView
import com.microinteractions.MicroInteractions
import com.microinteractions.core.MicroInteraction
import com.microinteractions.extensions.*
import com.microinteractions.themes.DefaultTheme
import com.microinteractions.themes.EnergeticTheme
import com.microinteractions.themes.SubtleTheme

class XmlActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_xml)
        
        setupViews()
    }
    
    private fun setupViews() {
        // Basic buttons
        findViewById<Button>(R.id.btnTap).addTapInteraction()
        
        findViewById<Button>(R.id.btnLongPress).addMicroInteraction(
            MicroInteraction.LongPress,
            InteractionTrigger.OnLongClick
        )
        
        // State buttons
        findViewById<Button>(R.id.btnSuccess).setOnClickListener {
            it.triggerMicroInteraction(MicroInteraction.Success)
        }
        
        findViewById<Button>(R.id.btnFailure).setOnClickListener {
            it.triggerMicroInteraction(MicroInteraction.Failure)
        }
        
        findViewById<Button>(R.id.btnWarning).setOnClickListener {
            it.triggerMicroInteraction(MicroInteraction.Warning)
        }
        
        // Toggle switch
        findViewById<Switch>(R.id.switchToggle).addToggleInteraction()
        
        // SeekBar
        findViewById<SeekBar>(R.id.seekBar).addSlideInteraction()
        
        // EditText
        findViewById<EditText>(R.id.editText).addEditingInteraction()
        
        // Action buttons
        findViewById<ImageButton>(R.id.btnRefresh).addMicroInteraction(MicroInteraction.Refresh)
        findViewById<ImageButton>(R.id.btnDelete).addMicroInteraction(MicroInteraction.Delete)
        findViewById<ImageButton>(R.id.btnFavorite).addMicroInteraction(MicroInteraction.Favorite)
        findViewById<ImageButton>(R.id.btnShare).addMicroInteraction(MicroInteraction.Share)
        
        // Theme radio buttons
        findViewById<RadioGroup>(R.id.radioGroupTheme).setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioDefault -> MicroInteractions.applyTheme(DefaultTheme())
                R.id.radioSubtle -> MicroInteractions.applyTheme(SubtleTheme())
                R.id.radioEnergetic -> MicroInteractions.applyTheme(EnergeticTheme())
            }
        }
        
        // Preset buttons
        findViewById<Button>(R.id.btnSilentPreset).setOnClickListener {
            MicroInteractions.Presets.applySilent()
            it.triggerMicroInteraction(MicroInteraction.Tap)
        }
        
        findViewById<Button>(R.id.btnBatteryPreset).setOnClickListener {
            MicroInteractions.Presets.applyBatterySaving()
            it.triggerMicroInteraction(MicroInteraction.Tap)
        }
        
        // Cards with interactions
        findViewById<MaterialCardView>(R.id.cardExample).addMicroInteraction(MicroInteraction.Tap)
            .withIntensity(0.5f)
            .withCompletion {
                Toast.makeText(this, "Card tapped!", Toast.LENGTH_SHORT).show()
            }
        
        // Chained interactions example
        findViewById<Button>(R.id.btnChained).addMicroInteraction(MicroInteraction.Tap)
            .withDelay(100)
            .withIntensity(0.8f)
            .withCondition { true } // Could check some condition
            .withCompletion {
                // Trigger another interaction after completion
                findViewById<Button>(R.id.btnChained).triggerMicroInteraction(MicroInteraction.Success)
            }
    }
}