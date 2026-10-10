package com.example.data.model

enum class DownloadStatus {
    PENDING,
    RUNNING,
    PAUSED,
    SUCCESSFUL,
    FAILED
}

data class MovieItem(
    val id: String,
    val title: String,
    val category: String,
    val year: String,
    val quality: String,
    val size: String,
    val language: String,
    val rating: String,
    val downloadUrl: String,
    val description: String,
    val tags: List<String> = emptyList(),
    val isTrending: Boolean = false
)

data class DownloadTask(
    val downloadId: Long,
    val title: String,
    val url: String,
    val fileName: String,
    val totalBytes: Long,
    val downloadedBytes: Long,
    val status: DownloadStatus,
    val progressPercent: Int = 0,
    val dateAdded: Long = System.currentTimeMillis()
)

data class BookmarkItem(
    val id: String,
    val title: String,
    val quality: String,
    val downloadUrl: String,
    val category: String,
    val addedAt: Long = System.currentTimeMillis()
)

data class RecentMovieLink(
    val id: String,
    val title: String,
    val url: String,
    val quality: String = "1080p FHD",
    val size: String = "1.8 GB",
    val timeLabel: String = "Today",
    val timestamp: Long = System.currentTimeMillis()
)

data class HelpGuide(
    val id: String,
    val title: String,
    val summary: String,
    val steps: List<String>,
    val tips: String
)
