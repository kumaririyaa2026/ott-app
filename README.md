# OTTApp — Android OTT Assignment

A simple Android OTT app with Bottom Navigation (Home / Explore / Profile). Only the
**Explore** screen is implemented in full, per the brief — Home and Profile are
intentionally blank placeholder screens.

## Tech Stack

- Kotlin
- XML layouts (View system, not Compose)
- MVVM architecture
- Media3 (ExoPlayer) for inline video playback
- Glide (available for thumbnail loading, if extended later)
- Kotlin Coroutines + LiveData

## Project Structure

```
app/src/main/java/com/example/ottapp/
├── MainActivity.kt              # Hosts BottomNavigationView + fragment swapping
├── home/HomeFragment.kt         # Blank screen
├── profile/ProfileFragment.kt   # Blank screen
├── explore/
│   ├── ExploreFragment.kt       # Main evaluated screen
│   ├── ExploreViewModel.kt      # Exposes video list as LiveData
│   └── VideoAdapter.kt          # RecyclerView adapter, owns one ExoPlayer per item
└── data/
    ├── model/VideoItem.kt       # Domain model (matches dummy JSON fields)
    ├── model/ApiResponse.kt     # Mirrors the { success, message, data.rows } envelope
    ├── local/DummyData.kt       # Hard-coded copy of the dummy JSON from the brief
    └── repository/VideoRepository.kt  # Single data-access point used by the ViewModel
```

## How the Explore screen works

- `ExploreViewModel` asks `VideoRepository` (a `suspend fun`) for the list of videos on
  init and exposes it via `LiveData<List<VideoItem>>`.
- `VideoRepository` currently returns `DummyData.getVideoList()`, which is a literal
  Kotlin copy of the dummy JSON payload from the assignment (same `rows` list, same
  fields). It's written as a suspend function returning the same shape a real Retrofit
  call would return, so swapping in a live network call later only means changing the
  inside of `VideoRepository.getVideos()` — the ViewModel and UI don't need to change.
- `ExploreFragment` observes the LiveData and submits it to a `ListAdapter`
  (`VideoAdapter`) shown in a `RecyclerView`.
- Each row (`item_video.xml`) shows:
  - An inline `androidx.media3.ui.PlayerView` playing the `cover_video_raw` MP4 URL
    (looped, muted by default, controller hidden — auto-plays like a social feed
    preview).
  - Title (1 line, truncated)
  - Description (2 lines, truncated)
  - "Watch Now" button
  - Like / Save / Share icons overlaid top-right of the video
  - Volume icon overlaid bottom-right of the video
  - None of the icons or the Watch Now button have click behaviour wired up — this
    matches the brief, which asks for **UI only** for these elements.
- `VideoAdapter` creates one `ExoPlayer` instance per bound view holder and releases it
  in `onViewRecycled` / `onFailedToRecycleView`, so scrolling through a long list does
  not leak players.

## Project Setup Instructions

1. Open the `OTTApp/` folder in Android Studio (**Open an existing project**, not
   "Import").
2. Let Android Studio sync Gradle. On first open it will prompt to regenerate the
   Gradle wrapper JAR — accept this (or run `gradle wrapper` from the terminal if you
   have Gradle installed locally); the wrapper JAR binary itself is not checked into
   this submission.
3. Ensure you have an internet connection on first sync (Gradle needs to download
   dependencies) and on first run (ExoPlayer needs to stream the MP4 URLs from
   `cover_video_raw`).
4. Run on an emulator or device with **minSdk 24** or higher.
5. The app opens directly on the **Explore** tab since that's the screen being
   evaluated; Home and Profile are reachable via the bottom navigation and show a
   simple placeholder label.

### Versions used

- Android Studio: Iguana / Koala or later (any recent stable release with AGP 8.2+
  support)
- Android Gradle Plugin (AGP): 8.2.2
- Gradle: 8.4
- Kotlin: 1.9.22
- compileSdk / targetSdk: 34
- minSdk: 24

## Assumptions Made

- **No functionality behind icons/button**: Like, Save, Share, and Volume icons and the
  Watch Now button are static UI elements only, as explicitly stated in the brief. They
  are wired up visually (overlaid on the video, positioned per the Figma reference) but
  have no `OnClickListener` attached.
- **Dummy data is local, not fetched over HTTP**: The brief provides a fixed JSON
  payload rather than a live endpoint to call, so it's embedded as a Kotlin object
  (`DummyData`) instead of being fetched via Retrofit. The repository layer is
  structured so a real API call could replace this with no changes needed elsewhere.
- **Autoplay + loop + muted**: Since the brief doesn't specify playback behaviour
  beyond "MP4 video using Media3," each visible video autoplays on bind, loops
  (`REPEAT_MODE_ALL`), and starts muted (`volume = 0f`) — this is standard behaviour
  for social/OTT-style feed previews and avoids multiple overlapping audio tracks
  playing at once as the user scrolls. The volume icon is present as UI only, per the
  brief, so this default isn't user-togglable in this build.
- **XML over Compose**: The brief allows either; XML + ViewBinding was chosen for
  faster, more predictable integration with `androidx.media3.ui.PlayerView` (Compose
  would need an `AndroidView` wrapper around the same `PlayerView` either way).
  Migrating to Compose is a mechanical follow-up if preferred.
- **Home/Profile are blank on purpose**: Only Explore is evaluated per the brief, so
  these two fragments just render a centered label and nothing else.
- **No APK included**: Only source code is included in this submission; the project
  can be built and an APK generated via Android Studio (**Build > Build Bundle(s) /
  APK(s) > Build APK(s)**) or `./gradlew assembleDebug` once the Gradle wrapper is
  regenerated.
