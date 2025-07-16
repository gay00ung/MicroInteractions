package com.gayoung.microinteractions.core

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.BounceInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.core.view.ViewCompat

class AnimationEngine {
    
    fun performAnimation(
        view: View,
        type: AnimationType,
        duration: Long = 300,
        intensity: Float = 1.0f,
        completion: (() -> Unit)? = null
    ) {
        when (type) {
            AnimationType.BOUNCE -> performBounce(view, duration, intensity, completion)
            AnimationType.SHAKE -> performShake(view, duration, intensity, completion)
            AnimationType.PULSE -> performPulse(view, duration, intensity, completion)
            AnimationType.SCALE -> performScale(view, duration, intensity, completion)
            AnimationType.ROTATE -> performRotate(view, duration, intensity, completion)
            AnimationType.FADE -> performFade(view, duration, intensity, completion)
            AnimationType.SLIDE -> performSlide(view, duration, intensity, completion)
            AnimationType.ELASTIC -> performElastic(view, duration, intensity, completion)
            AnimationType.FLASH -> performFlash(view, duration, intensity, completion)
            is AnimationType.CUSTOM -> type.animator(view, duration, intensity, completion)
        }
    }
    
    private fun performBounce(view: View, duration: Long, intensity: Float, completion: (() -> Unit)?) {
        val scaleX = ObjectAnimator.ofFloat(view, View.SCALE_X, 1f, 1.2f * intensity, 1f)
        val scaleY = ObjectAnimator.ofFloat(view, View.SCALE_Y, 1f, 1.2f * intensity, 1f)
        
        AnimatorSet().apply {
            playTogether(scaleX, scaleY)
            this.duration = duration
            interpolator = BounceInterpolator()
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    completion?.invoke()
                }
            })
            start()
        }
    }
    
    private fun performShake(view: View, duration: Long, intensity: Float, completion: (() -> Unit)?) {
        val shakeDistance = 10f * intensity
        
        ObjectAnimator.ofFloat(view, View.TRANSLATION_X, 0f, -shakeDistance, shakeDistance, -shakeDistance, shakeDistance, 0f).apply {
            this.duration = duration
            interpolator = AccelerateDecelerateInterpolator()
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    completion?.invoke()
                }
            })
            start()
        }
    }
    
    private fun performPulse(view: View, duration: Long, intensity: Float, completion: (() -> Unit)?) {
        val pulseScale = 1f + (0.05f * intensity)
        
        val scaleXUp = ObjectAnimator.ofFloat(view, View.SCALE_X, 1f, pulseScale)
        val scaleYUp = ObjectAnimator.ofFloat(view, View.SCALE_Y, 1f, pulseScale)
        val scaleXDown = ObjectAnimator.ofFloat(view, View.SCALE_X, pulseScale, 1f)
        val scaleYDown = ObjectAnimator.ofFloat(view, View.SCALE_Y, pulseScale, 1f)
        
        AnimatorSet().apply {
            play(scaleXUp).with(scaleYUp)
            play(scaleXDown).with(scaleYDown).after(scaleXUp)
            this.duration = duration / 2
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    completion?.invoke()
                }
            })
            start()
        }
    }
    
    private fun performScale(view: View, duration: Long, intensity: Float, completion: (() -> Unit)?) {
        val targetScale = 0.9f - (0.1f * intensity)
        
        val scaleX = ObjectAnimator.ofFloat(view, View.SCALE_X, 1f, targetScale, 1f)
        val scaleY = ObjectAnimator.ofFloat(view, View.SCALE_Y, 1f, targetScale, 1f)
        
        AnimatorSet().apply {
            playTogether(scaleX, scaleY)
            this.duration = duration
            interpolator = AccelerateDecelerateInterpolator()
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    completion?.invoke()
                }
            })
            start()
        }
    }
    
    private fun performRotate(view: View, duration: Long, intensity: Float, completion: (() -> Unit)?) {
        val rotation = 360f * intensity
        
        ObjectAnimator.ofFloat(view, View.ROTATION, 0f, rotation).apply {
            this.duration = duration
            interpolator = AccelerateDecelerateInterpolator()
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    view.rotation = 0f
                    completion?.invoke()
                }
            })
            start()
        }
    }
    
    private fun performFade(view: View, duration: Long, intensity: Float, completion: (() -> Unit)?) {
        val targetAlpha = 1f - (0.5f * intensity)
        
        ObjectAnimator.ofFloat(view, View.ALPHA, 1f, targetAlpha, 1f).apply {
            this.duration = duration
            interpolator = AccelerateDecelerateInterpolator()
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    completion?.invoke()
                }
            })
            start()
        }
    }
    
    private fun performSlide(view: View, duration: Long, intensity: Float, completion: (() -> Unit)?) {
        val slideDistance = 50f * intensity
        
        ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, 0f, slideDistance, 0f).apply {
            this.duration = duration
            interpolator = DecelerateInterpolator()
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    completion?.invoke()
                }
            })
            start()
        }
    }
    
    private fun performElastic(view: View, duration: Long, intensity: Float, completion: (() -> Unit)?) {
        val elasticScale = 1f + (0.2f * intensity)
        
        val scaleX = ObjectAnimator.ofFloat(view, View.SCALE_X, 1f, elasticScale, 1f)
        val scaleY = ObjectAnimator.ofFloat(view, View.SCALE_Y, 1f, elasticScale, 1f)
        
        AnimatorSet().apply {
            playTogether(scaleX, scaleY)
            this.duration = duration
            interpolator = OvershootInterpolator(2f)
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    completion?.invoke()
                }
            })
            start()
        }
    }
    
    private fun performFlash(view: View, duration: Long, intensity: Float, completion: (() -> Unit)?) {
        ValueAnimator.ofFloat(0f, 1f).apply {
            this.duration = duration
            repeatCount = (3 * intensity).toInt()
            repeatMode = ValueAnimator.REVERSE
            addUpdateListener { animator ->
                view.alpha = if (animator.animatedFraction < 0.5f) 0.3f else 1f
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    view.alpha = 1f
                    completion?.invoke()
                }
            })
            start()
        }
    }
    
    fun cancelAnimation(view: View) {
        ViewCompat.animate(view).cancel()
    }
}

sealed class AnimationType {
    object BOUNCE : AnimationType()
    object SHAKE : AnimationType()
    object PULSE : AnimationType()
    object SCALE : AnimationType()
    object ROTATE : AnimationType()
    object FADE : AnimationType()
    object SLIDE : AnimationType()
    object ELASTIC : AnimationType()
    object FLASH : AnimationType()
    data class CUSTOM(
        val animator: (view: View, duration: Long, intensity: Float, completion: (() -> Unit)?) -> Unit
    ) : AnimationType()
}