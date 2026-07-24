# OTTApp

Android OTT app assignment - Bottom Navigation with Home, Explore and Profile
tabs. Only Explore is fully built out since that's the screen being
evaluated, Home and Profile are just blank screens.

Explore shows a scrolling feed of videos (from the dummy JSON given in the
assignment) with an inline autoplaying video preview, title, description,
view count, a Watch Now button, and like/save/share/volume icons.

## Tech stack

- Kotlin
- XML layouts + ViewBinding
- MVVM
- Navigation Component
- Media3 / ExoPlayer for video
- Gson for parsing the dummy JSON
- Coroutines + LiveData

## How it works

- `VideoRepository` reads `assets/dummy_response.json` and parses it with
  Gson. It's a suspend function so swapping this for a real API call later
  is a one-line change.
- `ExploreViewModel` calls the repo and exposes the list as LiveData.
- `ExploreFragment` observes that and submits it to a `ListAdapter`
  (`VideoAdapter`) in a RecyclerView.
- Only one video plays at a time. Instead of giving every row its own
  ExoPlayer, there's a single shared player (`VideoPlayerManager`) that gets
  attached to whichever row is closest to the center of the screen as you
  scroll. Keeps memory/CPU usage down and there's no overlapping audio.
- The adapter itself doesn't touch ExoPlayer at all, it just binds
  title/description/view count. All the player logic lives in the fragment.

## Project structure

```
app/src/main/java/com/example/ottapp/
├── MainActivity.kt
├── data/
│   ├── model/          VideoItem, ApiResponse
│   ├── local/           DummyData (reads + parses the json asset)
│   └── repository/     VideoRepository
├── viewmodel/          ExploreViewModel
├── ui/
│   ├── home/
│   ├── explore/
│   └── profile/
├── adapter/             VideoAdapter
├── player/              VideoPlayerManager
└── utils/               ViewCountFormatter
```

## Running it

1. Open the project in Android Studio (Open existing project)
2. Let it sync gradle
3. Run on minSdk 24+, needs internet since the videos stream from the URLs
   in the JSON
4. Opens directly on the Explore tab

Versions I used: Android Studio Iguana/Koala or later, AGP 8.2.2, Gradle
8.4, Kotlin 1.9.22, compileSdk/targetSdk 34, minSdk 24.

## Generating the APK

Build > Build Bundle(s)/APK(s) > Build APK(s) in Android Studio, or
`./gradlew assembleDebug`. Also set up a GitHub Actions workflow
(`.github/workflows/build-apk.yml`) that builds the debug APK on every push
and uploads it as an artifact under the Actions tab.

## Assumptions

- Like/Save/Share/Volume icons and the Watch Now button are UI only, no
  click behaviour, as stated in the brief.
- Used the bundled dummy JSON instead of a real API since that's what was
  given in the assignment.
- Videos autoplay, loop, and are muted by default since the brief doesn't
  say otherwise. Only one plays at a time.
- Went with XML/ViewBinding instead of Compose since it integrates more
  directly with `PlayerView`.
- Home and Profile are just empty screens since only Explore is evaluated.
- Kept things simple - no Hilt/Koin, no Paging, no Flow - just LiveData and
  suspend functions since that felt like the right scope for this.
