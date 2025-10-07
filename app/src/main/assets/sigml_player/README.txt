# Offline Web Avatar (GLB) - Readme

Place the following files into this folder to enable a fully offline 3D avatar in the Translator screen:

1) 3D model and animations
   - avatar.glb
     - A GLB with animation clips. Clip names should match values in animations.json

2) Animation map
   - animations.json
     - Maps text words/phrases (lowercase) to GLB animation clip names, e.g.:
       {
         "hello": "Hello",
         "thank you": "ThankYou"
       }

3) Three.js libraries (local, offline)
   - lib/three.min.js
   - lib/GLTFLoader.js

Folder structure:
  assets/sigml_player/
  ├─ glb_viewer.html            (load with: asset://sigml_player/glb_viewer.html?text={text})
  ├─ animations.json            (word → clip map)
  ├─ avatar.glb                 (your rigged model + clips)
  └─ lib/
     ├─ three.min.js
     └─ GLTFLoader.js

How the Translator uses this
- In the Translator screen, switch to Web Avatar (or let auto-fallback choose it) and set the Custom Avatar URL to:
  asset://sigml_player/glb_viewer.html?text={text}
- The page reads the {text} query, splits into words, and plays the mapped animation clips in sequence.
- If files are missing, it shows a friendly fallback message.

Notes
- This runs entirely offline inside the Android WebView using the app asset domain.
- If you also add a drawable named human_avatar (res/drawable/human_avatar.png), the OfflineAvatarPlayer UI shows your avatar image for the emoji fallback path.
