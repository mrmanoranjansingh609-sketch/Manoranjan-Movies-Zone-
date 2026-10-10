package com.example.data.repository

import com.example.data.model.HelpGuide
import com.example.data.model.MovieItem

object MovieRepository {

    val categories = listOf(
        "All",
        "Bollywood",
        "Hollywood Dubbed",
        "South Hindi",
        "Web Series",
        "Action & Sci-Fi",
        "Comedy & Drama"
    )

    val sampleMovies = listOf(
        MovieItem(
            id = "m1",
            title = "KGF Chapter 2 (Hindi)",
            category = "South Hindi",
            year = "2022",
            quality = "1080p FHD",
            size = "1.8 GB",
            language = "Hindi DD 5.1",
            rating = "8.3",
            downloadUrl = "https://sites.google.com/view/manoranjanmovieszone/home",
            description = "In the blood-soaked Kolar Gold Fields, Rocky's name strikes fear into his foes. While his allies look up to him, the government sees him as a threat to law and order.",
            tags = listOf("Action", "Blockbuster", "HD"),
            isTrending = true
        ),
        MovieItem(
            id = "m2",
            title = "Jawan (Extended Cut)",
            category = "Bollywood",
            year = "2023",
            quality = "4K UHD / 1080p",
            size = "2.4 GB",
            language = "Hindi",
            rating = "7.8",
            downloadUrl = "https://sites.google.com/view/manoranjanmovieszone/home",
            description = "A high-octane action thriller outlining the emotional journey of a man who is set out to rectify the wrongs in society driven by a personal vendetta.",
            tags = listOf("Thriller", "Shah Rukh Khan", "Superhit"),
            isTrending = true
        ),
        MovieItem(
            id = "m3",
            title = "Avengers: Endgame (Hindi Dubbed)",
            category = "Hollywood Dubbed",
            year = "2019",
            quality = "1080p Dual Audio",
            size = "2.1 GB",
            language = "Hindi + English",
            rating = "8.4",
            downloadUrl = "https://sites.google.com/view/manoranjanmovieszone/home",
            description = "After the devastating events of Infinity War, the universe is in ruins. With the help of remaining allies, the Avengers assemble once more to reverse Thanos' actions.",
            tags = listOf("Marvel", "Sci-Fi", "Dual Audio"),
            isTrending = true
        ),
        MovieItem(
            id = "m4",
            title = "Pushpa 2: The Rule (Hindi)",
            category = "South Hindi",
            year = "2024",
            quality = "1080p WEB-DL",
            size = "1.9 GB",
            language = "Hindi",
            rating = "8.1",
            downloadUrl = "https://sites.google.com/view/manoranjanmovieszone/home",
            description = "The clashes escalate as Pushpa Raj expands his red sandalwood empire while squaring off against SP Bhanwar Singh Shekhawat in an explosive showdown.",
            tags = listOf("Action", "Allu Arjun", "Trending"),
            isTrending = true
        ),
        MovieItem(
            id = "m5",
            title = "Panchayat Season 3",
            category = "Web Series",
            year = "2024",
            quality = "720p / 1080p",
            size = "3.2 GB Complete",
            language = "Hindi",
            rating = "8.9",
            downloadUrl = "https://sites.google.com/view/manoranjanmovieszone/home",
            description = "Abhishek Tripathi continues navigating village politics, local disputes, and his CAT exam preparations in Phulera village with beloved characters.",
            tags = listOf("Comedy", "Drama", "TVF"),
            isTrending = false
        ),
        MovieItem(
            id = "m6",
            title = "Oppenheimer (Hindi Dubbed)",
            category = "Hollywood Dubbed",
            year = "2023",
            quality = "1080p IMAX Audio",
            size = "2.3 GB",
            language = "Hindi + English",
            rating = "8.9",
            downloadUrl = "https://sites.google.com/view/manoranjanmovieszone/home",
            description = "The story of American scientist J. Robert Oppenheimer and his role in the development of the atomic bomb during the Manhattan Project.",
            tags = listOf("Biographical", "Nolan", "Oscar Winner"),
            isTrending = false
        ),
        MovieItem(
            id = "m7",
            title = "Stree 2: Sarkate Ka Aatank",
            category = "Bollywood",
            year = "2024",
            quality = "1080p FHD",
            size = "1.7 GB",
            language = "Hindi Atmos",
            rating = "7.6",
            downloadUrl = "https://sites.google.com/view/manoranjanmovieszone/home",
            description = "Chanderi town is haunted by a headless monster that kidnaps modern women. Vicky and his loyal friends team up with Stree to save the town.",
            tags = listOf("Horror-Comedy", "Superhit", "Hindi"),
            isTrending = true
        ),
        MovieItem(
            id = "m8",
            title = "Salaar: Part 1 – Ceasefire (Hindi)",
            category = "South Hindi",
            year = "2023",
            quality = "1080p Dual Audio",
            size = "2.2 GB",
            language = "Hindi Dubbed",
            rating = "7.5",
            downloadUrl = "https://sites.google.com/view/manoranjanmovieszone/home",
            description = "A fate-deciding friendship between Deva and Varadha in the dystopian city-state of Khansaar leads to intense warfare and underworld rivalries.",
            tags = listOf("Prabhas", "Prashanth Neel", "Action"),
            isTrending = false
        ),
        MovieItem(
            id = "m9",
            title = "Mirzapur Season 3",
            category = "Web Series",
            year = "2024",
            quality = "1080p All Episodes",
            size = "4.1 GB",
            language = "Hindi",
            rating = "8.5",
            downloadUrl = "https://sites.google.com/view/manoranjanmovieszone/home",
            description = "The battle for the throne of Mirzapur intensifies with Guddu Pandit and Golu facing rising factions in Purvanchal.",
            tags = listOf("Crime", "Thriller", "Action"),
            isTrending = false
        )
    )

    val helpGuides = listOf(
        HelpGuide(
            id = "g1",
            title = "How to Download Movies from Manoranjan Zone",
            summary = "Quick 4-step instructions to get direct download links with maximum speed.",
            steps = listOf(
                "Open 'Movies Zone' from the app home screen or drawer menu.",
                "Search or click on your desired movie poster or title.",
                "Choose your preferred resolution (480p, 720p, or 1080p FHD).",
                "Click the 'Direct Download' or 'Fast Server' button. The app's built-in download manager will automatically capture the file and notify you!"
            ),
            tips = "If you encounter a timer or link verification page, wait 5 seconds and click 'Get Link'. Never click unknown pop-up ads."
        ),
        HelpGuide(
            id = "g2",
            title = "Bypassing Shorteners & Ads",
            summary = "Tips to navigate through cloud storage mirrors and shorteners safely.",
            steps = listOf(
                "Use the in-app browser or Chrome for best compatibility.",
                "Look for 'Dual Audio' or 'Direct Google Drive' server options.",
                "If redirected to a new tab with an ad, press Back immediately to return to the original portal.",
                "Once the direct video stream or link appears, copy the link into the app's 'Link Grabber' tab for high-speed multi-threaded download."
            ),
            tips = "You can paste ANY direct video or download link into the 'Downloader' tab to download straight to your phone's storage."
        ),
        HelpGuide(
            id = "g3",
            title = "Recommended Video Players",
            summary = "Best players for smooth playback of MKV, MP4, and multi-audio tracks.",
            steps = listOf(
                "VLC for Android: Plays all audio codecs (E-AC3, TrueHD, DTS) and subtitles seamlessly.",
                "MX Player: Excellent HW+ hardware acceleration for 1080p and 4K playback.",
                "To change audio from English to Hindi on dual-audio movies, tap the Audio track icon inside VLC/MX Player."
            ),
            tips = "Always ensure your device has sufficient free storage space before starting 1080p FHD downloads."
        )
    )
}
