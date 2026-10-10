package com.example.data.downloader

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import com.example.data.model.DownloadStatus
import com.example.data.model.DownloadTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File

class DownloadController(private val context: Context) {

    private val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    private val scope = CoroutineScope(Dispatchers.IO)

    private val _tasks = MutableStateFlow<List<DownloadTask>>(emptyList())
    val tasks: StateFlow<List<DownloadTask>> = _tasks.asStateFlow()

    init {
        // Poller to update active download progress
        scope.launch {
            while (isActive) {
                updateTasksProgress()
                delay(1200)
            }
        }
    }

    fun startDownload(url: String, customFileName: String? = null): Long? {
        val cleanUrl = url.trim()
        if (cleanUrl.isEmpty() || (!cleanUrl.startsWith("http://") && !cleanUrl.startsWith("https://"))) {
            Toast.makeText(context, "Please enter a valid HTTP/HTTPS URL", Toast.LENGTH_SHORT).show()
            return null
        }

        try {
            val uri = Uri.parse(cleanUrl)
            val extractedFileName = uri.lastPathSegment?.takeIf { it.isNotEmpty() } ?: "manoranjan_video_${System.currentTimeMillis()}.mp4"
            val finalFileName = if (!customFileName.isNullOrBlank()) {
                if (customFileName.contains(".")) customFileName else "$customFileName.mp4"
            } else {
                extractedFileName
            }

            val request = DownloadManager.Request(uri).apply {
                setTitle("Manoranjan: $finalFileName")
                setDescription("Downloading movie file...")
                setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, finalFileName)
                setAllowedOverMetered(true)
                setAllowedOverRoaming(true)
            }

            val downloadId = downloadManager.enqueue(request)

            val newTask = DownloadTask(
                downloadId = downloadId,
                title = finalFileName,
                url = cleanUrl,
                fileName = finalFileName,
                totalBytes = -1,
                downloadedBytes = 0,
                status = DownloadStatus.RUNNING,
                progressPercent = 0
            )

            _tasks.value = listOf(newTask) + _tasks.value
            Toast.makeText(context, "Download started: $finalFileName", Toast.LENGTH_SHORT).show()
            return downloadId
        } catch (e: Exception) {
            Toast.makeText(context, "Download failed to start: ${e.message}", Toast.LENGTH_LONG).show()
            return null
        }
    }

    private fun updateTasksProgress() {
        val currentTasks = _tasks.value
        if (currentTasks.isEmpty()) return

        val updated = currentTasks.map { task ->
            if (task.status == DownloadStatus.SUCCESSFUL || task.status == DownloadStatus.FAILED) {
                task
            } else {
                val query = DownloadManager.Query().setFilterById(task.downloadId)
                try {
                    downloadManager.query(query)?.use { cursor ->
                        if (cursor.moveToFirst()) {
                            val statusIdx = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                            val downloadedIdx = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
                            val totalIdx = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)

                            val status = if (statusIdx != -1) cursor.getInt(statusIdx) else -1
                            val bytesDownloaded = if (downloadedIdx != -1) cursor.getLong(downloadedIdx) else 0L
                            val totalBytes = if (totalIdx != -1) cursor.getLong(totalIdx) else -1L

                            val downloadStatus = when (status) {
                                DownloadManager.STATUS_SUCCESSFUL -> DownloadStatus.SUCCESSFUL
                                DownloadManager.STATUS_FAILED -> DownloadStatus.FAILED
                                DownloadManager.STATUS_PAUSED -> DownloadStatus.PAUSED
                                DownloadManager.STATUS_PENDING -> DownloadStatus.PENDING
                                else -> DownloadStatus.RUNNING
                            }

                            val progress = if (totalBytes > 0) {
                                ((bytesDownloaded * 100) / totalBytes).toInt().coerceIn(0, 100)
                            } else {
                                if (downloadStatus == DownloadStatus.SUCCESSFUL) 100 else 0
                            }

                            task.copy(
                                totalBytes = totalBytes,
                                downloadedBytes = bytesDownloaded,
                                status = downloadStatus,
                                progressPercent = progress
                            )
                        } else {
                            task
                        }
                    } ?: task
                } catch (e: Exception) {
                    task
                }
            }
        }

        _tasks.value = updated
    }

    fun cancelTask(downloadId: Long) {
        try {
            downloadManager.remove(downloadId)
            _tasks.value = _tasks.value.filter { it.downloadId != downloadId }
            Toast.makeText(context, "Download cancelled", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            _tasks.value = _tasks.value.filter { it.downloadId != downloadId }
        }
    }

    fun openDownloadedFile(task: DownloadTask) {
        try {
            val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), task.fileName)
            if (file.exists()) {
                val uri = Uri.fromFile(file)
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(uri, "video/*")
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
                }
                context.startActivity(Intent.createChooser(intent, "Play movie with...").apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                })
            } else {
                // Fallback to DownloadManager open
                val uri = downloadManager.getUriForDownloadedFile(task.downloadId)
                if (uri != null) {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        setDataAndType(uri, "video/*")
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
                    }
                    context.startActivity(Intent.createChooser(intent, "Play movie with...").apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    })
                } else {
                    Toast.makeText(context, "File is located in Downloads folder: ${task.fileName}", Toast.LENGTH_LONG).show()
                }
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Unable to open directly: file saved in Downloads", Toast.LENGTH_SHORT).show()
        }
    }
}
