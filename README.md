# OTTApp — Android OTT Interview Assignment

## Project Overview

A simple Android OTT (over-the-top streaming) app built for the Android Developer
interview assignment. The app has Bottom Navigation with three tabs — **Home**,
**Explore**, and **Profile** — but only **Explore** is implemented in full, since
that's the only screen being evaluated per the brief. Home and Profile are
intentionally blank placeholder screens.

The Explore screen shows a vertically scrolling, full-bleed feed of video rows
(title, description, view count, Watch Now button, and like/save/share/volume
icons), each with an inline autoplaying Media3 (ExoPlayer) video preview, built
against the dummy JSON supplied in the brief.

## Features

- Bottom Navigation (Home / Explore / Profile) using the Navigation Component
- Explore feed: full-bleed video cards with an autoplaying, looping, muted
  inline video preview (Media3 / ExoPlayer)
- Only one video plays at a time — as you scroll, the shared player moves to
  whichever row is currently most visible, and playback pauses automatically
  when the app is backgrounded or the tab isn't visible
- Title, 2-line description, formatted view count (e.g. "24.8K"), Watch Now
  button, and like/save/share/volume icons (all UI-only per the brief)
- MVVM architecture with a Repository layer and LiveData
- Dummy JSON bundled locally as a project asset and parsed with Gson
- Efficient list updates via `ListAdapter` + `DiffUtil`

## Architecture

```
Repository  →  ViewModel  →  Fragment  →  RecyclerView Adapter
```

- **Repository** (`VideoRepository`) is the single source of truth for video
  data. It reads and parses the bundled dummy JSON today, but is written as a
  suspend function returning the same shape a real network call would, so
  swapping in a live endpoint later doesn't require any change to the
  ViewModel or UI.
- **ViewModel** (`ExploreViewModel`) asks the repository for data and exposes
  it as `LiveData<List<VideoItem>>`, surviving configuration changes
  independently of the Fragment.
- **Fragment** (`ExploreFragment`) observes that LiveData, submits it to the
  adapter, and owns the single shared `VideoPlayerManager` used to decide
  which row's video is currently playing.
- **Adapter** (`VideoAdapter`) only binds data to views — it does not own any
  ExoPlayer instances or contain business logic, keeping responsibilities
  cleanly separated.

## Libraries Used

| Library | Purpose |
|---|---|
| AndroidX Core / AppCompat | Base Android platform support |
| Material Components | Bottom navigation, buttons, theming |
| ConstraintLayout | Screen layouts |
| Navigation (fragment-ktx / ui-ktx) | Bottom nav ↔ fragment wiring |
| Lifecycle (ViewModel / LiveData) | MVVM state holders |
| RecyclerView | Explore feed list |
| Media3 (ExoPlayer + UI) | Inline video playback |
| Gson | Parsing the bundled dummy JSON |
| Kotlin Coroutines | Repository/ViewModel async calls |

## Folder Structure

```
app/src/main/java/com/example/ottapp/
├── MainActivity.kt                    # Hosts BottomNavigationView + NavHostFragment
├── data/
│   ├── model/
│   │   ├── VideoItem.kt               # Domain model (matches dummy JSON fields)
│   │   └── ApiResponse.kt             # Mirrors { success, message, data.rows }
│   ├── local/
│   │   └── DummyData.kt               # Reads + Gson-parses assets/dummy_response.json
│   └── repository/
│       └── VideoRepository.kt         # Single data-access point used by the ViewModel
├── viewmodel/
│   └── ExploreViewModel.kt            # Exposes video list as LiveData
├── ui/
│   ├── home/HomeFragment.kt           # Blank screen (not evaluated)
│   ├── explore/ExploreFragment.kt     # Main evaluated screen
│   └── profile/ProfileFragment.kt     # Blank screen (not evaluated)
├── adapter/
│   └── VideoAdapter.kt                # ListAdapter + DiffUtil, binds data only
├── player/
│   └── VideoPlayerManager.kt          # Single shared ExoPlayer used across the feed
└── utils/
    └── ViewCountFormatter.kt          # "24840" -> "24.8K" formatting helper

app/src/main/res/
├── layout/          # activity_main, fragment_home/explore/profile, item_video
├── navigation/       # navigation.xml (nav graph: home / explore / profile)
├── menu/            # menu_bottom.xml (bottom nav items)
├── drawable/        # icons + gradient/badge/button backgrounds
├── values/          # strings, colors, themes
└── assets/dummy_response.json   # Bundled dummy JSON, parsed via Gson
```

## Project Setup / Build Instructions

1. Open the `OTTApp/` folder in Android Studio (**Open an existing project**, not
   "Import").
2. Let Android Studio sync Gradle — on first sync it needs an internet
   connection to download dependencies.
3. Run on an emulator or physical device with **minSdk 24** or higher.
   An internet connection is also needed at runtime, since ExoPlayer streams
   the MP4 files directly from the `cover_video_raw` URLs in the JSON.
4. The app opens directly on the **Explore** tab (set as the nav graph's
   start destination) since that's the screen being evaluated; Home and
   Profile are reachable via the bottom navigation and show a simple
   placeholder screen.

### Versions Used

- **Android Studio**: Iguana / Koala or later (any recent stable release with
  AGP 8.2+ support)
- **Android Gradle Plugin (AGP)**: 8.2.2
- **Gradle**: 8.4
- **Kotlin**: 1.9.22
- **compileSdk / targetSdk**: 34
- **minSdk**: 24

## How to Generate the APK

- **From Android Studio**: **Build → Build Bundle(s) / APK(s) → Build APK(s)**.
  The debug APK is written to `app/build/outputs/apk/debug/app-debug.apk`.
- **From the command line**: `./gradlew assembleDebug` (debug) or
  `./gradlew assembleRelease` (release, unsigned unless a signing config is
  added).
- **Via CI**: this repo includes a GitHub Actions workflow
  (`.github/workflows/build-apk.yml`) that builds the debug APK automatically
  on every push to `main` and uploads it as a downloadable build artifact —
  check the **Actions** tab on GitHub.

## What Should Be Submitted

- This repository (source code, README, Gradle files)
- The generated debug APK (from the CI artifact, or built locally as above)

## Assumptions Made

- **No functionality behind icons/button**: Like, Save, Share, and Volume icons
  and the Watch Now button are static UI elements only, as explicitly stated in
  the brief. They're positioned per the Figma reference but have no
  `OnClickListener` attached.
- **Dummy data is bundled locally, not fetched over HTTP**: The brief provides
  a fixed JSON payload rather than a live endpoint, so it's bundled as
  `assets/dummy_response.json` and parsed with Gson, rather than fetched via
  Retrofit. The repository layer is structured so a real API call could
  replace this with no changes needed elsewhere.
- **Autoplay + loop + muted, one at a time**: Since the brief doesn't specify
  playback behaviour beyond "MP4 video using Media3," the currently-visible
  video autoplays, loops (`REPEAT_MODE_ALL`), and starts muted (`volume = 0f`).
  Only one video plays at a time — a single shared `ExoPlayer` is moved
  between rows based on scroll position, which also keeps memory/CPU usage
  low and avoids multiple overlapping audio tracks.
- **XML over Compose**: The brief for this build required XML + ViewBinding
  (not Jetpack Compose), which also integrates directly with
  `androidx.media3.ui.PlayerView` without an `AndroidView` wrapper.
- **Home/Profile are blank on purpose**: Only Explore is evaluated per the
  brief, so these two fragments render nothing but their layout shell.
- **No coroutines Flow / Paging / DI framework**: Kept intentionally simple
  (LiveData + suspend functions, no Hilt/Koin) to match a straightforward,
  interview-appropriate scope rather than over-engineering the solution.

## Screenshots

_Add screenshots of the running Explore screen here before submitting, e.g.:_

```
docs/screenshot_explore.png
```

## Future Improvements

- Replace the bundled dummy JSON with a real Retrofit-backed network call
  (the repository is already shaped to make this a drop-in change)
- Add pagination for longer video lists
- Wire up real click behaviour for like/save/share and the Watch Now button
- Add unit tests for `VideoRepository` / `ExploreViewModel` and UI tests for
  `ExploreFragment`
- Add a signing config so CI can also produce a signed release APK
