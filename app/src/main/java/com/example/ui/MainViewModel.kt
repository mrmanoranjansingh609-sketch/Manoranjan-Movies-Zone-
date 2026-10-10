package com.example.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.example.data.downloader.DownloadController
import com.example.data.model.BookmarkItem
import com.example.data.model.DownloadTask
import com.example.data.model.MovieItem
import com.example.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class AppDestination {
    HOME,
    PORTAL_MOVIES,
    PORTAL_HELP,
    DOWNLOADER,
    BOOKMARKS
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val downloadController = DownloadController(application)
    val downloadTasks: StateFlow<List<DownloadTask>> = downloadController.tasks

    private val _currentDestination = MutableStateFlow(AppDestination.HOME)
    val currentDestination: StateFlow<AppDestination> = _currentDestination.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _isAboutDialogVisible = MutableStateFlow(false)
    val isAboutDialogVisible: StateFlow<Boolean> = _isAboutDialogVisible.asStateFlow()

    // Portal WebView active URL
    private val _portalUrl = MutableStateFlow("https://sites.google.com/view/manoranjanmovieszone/home")
    val portalUrl: StateFlow<String> = _portalUrl.asStateFlow()

    private val _portalTitle = MutableStateFlow("Manoranjan Movies Zone")
    val portalTitle: StateFlow<String> = _portalTitle.asStateFlow()

    // Bookmarks
    private val _bookmarks = MutableStateFlow<List<BookmarkItem>>(emptyList())
    val bookmarks: StateFlow<List<BookmarkItem>> = _bookmarks.asStateFlow()

    // Recent Downloads local state
    private val _recentDownloads = MutableStateFlow<List<com.example.data.model.RecentMovieLink>>(emptyList())
    val recentDownloads: StateFlow<List<com.example.data.model.RecentMovieLink>> = _recentDownloads.asStateFlow()

    private val prefs = application.getSharedPreferences("manoranjan_prefs", Context.MODE_PRIVATE)

    init {
        loadBookmarks()
        loadRecentDownloads()
    }

    fun navigateTo(dest: AppDestination) {
        _currentDestination.value = dest
    }

    fun openPortal(url: String, title: String) {
        _portalUrl.value = url
        _portalTitle.value = title
        _currentDestination.value = if (url.contains("xyz-helps")) AppDestination.PORTAL_HELP else AppDestination.PORTAL_MOVIES
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedCategory(cat: String) {
        _selectedCategory.value = cat
    }

    fun setAboutDialogVisible(visible: Boolean) {
        _isAboutDialogVisible.value = visible
    }

    fun startDownload(url: String, fileName: String? = null) {
        val downloadTitle = if (!fileName.isNullOrBlank()) {
            fileName.removeSuffix(".mp4").removeSuffix(".mkv")
        } else {
            android.net.Uri.parse(url).lastPathSegment?.takeIf { it.isNotEmpty() } ?: "Direct Movie Stream"
        }
        addRecentDownload(
            title = downloadTitle,
            url = url,
            quality = if (downloadTitle.contains("4K", ignoreCase = true)) "4K UHD" else if (downloadTitle.contains("720p", ignoreCase = true)) "720p HD" else "1080p FHD",
            size = "1.8 GB"
        )
        downloadController.startDownload(url, fileName)
    }

    fun addRecentDownload(title: String, url: String, quality: String = "1080p FHD", size: String = "1.8 GB") {
        val current = _recentDownloads.value.toMutableList()
        current.removeAll { it.url.equals(url, ignoreCase = true) || it.title.equals(title, ignoreCase = true) }
        val newItem = com.example.data.model.RecentMovieLink(
            id = "rec_${System.currentTimeMillis()}",
            title = title,
            url = url,
            quality = quality,
            size = size,
            timeLabel = "Just now",
            timestamp = System.currentTimeMillis()
        )
        current.add(0, newItem)
        val limited = current.take(20)
        _recentDownloads.value = limited
        saveRecentDownloads(limited)
    }

    fun removeRecentDownload(id: String) {
        val updated = _recentDownloads.value.filter { it.id != id }
        _recentDownloads.value = updated
        saveRecentDownloads(updated)
    }

    fun clearRecentDownloads() {
        _recentDownloads.value = emptyList()
        prefs.edit().remove("saved_recent_downloads").apply()
    }

    fun cancelDownload(downloadId: Long) {
        downloadController.cancelTask(downloadId)
    }

    fun openDownloadedFile(task: DownloadTask) {
        downloadController.openDownloadedFile(task)
    }

    fun toggleBookmark(movie: MovieItem) {
        val current = _bookmarks.value.toMutableList()
        val existing = current.find { it.id == movie.id }
        if (existing != null) {
            current.remove(existing)
        } else {
            current.add(
                BookmarkItem(
                    id = movie.id,
                    title = movie.title,
                    quality = movie.quality,
                    downloadUrl = movie.downloadUrl,
                    category = movie.category
                )
            )
        }
        _bookmarks.value = current
        saveBookmarks(current)
    }

    fun isBookmarked(movieId: String): Boolean {
        return _bookmarks.value.any { it.id == movieId }
    }

    fun removeBookmark(id: String) {
        val updated = _bookmarks.value.filter { it.id != id }
        _bookmarks.value = updated
        saveBookmarks(updated)
    }

    private fun loadBookmarks() {
        val savedTitles = prefs.getStringSet("saved_bookmarks", emptySet()) ?: emptySet()
        val loaded = savedTitles.mapNotNull { itemStr ->
            val parts = itemStr.split("|||")
            if (parts.size >= 5) {
                BookmarkItem(
                    id = parts[0],
                    title = parts[1],
                    quality = parts[2],
                    downloadUrl = parts[3],
                    category = parts[4]
                )
            } else null
        }
        _bookmarks.value = loaded
    }

    private fun saveBookmarks(items: List<BookmarkItem>) {
        val set = items.map { "${it.id}|||${it.title}|||${it.quality}|||${it.downloadUrl}|||${it.category}" }.toSet()
        prefs.edit().putStringSet("saved_bookmarks", set).apply()
    }

    private fun loadRecentDownloads() {
        val saved = prefs.getStringSet("saved_recent_downloads", null)
        if (saved != null && saved.isNotEmpty()) {
            val list = saved.mapNotNull { itemStr ->
                val parts = itemStr.split("|||")
                if (parts.size >= 6) {
                    com.example.data.model.RecentMovieLink(
                        id = parts[0],
                        title = parts[1],
                        url = parts[2],
                        quality = parts[3],
                        size = parts[4],
                        timeLabel = parts[5],
                        timestamp = parts.getOrNull(6)?.toLongOrNull() ?: System.currentTimeMillis()
                    )
                } else null
            }.sortedByDescending { it.timestamp }
            _recentDownloads.value = list
        } else {
            // Default initial local state so user sees realistic recently accessed movie links right away
            val initial = listOf(
                com.example.data.model.RecentMovieLink(
                    id = "rec_1",
                    title = "KGF Chapter 2 (Hindi Dubbed)",
                    url = "https://sites.google.com/view/manoranjanmovieszone/home",
                    quality = "1080p FHD",
                    size = "1.8 GB",
                    timeLabel = "15 mins ago",
                    timestamp = System.currentTimeMillis() - 15 * 60 * 1000
                ),
                com.example.data.model.RecentMovieLink(
                    id = "rec_2",
                    title = "Jawan (Extended Cut 4K)",
                    url = "https://sites.google.com/view/manoranjanmovieszone/home",
                    quality = "4K UHD",
                    size = "2.4 GB",
                    timeLabel = "1 hour ago",
                    timestamp = System.currentTimeMillis() - 60 * 60 * 1000
                ),
                com.example.data.model.RecentMovieLink(
                    id = "rec_3",
                    title = "Pushpa 2: The Rule (Hindi)",
                    url = "https://sites.google.com/view/manoranjanmovieszone/home",
                    quality = "1080p WEB-DL",
                    size = "1.9 GB",
                    timeLabel = "Today",
                    timestamp = System.currentTimeMillis() - 4 * 60 * 60 * 1000
                ),
                com.example.data.model.RecentMovieLink(
                    id = "rec_4",
                    title = "Avengers: Endgame (Dual Audio)",
                    url = "https://sites.google.com/view/manoranjanmovieszone/home",
                    quality = "1080p Bluray",
                    size = "2.1 GB",
                    timeLabel = "Yesterday",
                    timestamp = System.currentTimeMillis() - 24 * 60 * 60 * 1000
                ),
                com.example.data.model.RecentMovieLink(
                    id = "rec_5",
                    title = "Stree 2: Sarkate Ka Aatank",
                    url = "https://sites.google.com/view/manoranjanmovieszone/home",
                    quality = "1080p FHD",
                    size = "1.7 GB",
                    timeLabel = "2 days ago",
                    timestamp = System.currentTimeMillis() - 48 * 60 * 60 * 1000
                )
            )
            _recentDownloads.value = initial
            saveRecentDownloads(initial)
        }
    }

    private fun saveRecentDownloads(items: List<com.example.data.model.RecentMovieLink>) {
        val set = items.map { "${it.id}|||${it.title}|||${it.url}|||${it.quality}|||${it.size}|||${it.timeLabel}|||${it.timestamp}" }.toSet()
        prefs.edit().putStringSet("saved_recent_downloads", set).apply()
    }

    fun getFilteredMovies(): List<MovieItem> {
        val query = _searchQuery.value.trim().lowercase()
        val cat = _selectedCategory.value

        return MovieRepository.sampleMovies.filter { movie ->
            val matchesCat = (cat == "All" || movie.category == cat)
            val matchesQuery = query.isEmpty() ||
                    movie.title.lowercase().contains(query) ||
                    movie.category.lowercase().contains(query) ||
                    movie.tags.any { it.lowercase().contains(query) } ||
                    movie.language.lowercase().contains(query)
            matchesCat && matchesQuery
        }
    }
}
