package com.gayoung.microinteractions.core

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.RequiresApi

class HapticEngine(private val context: Context) {
    
    private val vibrator: Vibrator? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }
    
    fun performHaptic(type: HapticType, intensity: Float = 0.7f) {
        if (!isHapticAvailable()) return
        
        when (type) {
            HapticType.LIGHT -> performLightHaptic(intensity)
            HapticType.MEDIUM -> performMediumHaptic(intensity)
            HapticType.HEAVY -> performHeavyHaptic(intensity)
            HapticType.SOFT -> performSoftHaptic(intensity)
            HapticType.RIGID -> performRigidHaptic(intensity)
            HapticType.SELECTION -> performSelectionHaptic()
            HapticType.SUCCESS -> performSuccessHaptic()
            HapticType.WARNING -> performWarningHaptic()
            HapticType.ERROR -> performErrorHaptic()
            is HapticType.CUSTOM -> performCustomHaptic(type.pattern, type.amplitudes)
        }
    }
    
    private fun performLightHaptic(intensity: Float) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val amplitude = (intensity * 50).toInt().coerceIn(1, 255)
            vibrator?.vibrate(VibrationEffect.createOneShot(10, amplitude))
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(10)
        }
    }
    
    private fun performMediumHaptic(intensity: Float) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val amplitude = (intensity * 150).toInt().coerceIn(1, 255)
            vibrator?.vibrate(VibrationEffect.createOneShot(20, amplitude))
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(20)
        }
    }
    
    private fun performHeavyHaptic(intensity: Float) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val amplitude = (intensity * 255).toInt().coerceIn(1, 255)
            vibrator?.vibrate(VibrationEffect.createOneShot(30, amplitude))
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(30)
        }
    }
    
    private fun performSoftHaptic(intensity: Float) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val amplitude = (intensity * 30).toInt().coerceIn(1, 255)
            vibrator?.vibrate(VibrationEffect.createWaveform(
                longArrayOf(0, 15, 10, 15),
                intArrayOf(0, amplitude, 0, amplitude),
                -1
            ))
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(longArrayOf(0, 15, 10, 15), -1)
        }
    }
    
    private fun performRigidHaptic(intensity: Float) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val amplitude = (intensity * 200).toInt().coerceIn(1, 255)
            vibrator?.vibrate(VibrationEffect.createOneShot(15, amplitude))
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(15)
        }
    }
    
    private fun performSelectionHaptic() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vibrator?.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK))
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(VibrationEffect.createOneShot(5, 50))
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(5)
        }
    }
    
    private fun performSuccessHaptic() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(VibrationEffect.createWaveform(
                longArrayOf(0, 20, 40, 30),
                intArrayOf(0, 180, 0, 220),
                -1
            ))
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(longArrayOf(0, 20, 40, 30), -1)
        }
    }
    
    private fun performWarningHaptic() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(VibrationEffect.createWaveform(
                longArrayOf(0, 10, 10, 10, 10, 10),
                intArrayOf(0, 150, 0, 150, 0, 150),
                -1
            ))
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(longArrayOf(0, 10, 10, 10, 10, 10), -1)
        }
    }
    
    private fun performErrorHaptic() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(VibrationEffect.createWaveform(
                longArrayOf(0, 50, 50, 50),
                intArrayOf(0, 255, 0, 255),
                -1
            ))
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(longArrayOf(0, 50, 50, 50), -1)
        }
    }
    
    private fun performCustomHaptic(pattern: LongArray, amplitudes: IntArray?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && amplitudes != null) {
            vibrator?.vibrate(VibrationEffect.createWaveform(pattern, amplitudes, -1))
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(pattern, -1)
        }
    }
    
    fun isHapticAvailable(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.hasVibrator() == true
        } else {
            @Suppress("DEPRECATION")
            vibrator?.hasVibrator() == true
        }
    }
    
    fun cancelHaptic() {
        vibrator?.cancel()
    }
}

sealed class HapticType {
    object LIGHT : HapticType()
    object MEDIUM : HapticType()
    object HEAVY : HapticType()
    object SOFT : HapticType()
    object RIGID : HapticType()
    object SELECTION : HapticType()
    object SUCCESS : HapticType()
    object WARNING : HapticType()
    object ERROR : HapticType()
    data class CUSTOM(val pattern: LongArray, val amplitudes: IntArray? = null) : HapticType() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            other as CUSTOM
            if (!pattern.contentEquals(other.pattern)) return false
            if (!amplitudes.contentEquals(other.amplitudes)) return false
            return true
        }
        
        override fun hashCode(): Int {
            var result = pattern.contentHashCode()
            result = 31 * result + (amplitudes?.contentHashCode() ?: 0)
            return result
        }
    }
}