package com.gayoung.microinteractions.core

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.SoundPool
import android.util.Log
import java.util.concurrent.ConcurrentHashMap

class SoundEngine(private val context: Context) {
    
    private val soundPool: SoundPool
    private val loadedSounds = ConcurrentHashMap<SoundType, Int>()
    private val customSounds = ConcurrentHashMap<String, Int>()
    private var mediaPlayer: MediaPlayer? = null
    
    init {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
            
        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(audioAttributes)
            .build()
            
        preloadSystemSounds()
    }
    
    private fun preloadSystemSounds() {
        // System sounds will be loaded on demand
        // Android doesn't expose system sounds via R.raw
    }
    
    fun playSound(type: SoundType, volume: Float = 0.7f) {
        if (!isSoundEnabled()) return
        
        when (type) {
            is SoundType.CUSTOM -> playCustomSound(type.fileName, volume)
            else -> playSystemSound(type, volume)
        }
    }
    
    private fun playSystemSound(type: SoundType, volume: Float) {
        // For now, system sounds are disabled
        // In a real implementation, you would load custom sound files
    }
    
    private fun playCustomSound(fileName: String, volume: Float) {
        val soundId = customSounds[fileName] ?: loadCustomSound(fileName)
        soundId?.let {
            soundPool.play(
                it,
                volume,
                volume,
                1,
                0,
                1.0f
            )
        }
    }
    
    fun preloadSound(fileName: String): Boolean {
        return loadCustomSound(fileName) != null
    }
    
    private fun loadCustomSound(fileName: String): Int? {
        customSounds[fileName]?.let { return it }
        
        return try {
            val assetFileDescriptor = context.assets.openFd("sounds/$fileName")
            val soundId = soundPool.load(assetFileDescriptor, 1)
            customSounds[fileName] = soundId
            soundId
        } catch (e: Exception) {
            Log.e("SoundEngine", "Failed to load custom sound: $fileName", e)
            null
        }
    }
    
    fun playLongSound(fileName: String, volume: Float = 0.7f) {
        stopLongSound()
        
        try {
            mediaPlayer = MediaPlayer().apply {
                val assetFileDescriptor = context.assets.openFd("sounds/$fileName")
                setDataSource(
                    assetFileDescriptor.fileDescriptor,
                    assetFileDescriptor.startOffset,
                    assetFileDescriptor.length
                )
                setVolume(volume, volume)
                prepare()
                start()
            }
        } catch (e: Exception) {
            Log.e("SoundEngine", "Failed to play long sound: $fileName", e)
        }
    }
    
    fun stopLongSound() {
        mediaPlayer?.apply {
            if (isPlaying) {
                stop()
            }
            release()
        }
        mediaPlayer = null
    }
    
    private fun isSoundEnabled(): Boolean {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        return audioManager.ringerMode != AudioManager.RINGER_MODE_SILENT
    }
    
    fun release() {
        soundPool.release()
        stopLongSound()
        loadedSounds.clear()
        customSounds.clear()
    }
}

sealed class SoundType {
    object TAP : SoundType()
    object CLICK : SoundType()
    object POP : SoundType()
    object SUCCESS : SoundType()
    object ERROR : SoundType()
    object WARNING : SoundType()
    object WHOOSH : SoundType()
    data class CUSTOM(val fileName: String) : SoundType()
}