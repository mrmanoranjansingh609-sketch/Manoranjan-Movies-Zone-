package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppDestination
import com.example.ui.theme.ManoranjanBorder
import com.example.ui.theme.ManoranjanDarkBg
import com.example.ui.theme.ManoranjanSurface
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonPurpleDark
import com.example.ui.theme.NeonPurplePrimary
import com.example.ui.theme.NeonPurpleSecondary

@Composable
fun ManoranjanDrawerContent(
    currentDestination: AppDestination,
    onNavigate: (AppDestination) -> Unit,
    onOpenPortal: (url: String, title: String) -> Unit,
    onOpenAbout: () -> Unit,
    onCloseDrawer: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Surface(
        modifier = Modifier
            .width(300.dp)
            .fillMaxHeight(),
        color = ManoranjanSurface,
        tonalElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .verticalScroll(scrollState)
                .padding(20.dp)
        ) {
            // Close Button Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = onCloseDrawer,
                    modifier = Modifier.testTag("drawer_close_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Menu",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            // Brand Header with Gradient
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(NeonPurpleSecondary, NeonPurpleDark)
                        )
                    )
                    .padding(18.dp)
            ) {
                Column {
                    Text(
                        text = "MANORANJAN",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp,
                        color = Color.White
                    )
                    Text(
                        text = "Movie Downloader & Hub",
                        fontSize = 12.sp,
                        color = NeonCyan,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Navigation Items matching the user's web drawer
            DrawerNavItem(
                icon = Icons.Default.Home,
                label = "Home Page",
                isSelected = currentDestination == AppDestination.HOME,
                onClick = {
                    onNavigate(AppDestination.HOME)
                    onCloseDrawer()
                }
            )

            DrawerNavItem(
                icon = Icons.Default.Movie,
                label = "Movies Zone",
                subtitle = "Browse online catalog",
                isSelected = currentDestination == AppDestination.PORTAL_MOVIES,
                onClick = {
                    onOpenPortal(
                        "https://sites.google.com/view/manoranjanmovieszone/home",
                        "Manoranjan Movies Zone"
                    )
                    onCloseDrawer()
                }
            )

            DrawerNavItem(
                icon = Icons.AutoMirrored.Filled.HelpOutline,
                label = "XYZ Help",
                subtitle = "Download tutorials & servers",
                isSelected = currentDestination == AppDestination.PORTAL_HELP,
                onClick = {
                    onOpenPortal(
                        "https://sites.google.com/view/manoranjan-xyz-helps/home",
                        "Manoranjan XYZ Help"
                    )
                    onCloseDrawer()
                }
            )

            DrawerNavItem(
                icon = Icons.Default.Download,
                label = "Download Manager",
                subtitle = "Manage movie files & links",
                isSelected = currentDestination == AppDestination.DOWNLOADER,
                onClick = {
                    onNavigate(AppDestination.DOWNLOADER)
                    onCloseDrawer()
                }
            )

            DrawerNavItem(
                icon = Icons.Default.Bookmark,
                label = "Saved Movies",
                subtitle = "Favorites & bookmarks",
                isSelected = currentDestination == AppDestination.BOOKMARKS,
                onClick = {
                    onNavigate(AppDestination.BOOKMARKS)
                    onCloseDrawer()
                }
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = ManoranjanBorder
            )

            DrawerNavItem(
                icon = Icons.Default.PhotoCamera,
                label = "Instagram",
                subtitle = "@mr__snatani__g",
                tint = NeonPink,
                onClick = {
                    try {
                        val uri = Uri.parse("https://www.instagram.com/mr__snatani__g/")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        context.startActivity(intent)
                    } catch (_: Exception) {}
                    onCloseDrawer()
                }
            )

            DrawerNavItem(
                icon = Icons.Default.Info,
                label = "About App",
                subtitle = "Version, credits & legal",
                onClick = {
                    onOpenAbout()
                    onCloseDrawer()
                }
            )

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(20.dp))

            // Footer
            Text(
                text = "© Manoranjan • All Rights Reserved",
                fontSize = 11.sp,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
fun DrawerNavItem(
    icon: ImageVector,
    label: String,
    subtitle: String? = null,
    isSelected: Boolean = false,
    tint: Color = NeonPurplePrimary,
    onClick: () -> Unit
) {
    val bgModifier = if (isSelected) {
        Modifier.background(
            Brush.horizontalGradient(
                listOf(NeonPurpleSecondary.copy(alpha = 0.35f), Color.Transparent)
            ),
            shape = RoundedCornerShape(12.dp)
        )
    } else {
        Modifier
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(bgModifier)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(if (isSelected) NeonPurpleSecondary else ManoranjanBorder.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) Color.White else tint,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                color = if (isSelected) NeonCyan else Color.White,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 15.sp
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    color = Color.Gray,
                    fontSize = 11.sp
                )
            }
        }
    }
}
