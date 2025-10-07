# Sign Language Video Recording Setup Guide

## Required Software

### Option 1: OBS Studio (Recommended - Free)
1. Download from: https://obsproject.com/
2. Install with default settings
3. Configuration for sign recording:
   - **Resolution**: 1920x1080 (1080p)
   - **Frame Rate**: 30 FPS
   - **Output Format**: MP4
   - **Video Bitrate**: 5000-8000 Kbps
   - **Audio**: Disable (sign language is visual only)

### Option 2: Windows Game Bar (Built-in)
- Press `Win + G` to open Game Bar
- Click record button or press `Win + Alt + R`
- Settings: Go to Settings → Gaming → Captures
  - Set video quality to "High"
  - Ensure resolution is 1080p

### Option 3: Third-party Alternatives
- **Bandicam** (paid but excellent quality)
- **Camtasia** (paid, great for editing)
- **ShareX** (free, lightweight)

## Recording Configuration

### OBS Studio Setup Steps:
1. Open OBS Studio
2. Create a new Scene: "Sign Recording"
3. Add Source → Display Capture
4. Select the monitor where SiGML Player will run
5. Crop the capture area to just the sign animation window

### Optimal Settings:
```
Video Settings:
- Base Resolution: 1920x1080
- Output Resolution: 1920x1080
- Downscale Filter: Lanczos
- FPS: 30

Output Settings:
- Output Mode: Advanced
- Type: Standard
- Recording Path: C:\Users\Administrator\Downloads\SignRecordings\videos\
- Recording Format: mp4
- Video Encoder: x264
- Rate Control: CBR
- Bitrate: 6000
```

## Recording Workflow

### Preparation (One-time setup):
1. Position SiGML Player window consistently on screen
2. Set up recording area to capture just the sign animation
3. Create hotkeys for start/stop recording (F9/F10 recommended)
4. Test with a few sample files to ensure quality

### Recording Process:
1. Start your recording software
2. Run the automated script (TestProcessor.bat first, then RobustSignProcessor.ps1)
3. For each sign:
   - Script opens SiGML file in player
   - Start recording when animation begins
   - Let the full animation play (usually 3-5 seconds)
   - Stop recording
   - Save with the sign name (script shows this)
   - Close SiGML Player to proceed to next file

### Keyboard Shortcuts:
- `F9` - Start Recording
- `F10` - Stop Recording  
- `Alt + F4` - Close SiGML Player window
- `Ctrl + C` - Stop batch script if needed

## File Naming Convention

The script will show you the sign name for each file. Save your recordings as:
- Numbers: `0.mp4`, `1.mp4`, `100.mp4`, etc.
- Letters: `a.mp4`, `b.mp4`, `c.mp4`, etc.  
- Words: `hello.mp4`, `good.mp4`, `bad.mp4`, etc.
- Phrases: `good_morning.mp4`, `thank_you.mp4`, etc.

## Quality Control Checklist

Before starting mass recording:
- [ ] Test recording captures clear animation
- [ ] Audio is disabled (not needed for sign language)
- [ ] File size is reasonable (1-5MB per sign)
- [ ] Recording hotkeys work smoothly
- [ ] SiGML Player opens and closes properly
- [ ] Batch script runs without errors

## Estimated Time Requirements

- **Setup and testing**: 1-2 hours
- **Recording all 3,318 signs**: 8-12 hours
  - ~10-15 seconds per sign (including script automation)
  - Recommended: Work in batches of 200-300 signs per session
- **Post-processing and organization**: 2-4 hours

## Tips for Efficient Recording

1. **Work in batches**: Process 200-500 signs at a time
2. **Use consistent lighting**: Keep screen brightness stable
3. **Take breaks**: Every 300-500 signs to maintain focus
4. **Monitor disk space**: Each sign ~2-5MB = ~15GB total space needed
5. **Backup regularly**: Copy completed videos to backup location

## Troubleshooting

### SiGML Player Issues:
- **Won't open**: Check if shortcut points to correct .exe file
- **Crashes**: Run as Administrator
- **Animation doesn't play**: Update graphics drivers

### Recording Issues:
- **Poor quality**: Increase bitrate in recording settings
- **Large file sizes**: Reduce bitrate or change encoder settings
- **Choppy playback**: Lower recording frame rate to 25 FPS

### Batch Script Issues:
- **Script won't run**: Run PowerShell as Administrator
- **Execution policy error**: Run: `Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser`
- **SiGML Player path wrong**: Update path in script

## Next Steps After Recording

1. Organize videos into proper folder structure
2. Convert to consistent format if needed
3. Generate JSON mapping file for app integration
4. Test video playback in Android app
5. Implement search and filtering functionality

---

**Ready to start?** Run the TestProcessor.bat first to test with 5 files, then proceed with the full RobustSignProcessor.ps1 for all 3,318 signs!