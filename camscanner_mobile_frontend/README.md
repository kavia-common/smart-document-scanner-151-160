# CamScanner Mobile Frontend

An advanced mobile Android application written in Kotlin that allows users to capture, enhance, and manage scanned documents with features like:
- AI-powered image enhancement (brightness/contrast, denoise, sharpen)
- Auto edge detection and cropping
- OCR for text extraction
- PDF merging and exporting
- Document organization and search
- User authentication
- Cloud sync for scans

UI:
- Light theme with modern minimalistic style
- Bottom navigation bar for Home (Documents), Scan, and Account
- Floating action button to start scanning
- Modal panels for scan preview and edit

Tech notes:
- Traditional Android Views with XML layouts (no Jetpack Compose)
- AndroidX libraries with explicit versions
- ViewBinding
- CameraX for capture
- ML Kit Text Recognition for OCR
- Open-source image processing via RenderScript (legacy) fallback/simple algorithms and ColorMatrix
- PDF generation via Android PdfDocument
- Room for local persistence
- WorkManager for cloud sync scaffolding

Environment variables (BuildConfig):
- API_BASE_URL: Base URL for backend/cloud sync (if any)
- OAUTH_CLIENT_ID: For authentication
- OAUTH_REDIRECT_URI: OAuth redirect
- CLOUD_BUCKET: Destination bucket/path for storing scans, if applicable

See .env.example in project root for required variables.

Build:
  ./gradlew build

Run:
  ./gradlew :app:installDebug
  Launch "Smart Document Scanner"
