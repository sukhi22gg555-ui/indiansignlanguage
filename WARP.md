# WARP.md

This file provides guidance to WARP (warp.dev) when working with code in this repository.

## Project Overview

This is an Android application for learning Indian Sign Language built with **Jetpack Compose**, **Firebase**, and **Kotlin**. The app provides interactive lessons, progress tracking, and user authentication for sign language learning.

## Technology Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose with Material 3
- **Backend**: Firebase (Auth, Firestore)
- **Architecture**: MVVM pattern with ViewModels
- **Navigation**: Jetpack Navigation Compose
- **Media**: ExoPlayer for video content
- **Image Loading**: Coil

## Common Development Commands

### Build and Run
```powershell
# Clean and build the project
./gradlew clean build

# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Install debug APK on connected device/emulator
./gradlew installDebug

# Run unit tests
./gradlew test

# Run instrumented tests (requires device/emulator)
./gradlew connectedAndroidTest

# Run specific test class
./gradlew test --tests "com.example.indiansignlanguage.AuthViewModelTest"

# Run lint checks
./gradlew lint

# Generate lint report
./gradlew lintDebug
```

### Debugging and Development
```powershell
# View connected devices
adb devices

# View logs (filter by app)
adb logcat | findstr "com.example.indiansignlanguage"

# Clear app data
adb shell pm clear com.example.indiansignlanguage

# Uninstall app
adb uninstall com.example.indiansignlanguage

# Check dependencies for vulnerabilities
./gradlew dependencyCheckAnalyze
```

## Architecture Overview

### Core Components

1. **MainActivity**: Single activity hosting all Compose screens with Navigation
2. **Screen Components**: Compose UI screens (HomeScreen, LoginScreen, etc.)
3. **ViewModels**: Business logic and state management (AuthViewModel)
4. **Data Layer**: Firebase integration and data models
5. **Utils**: Firebase utilities and helper functions

### Key Architectural Patterns

- **Single Activity Architecture**: All screens are Composables within MainActivity
- **Navigation**: Uses Jetpack Navigation Compose with string-based routes
- **State Management**: ViewModels with Firebase integration
- **Data Flow**: Screen → ViewModel → FirebaseUtils → Firebase Backend

### Package Structure
```
com.example.indiansignlanguage/
├── data/              # Data models (UserModels.kt)
├── ui/theme/          # Compose theming (Color, Theme, Type)
├── utils/             # Firebase utilities and helpers
├── *.kt              # Screen composables and ViewModels
```

### Firebase Integration

The app uses Firebase for:
- **Authentication**: Email/password sign-in/up
- **Firestore**: User profiles, lesson progress, achievements
- **Collections Structure**:
  - `users/{userId}` - User profiles
  - `users/{userId}/lessonProgress` - Individual lesson tracking
  - `users/{userId}/moduleProgress` - Module completion tracking  
  - `users/{userId}/dailyStats` - Learning statistics
  - `users/{userId}/achievements` - Unlocked achievements

### Data Models

Key data classes in `data/UserModels.kt`:
- `UserProfile`: Main user data and overall progress
- `LessonProgress`: Individual lesson completion tracking
- `ModuleProgress`: Module-level progress (Numbers, Greetings, etc.)
- `LearningStats`: Daily/weekly learning statistics
- `Achievement`: Achievement system data

## Development Guidelines

### Adding New Screens
1. Create new Composable function (e.g., `NewScreen.kt`)
2. Add navigation route in `MainActivity.kt` NavHost
3. Update navigation calls in existing screens
4. Follow existing naming convention: `ScreenName.kt`

### Firebase Operations
- Use `FirebaseUtils` object for all Firebase operations
- All Firebase calls are suspend functions - use in ViewModels with proper coroutine scopes
- Handle Results properly - all Firebase functions return `Result<T>`
- Check user authentication before Firebase operations

### UI Development
- Follow Material 3 design system
- Use consistent color scheme from `ui/theme/Color.kt`
- Maintain consistent spacing and typography
- Test on different screen sizes
- Use `@Preview` annotations for Compose previews

### Testing Strategy
- Unit tests for ViewModels and utility functions
- UI tests for critical user flows
- Firebase Security Rules testing
- Device testing on multiple Android versions (minSdk 24, targetSdk 36)

### Code Organization
- Keep Composables focused and small
- Extract reusable UI components
- Use meaningful variable and function names
- Add documentation for complex Firebase operations
- Follow Kotlin coding conventions

## Important Configuration

### Build Configuration
- `minSdk`: 24 (Android 7.0)
- `targetSdk`: 36 (Android 14)
- `compileSdk`: 36
- Java compatibility: Version 11

### Key Dependencies
- Jetpack Compose BOM: 2024.09.00
- Firebase BOM: 33.1.1
- ExoPlayer: 1.3.1 (for video content)
- Navigation Compose: 2.7.7
- Coil: 2.6.0 (image loading)

### Firebase Setup
- `google-services.json` is required in `app/` directory
- Firebase configuration is handled via Google Services plugin
- Internet permission is declared in AndroidManifest.xml

## File Locations

### Source Code
- Main source: `app/src/main/java/com/example/indiansignlanguage/`
- Resources: `app/src/main/res/`
- Tests: `app/src/test/` and `app/src/androidTest/`

### Configuration Files
- App build config: `app/build.gradle.kts`
- Project build config: `build.gradle.kts`
- Version catalog: `gradle/libs.versions.toml`
- Firebase config: `app/google-services.json`

### Key Files to Understand
1. `MainActivity.kt` - App entry point and navigation setup
2. `AuthViewModel.kt` - Authentication logic
3. `FirebaseUtils.kt` - All Firebase operations
4. `data/UserModels.kt` - Core data structures
5. `HomeScreen.kt` - Main app interface after login

## Current App Features

- User authentication (sign up/login)
- Learning modules (Numbers, Greetings, Alphabets, etc.)
- Progress tracking and statistics
- Achievement system
- User profile management
- Search functionality
- Video-based learning content support

The app is designed for scalable learning content with robust progress tracking and user engagement features.