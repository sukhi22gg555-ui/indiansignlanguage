# Indian Sign Language Translator

## Overview
The ISL Translator screen provides text-to-sign language translation using an integrated avatar system with the SiGML player.

## Features

### ✨ Translator Screen Features
- **Text Input**: Type any message to translate to Indian Sign Language
- **Avatar Display**: Shows sign language animation for the input text
- **Quick Phrases**: Pre-defined common phrases for quick access
- **Replay Controls**: Replay sign language animations
- **Modern UI**: Clean, intuitive design with Material 3 components

### 🤖 Avatar Integration
- **SiGML Player**: Integrated SiGML-Player.exe for sign language animation
- **Multiple Launch Methods**: Tries different approaches to launch the avatar
- **Asset Management**: Automatically extracts player from app assets
- **Error Handling**: Graceful fallbacks when avatar isn't available

## Navigation
The translator can be accessed from:
1. **Home Screen**: Tap "Translator" in bottom navigation
2. **Profile Screen**: Tap "Translator" in bottom navigation  
3. **Direct Navigation**: Using `navController.navigate("translator")`

## Files Added/Modified

### New Files Created
1. **TranslatorScreen.kt** - Main translator UI with modern design
2. **SiGMLPlayerManager.kt** - Avatar integration utility class
3. **file_paths.xml** - FileProvider configuration for asset sharing

### Modified Files
1. **MainActivity.kt** - Added translator route to navigation
2. **HomeScreen.kt** - Connected translator navigation
3. **ProfileScreen.kt** - Connected translator navigation
4. **AndroidManifest.xml** - Added FileProvider and permissions

### Assets Added
1. **SiGML-Player.exe** - Sign language avatar player executable
2. **SignFiles.rar** - Supporting sign language files

## How to Use

### 1. Open Translator
- Launch the app and navigate to the "Translator" tab from bottom navigation

### 2. Enter Text
- Type your message in the text input field
- OR tap one of the Quick Phrase chips

### 3. Translate
- Tap "Translate to Sign Language" button
- The app will attempt to launch the SiGML player with your text

### 4. Control Avatar
- **Replay**: Tap replay button to show animation again  
- **Status**: Tap status button to check player availability

## Technical Details

### Avatar Integration Methods
The app tries multiple methods to launch the SiGML player:

1. **System Command**: Launches via system terminal
2. **Intent Launch**: Opens via Android Intent system  
3. **Direct File Opening**: Uses system file associations

### File Management
- Avatar files are extracted from assets to internal storage
- Files are made executable automatically
- FileProvider enables secure file sharing between apps

### Permissions Required
```xml
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```

## Troubleshooting

### Avatar Not Launching
1. Check if SiGML-Player.exe is in assets folder
2. Verify FileProvider configuration in AndroidManifest.xml
3. Ensure proper permissions are granted
4. Try tapping status button to check player status

### Common Issues
- **"Player not extracted"**: Assets not properly copied
- **"Player file not found"**: Extraction failed, check storage permissions
- **"Unable to launch"**: System doesn't support .exe execution

## Development Notes

### Testing in Android Studio
1. Open project in Android Studio
2. Ensure assets folder contains both executable files
3. Run on device or emulator
4. Test different text inputs and quick phrases

### Customization
- **Quick Phrases**: Edit `quickPhrases` list in `QuickPhrasesSection`
- **UI Colors**: Modify color definitions at top of TranslatorScreen.kt
- **Avatar Methods**: Add more launch methods in SiGMLPlayerManager.kt

## Future Enhancements
- [ ] Support for different sign language dialects
- [ ] Offline sign language database
- [ ] Video recording of sign translations
- [ ] Integration with device camera for sign recognition
- [ ] Custom avatar selection
- [ ] Speed control for sign animations

## Architecture

```
TranslatorScreen
├── InputSection (text input + translate button)
├── AvatarDisplaySection (avatar display area)
├── QuickPhrasesSection (preset phrases)
└── TranslatorBottomNavigationBar (navigation)

SiGMLPlayerManager
├── initializeSiGMLPlayer() (asset extraction)
├── translateText() (main translation function)
├── launchViaSystemCommand() (method 1)
├── launchViaIntent() (method 2)
└── launchDirectly() (method 3)
```

---

**Note**: The SiGML player integration is designed for Android devices but may have limitations since .exe files are Windows executables. For best results, consider using a native Android sign language library or web-based solution.