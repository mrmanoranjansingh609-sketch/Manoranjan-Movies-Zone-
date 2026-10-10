package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ManoranjanBorder
import com.example.ui.theme.ManoranjanDarkBg
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonPurplePrimary

@Composable
fun ManoranjanTopBar(
    activeDownloadsCount: Int,
    onMenuClick: () -> Unit,
    onDownloadsClick: () -> Unit,
    onAboutClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(ManoranjanDarkBg.copy(alpha = 0.95f))
            .statusBarsPadding()
            .height(64.dp)
            .drawBehind {
                drawLine(
                    color = ManoranjanBorder,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 1.dp.toPx()
                )
            }
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Hamburger menu button matching HTML: <div class="menu" onclick="openMenu()">☰</div>
            IconButton(
                onClick = onMenuClick,
                modifier = Modifier
                    .size(48.dp)
                    .testTag("menu_hamburger_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Open Menu",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Logo matching HTML: <div class="logo">MANORANJAN</div>
            Text(
                text = "MANORANJAN",
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.5.sp,
                color = Color.White,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.testTag("app_logo_title")
            )

            Spacer(modifier = Modifier.weight(1f))

            // Active Downloads Badge Button
            IconButton(
                onClick = onDownloadsClick,
                modifier = Modifier
                    .size(48.dp)
                    .testTag("active_downloads_button")
            ) {
                BadgedBox(
                    badge = {
                        if (activeDownloadsCount > 0) {
                            Badge(
                                containerColor = NeonPink,
                                contentColor = Color.White
                            ) {
                                Text(
                                    text = activeDownloadsCount.toString(),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = "Downloads",
                        tint = if (activeDownloadsCount > 0) NeonCyan else Color.White.copy(alpha = 0.85f),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // About Button
            IconButton(
                onClick = onAboutClick,
                modifier = Modifier
                    .size(48.dp)
                    .testTag("about_app_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "About",
                    tint = Color.White.copy(alpha = 0.85f),
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}
