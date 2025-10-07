# Sign Language Video Organization

This directory contains all recorded sign language videos organized by categories:

## Directory Structure
```
signs/
├── numbers/         # Numeric signs (0, 1, 2, 100, 500, etc.)
├── letters/         # Alphabetic signs (a, b, c, etc.)
├── words/          # Single word signs (hello, good, bad, etc.)
├── phrases/        # Multi-word signs (good_morning, thank_you, etc.)
├── metadata/       # JSON mapping files and metadata
└── signs_mapping.json  # Main mapping file for app integration
```

## Video Specifications
- Format: MP4
- Resolution: 1080p (1920x1080)
- Frame Rate: 30fps
- Bitrate: Optimized for mobile playback

## Total Signs Available
- **3,318 total signs** from SiGML files
- Categories automatically detected and organized
- Each sign has a unique identifier matching the SiGML filename

## Usage in Android App
The signs_mapping.json file provides all necessary metadata for:
- Sign name lookup
- Category filtering
- Video file paths
- Sign descriptions
- Search functionality