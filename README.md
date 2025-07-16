# MicroInteractions for Android

A delightful Android library for adding micro-interactions to your apps with haptic feedback, sounds, and animations. Inspired by the iOS SwiftMicroInteractions library.

[![Android](https://img.shields.io/badge/Android-21%2B-green.svg)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.20-blue.svg)](https://kotlinlang.org)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![](https://jitpack.io/v/gay00ung/MicroInteractions.svg)](https://jitpack.io/#gay00ung/MicroInteractions)

## Features

- 🎯 **Easy Integration** - Simple API with minimal setup required
- 📱 **Haptic Feedback** - Support for all Android haptic types including custom patterns
- 🔊 **Sound Effects** - Built-in system sounds with custom sound support
- ✨ **Animations** - Beautiful animations including bounce, shake, pulse, and more
- 🎨 **Themes** - Pre-built themes and full customization support
- 📝 **XML & Compose** - Full support for both View system and Jetpack Compose
- ♿️ **Accessibility** - Respects system settings
- 🔋 **Battery Conscious** - Adaptive feedback and battery-saving mode
- 📊 **Analytics Ready** - Built-in analytics handler support
- 🐛 **Debug Mode** - Logging for development

## Installation

### Gradle

Add the dependency to your module's `build.gradle.kts`:

```kotlin
dependencies {
    implementation ("com.github.gay00ung:MicroInteractions:v1.0.1")
}
```

### Maven

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' }
		}
}
```

## Quick Start

### Basic Setup

Initialize in your Application class:

```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Initialize MicroInteractions
        MicroInteractions.init(this)
        
        // Optional configuration
        MicroInteractions.configure {
            isHapticEnabled = true
            isSoundEnabled = true
            defaultIntensity = 0.7f
        }
    }
}
```

### XML Layout Usage

```kotlin
// Simple tap interaction
button.addTapInteraction()

// With completion
button.addMicroInteraction(MicroInteraction.Success) {
    Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show()
}

// Advanced configuration
button.addMicroInteraction(MicroInteraction.Tap)
    .withDelay(100)
    .withIntensity(0.8f)
    .withCondition { isEnabled }
    .withCompletion { 
        // Do something after
    }

// Programmatic trigger
view.triggerMicroInteraction(MicroInteraction.Success)
```

### Jetpack Compose Usage

```kotlin
// Basic usage
Button(
    onClick = { /* action */ },
    modifier = Modifier.tapInteraction()
) {
    Text("Tap Me")
}

// Toggle with interaction
Switch(
    checked = isChecked,
    onCheckedChange = { isChecked = it },
    modifier = Modifier.toggleInteraction()
)

// Custom trigger
Text(
    "Hello",
    modifier = Modifier.microInteraction(
        MicroInteraction.Pulse, 
        trigger = ComposeTrigger.OnAppear
    )
)

// Manual trigger with state
@Composable
fun MyComponent() {
    var showSuccess by remember { mutableStateOf(false) }
    
    Button(onClick = { showSuccess = true }) {
        Text("Submit")
    }
    
    MicroInteractionTrigger(
        interaction = MicroInteraction.Success,
        trigger = showSuccess
    ) {
        LaunchedEffect(showSuccess) {
            if (showSuccess) {
                showSuccess = false
            }
        }
    }
}
```

## Available Interactions

### Pre-defined Interactions

- **Basic**: `Tap`, `LongPress`, `Swipe`
- **States**: `Success`, `Failure`, `Warning`, `Loading`
- **Actions**: `Toggle`, `Refresh`, `Delete`, `Favorite`, `Share`

### Custom Interactions

```kotlin
// Create custom interaction
val customInteraction = MicroInteraction.Custom(
    name = "powerUp",
    feedback = FeedbackType.combined(
        FeedbackType.haptic(HapticType.HEAVY),
        FeedbackType.sound(SoundType.CUSTOM("powerup.mp3")),
        FeedbackType.animation(AnimationType.ELASTIC)
    )
)

view.addMicroInteraction(customInteraction)
```

## Themes

### Built-in Themes

```kotlin
// Apply pre-built themes
MicroInteractions.applyTheme(DefaultTheme())
MicroInteractions.applyTheme(SubtleTheme())
MicroInteractions.applyTheme(EnergeticTheme())
```

### Custom Theme

```kotlin
class MyCustomTheme : MicroInteractionTheme {
    override val tapFeedback = FeedbackType.combined(
        FeedbackType.haptic(HapticType.MEDIUM),
        FeedbackType.sound(SoundType.POP)
    )
    
    override val successColor = Color(0xFF4CAF50)
    override val animationDuration = 0.35f
    // ... implement other properties
}

MicroInteractions.applyTheme(MyCustomTheme())
```

## Configuration Presets

```kotlin
// Quick presets for common scenarios
MicroInteractions.Presets.applySubtle()      // Minimal feedback
MicroInteractions.Presets.applyEnergetic()   // Strong feedback
MicroInteractions.Presets.applySilent()      // No sounds
MicroInteractions.Presets.applyAccessible()  // Accessibility-friendly
MicroInteractions.Presets.applyBatterySaving() // Reduced battery usage
```

## Advanced Features

### Analytics Integration

```kotlin
MicroInteractions.setAnalyticsHandler { interaction ->
    analytics.track("micro_interaction", mapOf(
        "type" to interaction.name,
        "timestamp" to System.currentTimeMillis()
    ))
}
```

### Debug Mode

```kotlin
// Enable logging for all interactions
MicroInteractions.enableDebugMode(true)
```

### Custom Sounds

Place sound files in `assets/sounds/` directory:

```kotlin
// Preload custom sounds
MicroInteractions.preloadSound("myCustomSound.mp3")

// Use in interaction
.sound(SoundType.CUSTOM("myCustomSound.mp3"))
```

## Control-Specific Extensions

### Button
```kotlin
button.addTapInteraction()
button.showSuccess()
button.showFailure()
```

### Switch/CheckBox
```kotlin
switch.addToggleInteraction()
checkbox.addToggleInteraction()
```

### SeekBar
```kotlin
seekBar.addSlideInteraction()
```

### EditText
```kotlin
editText.addEditingInteraction()
```

### SwipeRefreshLayout
```kotlin
swipeRefreshLayout.addRefreshInteraction()
```

## Compose Modifiers

```kotlin
// Convenience modifiers
Modifier.tapInteraction()
Modifier.successInteraction()
Modifier.failureInteraction()
Modifier.toggleInteraction()

// With configuration
Modifier.microInteraction(
    interaction = MicroInteraction.Bounce,
    trigger = ComposeTrigger.OnClick,
    enabled = true
)
```

## Best Practices

1. **Use Sparingly** - Micro-interactions should enhance, not overwhelm
2. **Be Consistent** - Use the same interactions for similar actions
3. **Respect User Preferences** - The library automatically respects system settings
4. **Test on Device** - Haptics require a physical device
5. **Consider Context** - Reduce feedback in quiet environments

## Example App

Check out the included example app:

```bash
cd example
./gradlew installDebug
```

## Requirements

- Android 5.0+ (API 21+)
- Kotlin 1.9.20+
- Android Studio Hedgehog+

## Permissions

The library requires the following permission (automatically included):

```xml
<uses-permission android:name="android.permission.VIBRATE" />
```

## Sound Resources

The library uses Android system sounds by default. Custom sounds can be added to your app's `assets/sounds/` directory.

## Contributing

We welcome contributions! Please see [CONTRIBUTING.md](CONTRIBUTING.md) for details.

## License

MicroInteractions is released under the MIT license. See [LICENSE](LICENSE) for details.

## Acknowledgments

- Inspired by the micro-interactions concept from Dan Saffer
- iOS SwiftMicroInteractions library by the original creators
- Built with love for the Android community

---

Made with ❤️ by the MicroInteractions Contributors
