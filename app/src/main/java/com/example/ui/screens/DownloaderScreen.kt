package com.example.ui.screens

import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DownloadStatus
import com.example.data.model.DownloadTask
import com.example.ui.MainViewModel
import com.example.ui.theme.ManoranjanBorder
import com.example.ui.theme.ManoranjanCardBg
import com.example.ui.theme.ManoranjanDarkBg
import com.example.ui.theme.ManoranjanSurface
import com.example.ui.theme.ManoranjanSurfaceVariant
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonPurpleDark
import com.example.ui.theme.NeonPurplePrimary
import com.example.ui.theme.NeonPurpleSecondary
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary

@Composable
fun DownloaderScreen(viewModel: MainViewModel) {
    val context = LocalContext.current
    val tasks by viewModel.downloadTasks.collectAsState()
    var inputUrl by remember { mutableStateOf("") }
    var customFileName by remember { mutableStateOf("") }
    var isAdvancedOptionsOpen by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ManoranjanDarkBg)
            .testTag("downloader_screen_content"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Top Header
        item {
            Column {
                Text(
                    text = "Download Manager",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "High-speed multi-threaded downloader with resume support",
                    fontSize = 12.sp,
                    color = TextMuted
                )
            }
        }

        // Fast Downloader Input Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, ManoranjanBorder, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = ManoranjanCardBg),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Grab & Download Movie Link",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = inputUrl,
                        onValueChange = { inputUrl = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("downloader_url_input"),
                        placeholder = {
                            Text("Paste direct video/movie URL here...", color = TextMuted, fontSize = 13.sp)
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    if (clipboard.hasPrimaryClip()) {
                                        val item = clipboard.primaryClip?.getItemAt(0)
                                        val text = item?.text?.toString() ?: ""
                                        if (text.isNotEmpty()) {
                                            inputUrl = text
                                            Toast.makeText(context, "Pasted link!", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentPaste,
                                    contentDescription = "Paste",
                                    tint = NeonCyan
                                )
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = NeonPurplePrimary,
                            unfocusedBorderColor = ManoranjanBorder,
                            focusedContainerColor = ManoranjanSurface,
                            unfocusedContainerColor = ManoranjanSurface
                        ),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (inputUrl.isNotBlank()) {
                                    viewModel.startDownload(inputUrl, customFileName.ifBlank { null })
                                    inputUrl = ""
                                    customFileName = ""
                                }
                            }
                        )
                    )

                    AnimatedVisibility(visible = isAdvancedOptionsOpen) {
                        Column(modifier = Modifier.padding(top = 10.dp)) {
                            OutlinedTextField(
                                value = customFileName,
                                onValueChange = { customFileName = it },
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = {
                                    Text("Custom file name (e.g., Movie_1080p.mp4)", color = TextMuted, fontSize = 13.sp)
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = NeonPurplePrimary,
                                    unfocusedBorderColor = ManoranjanBorder,
                                    focusedContainerColor = ManoranjanSurface,
                                    unfocusedContainerColor = ManoranjanSurface
                                ),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isAdvancedOptionsOpen) "Hide options" else "File name options...",
                            fontSize = 12.sp,
                            color = NeonPurplePrimary,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(start = 4.dp)
                        )

                        ElevatedButton(
                            onClick = {
                                if (inputUrl.isNotBlank()) {
                                    viewModel.startDownload(inputUrl, customFileName.ifBlank { null })
                                    inputUrl = ""
                                    customFileName = ""
                                } else {
                                    Toast.makeText(context, "Please paste a link first", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier
                                .height(44.dp)
                                .testTag("btn_trigger_download"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.elevatedButtonColors(
                                containerColor = NeonPurpleSecondary
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Start Download",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }

        // Storage Location Info Box
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(ManoranjanSurfaceVariant.copy(alpha = 0.5f))
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Folder,
                    contentDescription = null,
                    tint = NeonCyan,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Downloaded Location",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Internal Storage > Downloads",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }
            }
        }

        // Tasks Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Downloads (${tasks.size})",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                if (tasks.isNotEmpty()) {
                    Text(
                        text = "Background Auto-Sync",
                        fontSize = 11.sp,
                        color = NeonCyan
                    )
                }
            }
        }

        // Empty State or List
        if (tasks.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    colors = CardDefaults.cardColors(containerColor = ManoranjanSurface),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(ManoranjanBorder),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = null,
                                tint = NeonPurplePrimary,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "No Active Downloads",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Downloads started from the Movies Zone portal or direct URLs will appear here with live speed and progress.",
                            fontSize = 12.sp,
                            color = TextMuted,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(tasks, key = { it.downloadId }) { task ->
                DownloadTaskItem(
                    task = task,
                    onPlay = { viewModel.openDownloadedFile(task) },
                    onCancel = { viewModel.cancelDownload(task.downloadId) }
                )
            }
        }
    }
}

@Composable
fun DownloadTaskItem(
    task: DownloadTask,
    onPlay: () -> Unit,
    onCancel: () -> Unit
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, ManoranjanBorder, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = ManoranjanCardBg),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            when (task.status) {
                                DownloadStatus.SUCCESSFUL -> Color(0xFF00C853)
                                DownloadStatus.FAILED -> NeonPink
                                DownloadStatus.PAUSED -> NeonAmber
                                else -> NeonPurpleSecondary
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (task.status) {
                            DownloadStatus.SUCCESSFUL -> Icons.Default.CheckCircle
                            DownloadStatus.FAILED -> Icons.Default.Error
                            else -> Icons.Default.Movie
                        },
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = task.title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = when (task.status) {
                                DownloadStatus.SUCCESSFUL -> "Completed"
                                DownloadStatus.FAILED -> "Failed / Server Error"
                                DownloadStatus.PAUSED -> "Paused"
                                DownloadStatus.PENDING -> "Connecting..."
                                DownloadStatus.RUNNING -> "Downloading ${task.progressPercent}%"
                            },
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = when (task.status) {
                                DownloadStatus.SUCCESSFUL -> Color(0xFF69F0AE)
                                DownloadStatus.FAILED -> NeonPink
                                DownloadStatus.PAUSED -> NeonAmber
                                else -> NeonCyan
                            }
                        )

                        if (task.totalBytes > 0) {
                            val totalMb = task.totalBytes / (1024 * 1024)
                            val downloadedMb = task.downloadedBytes / (1024 * 1024)
                            Text(
                                text = " • ${downloadedMb}MB / ${totalMb}MB",
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }
                    }
                }

                IconButton(onClick = onCancel) {
                    Icon(
                        imageVector = if (task.status == DownloadStatus.SUCCESSFUL) Icons.Default.Delete else Icons.Default.Cancel,
                        contentDescription = "Remove",
                        tint = TextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            if (task.status == DownloadStatus.RUNNING || task.status == DownloadStatus.PENDING) {
                Spacer(modifier = Modifier.height(10.dp))
                LinearProgressIndicator(
                    progress = { (task.progressPercent / 100f).coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = NeonPurplePrimary,
                    trackColor = ManoranjanBorder
                )
            }

            if (task.status == DownloadStatus.SUCCESSFUL) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ElevatedButton(
                        onClick = onPlay,
                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = Color(0xFF00C853)
                        ),
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Play Movie",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    ElevatedButton(
                        onClick = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, task.title)
                                putExtra(Intent.EXTRA_TEXT, task.url)
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share link"))
                        },
                        modifier = Modifier.height(38.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = ManoranjanSurfaceVariant
                        ),
                        contentPadding = PaddingValues(horizontal = 12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            tint = NeonCyan,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Share",
                            fontSize = 12.sp,
                            color = NeonCyan
                        )
                    }
                }
            }
        }
    }
}
