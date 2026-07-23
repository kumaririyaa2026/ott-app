package com.example.ottapp.data.local

import com.example.ottapp.data.model.VideoItem

/**
 * Hard-coded stand-in for the "Series list fetched" API response supplied in
 * the assignment brief. In a production app this would instead be parsed
 * from a network response (e.g. via Retrofit + Moshi/Gson), but the
 * repository layer is written so that swapping this source out later does
 * not require any change to the ViewModel or UI.
 */
object DummyData {

    fun getVideoList(): List<VideoItem> = listOf(
        VideoItem(
            id = 220,
            title = "Galat",
            description = "Galat is a bold psychological drama where 21-year-old Arya's rebellion spirals into a dangerous obsession.",
            thumbnail = "https://cdn.hikecinema.com/uploads/series/images/1780666962062-822379105.webp",
            coverVideoRaw = "https://cdn.hikecinema.com/uploads/series/cover_video/1781615100032-420642525.mp4",
            likes = 1,
            views = 24840,
            ageRating = "U/A 16+"
        ),
        VideoItem(
            id = 205,
            title = "Ilaka 2",
            description = "Ilaka is a gritty coming-of-age drama set in Mumbai's chawls, where ambition and survival shape everyday life.",
            thumbnail = "https://cdn.hikecinema.com/uploads/series/images/1780662444633-778990225.jpeg",
            coverVideoRaw = "https://cdn.hikecinema.com/uploads/series/cover_video/1781098201674-968835617.mp4",
            likes = 0,
            views = 7555,
            ageRating = "U/A 16+"
        ),
        VideoItem(
            id = 217,
            title = "Blackmail",
            description = "Black Mail is a psychological thriller where love, obsession, and betrayal collide with deadly consequences.",
            thumbnail = "https://cdn.hikecinema.com/uploads/series/images/1781096562668-474866623.webp",
            coverVideoRaw = "https://cdn.hikecinema.com/uploads/series/cover_video/1781096532192-611708842.mp4",
            likes = 1,
            views = 31894,
            ageRating = "U/A 16+"
        ),
        VideoItem(
            id = 210,
            title = "Totka",
            description = "TOTKA is a dark horror-comedy about four carefree bachelors whose fun-filled lives take a terrifying turn.",
            thumbnail = "https://cdn.hikecinema.com/uploads/series/images/1781097423468-930868303.webp",
            coverVideoRaw = "https://cdn.hikecinema.com/uploads/series/cover_video/1781097416354-249874661.mp4",
            likes = 0,
            views = 6030,
            ageRating = "U/A 16+"
        ),
        VideoItem(
            id = 202,
            title = "Chor ke Ghar Me Chor",
            description = "A hilarious comedy-drama where a petty thief discovers the house owner is an even bigger thief.",
            thumbnail = "https://cdn.hikecinema.com/uploads/series/images/1781098652169-49829784.webp",
            coverVideoRaw = "https://cdn.hikecinema.com/uploads/series/cover_video/1781098649079-789863469.mp4",
            likes = 0,
            views = 5788,
            ageRating = "U/A 16+"
        ),
        VideoItem(
            id = 201,
            title = "Instant Insaaf 2",
            description = "A legal drama inspired by real Indian court cases featuring intense courtroom battles.",
            thumbnail = "https://cdn.hikecinema.com/uploads/series/images/1781098700751-901186647.webp",
            coverVideoRaw = "https://cdn.hikecinema.com/uploads/series/cover_video/1781098694332-684559202.mp4",
            likes = 0,
            views = 8489,
            ageRating = "U/A 16+"
        ),
        VideoItem(
            id = 211,
            title = "Heartbreaker",
            description = "A seductive crime thriller centered on a mysterious femme fatale targeting powerful men.",
            thumbnail = "https://cdn.hikecinema.com/uploads/series/images/1781097366129-374783431.webp",
            coverVideoRaw = "https://cdn.hikecinema.com/uploads/series/cover_video/1781883615116-689608645.mp4",
            likes = 1,
            views = 19814,
            ageRating = "U/A 16+"
        )
    )
}
