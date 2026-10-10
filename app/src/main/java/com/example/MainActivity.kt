package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.AppDestination
import com.example.ui.MainViewModel
import com.example.ui.components.ManoranjanDrawerContent
import com.example.ui.components.ManoranjanTopBar
import com.example.ui.screens.AboutDialog
import com.example.ui.screens.BookmarksScreen
import com.example.ui.screens.DownloaderScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.WebPortalScreen
import com.example.ui.theme.ManoranjanBorder
import com.example.ui.theme.ManoranjanDarkBg
import com.example.ui.theme.ManoranjanSurface
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonPurpleSecondary
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppRoot()
            }
        }
    }
}

@Composable
fun MainAppRoot(viewModel: MainViewModel = viewModel()) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val currentDestination by viewModel.currentDestination.collectAsState()
    val isAboutVisible by viewModel.isAboutDialogVisible.collectAsState()
    val activeTasks by viewModel.downloadTasks.collectAsState()
    val portalUrl by viewModel.portalUrl.collectAsState()
    val portalTitle by viewModel.portalTitle.collectAsState()

    val runningDownloadsCount = activeTasks.count {
        it.status == com.example.data.model.DownloadStatus.RUNNING ||
                it.status == com.example.data.model.DownloadStatus.PENDING
    }

    // Handle back button for drawer or returning to Home screen
    BackHandler(enabled = drawerState.isOpen || currentDestination != AppDestination.HOME) {
        if (drawerState.isOpen) {
            scope.launch { drawerState.close() }
        } else if (currentDestination != AppDestination.HOME) {
            viewModel.navigateTo(AppDestination.HOME)
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = ManoranjanDarkBg
            ) {
                ManoranjanDrawerContent(
                    currentDestination = currentDestination,
                    onNavigate = { dest -> viewModel.navigateTo(dest) },
                    onOpenPortal = { url, title -> viewModel.openPortal(url, title) },
                    onOpenAbout = { viewModel.setAboutDialogVisible(true) },
                    onCloseDrawer = { scope.launch { drawerState.close() } }
                )
            }
        }
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(ManoranjanDarkBg),
            topBar = {
                ManoranjanTopBar(
                    activeDownloadsCount = runningDownloadsCount,
                    onMenuClick = {
                        scope.launch {
                            if (drawerState.isClosed) drawerState.open() else drawerState.close()
                        }
                    },
                    onDownloadsClick = {
                        viewModel.navigateTo(AppDestination.DOWNLOADER)
                    },
                    onAboutClick = {
                        viewModel.setAboutDialogVisible(true)
                    }
                )
            },
            bottomBar = {
                NavigationBar(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .drawBehind {
                            drawLine(
                                color = ManoranjanBorder,
                                start = Offset(0f, 0f),
                                end = Offset(size.width, 0f),
                                strokeWidth = 1.dp.toPx()
                            )
                        }
                        .testTag("main_bottom_nav"),
                    containerColor = ManoranjanSurface,
                    tonalElevation = 8.dp
                ) {
                    // Home Tab
                    NavigationBarItem(
                        selected = currentDestination == AppDestination.HOME,
                        onClick = { viewModel.navigateTo(AppDestination.HOME) },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "Home",
                                modifier = Modifier.size(22.dp)
                            )
                        },
                        label = {
                            Text(
                                "Home",
                                fontSize = 11.sp,
                                fontWeight = if (currentDestination == AppDestination.HOME) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = NeonCyan,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = NeonPurpleSecondary
                        )
                    )

                    // Movies Zone Tab
                    NavigationBarItem(
                        selected = currentDestination == AppDestination.PORTAL_MOVIES,
                        onClick = {
                            viewModel.openPortal(
                                "https://sites.google.com/view/manoranjanmovieszone/home",
                                "Manoranjan Movies Zone"
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Movie,
                                contentDescription = "Movies Zone",
                                modifier = Modifier.size(22.dp)
                            )
                        },
                        label = {
                            Text(
                                "Movies Zone",
                                fontSize = 11.sp,
                                fontWeight = if (currentDestination == AppDestination.PORTAL_MOVIES) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = NeonCyan,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = NeonPurpleSecondary
                        )
                    )

                    // XYZ Help Tab
                    NavigationBarItem(
                        selected = currentDestination == AppDestination.PORTAL_HELP,
                        onClick = {
                            viewModel.openPortal(
                                "https://sites.google.com/view/manoranjan-xyz-helps/home",
                                "Manoranjan XYZ Help"
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.HelpOutline,
                                contentDescription = "XYZ Help",
                                modifier = Modifier.size(22.dp)
                            )
                        },
                        label = {
                            Text(
                                "XYZ Help",
                                fontSize = 11.sp,
                                fontWeight = if (currentDestination == AppDestination.PORTAL_HELP) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = NeonCyan,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = NeonPurpleSecondary
                        )
                    )

                    // Downloader Tab
                    NavigationBarItem(
                        selected = currentDestination == AppDestination.DOWNLOADER,
                        onClick = { viewModel.navigateTo(AppDestination.DOWNLOADER) },
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (runningDownloadsCount > 0) {
                                        Badge(
                                            containerColor = NeonPink,
                                            contentColor = Color.White
                                        ) {
                                            Text(
                                                text = runningDownloadsCount.toString(),
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Download,
                                    contentDescription = "Downloads",
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        },
                        label = {
                            Text(
                                "Downloads",
                                fontSize = 11.sp,
                                fontWeight = if (currentDestination == AppDestination.DOWNLOADER) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = NeonCyan,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = NeonPurpleSecondary
                        )
                    )

                    // Bookmarks Tab
                    NavigationBarItem(
                        selected = currentDestination == AppDestination.BOOKMARKS,
                        onClick = { viewModel.navigateTo(AppDestination.BOOKMARKS) },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Bookmark,
                                contentDescription = "Saved",
                                modifier = Modifier.size(22.dp)
                            )
                        },
                        label = {
                            Text(
                                "Saved",
                                fontSize = 11.sp,
                                fontWeight = if (currentDestination == AppDestination.BOOKMARKS) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = NeonCyan,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = NeonPurpleSecondary
                        )
                    )
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Crossfade(targetState = currentDestination, label = "ScreenTransition") { destination ->
                    when (destination) {
                        AppDestination.HOME -> {
                            HomeScreen(
                                viewModel = viewModel,
                                onOpenPortal = { url, title -> viewModel.openPortal(url, title) },
                                onNavigate = { dest -> viewModel.navigateTo(dest) }
                            )
                        }

                        AppDestination.PORTAL_MOVIES, AppDestination.PORTAL_HELP -> {
                            WebPortalScreen(
                                url = portalUrl,
                                title = portalTitle,
                                viewModel = viewModel,
                                onBackToHome = { viewModel.navigateTo(AppDestination.HOME) }
                            )
                        }

                        AppDestination.DOWNLOADER -> {
                            DownloaderScreen(viewModel = viewModel)
                        }

                        AppDestination.BOOKMARKS -> {
                            BookmarksScreen(
                                viewModel = viewModel,
                                onOpenPortal = { url, title -> viewModel.openPortal(url, title) },
                                onNavigate = { dest -> viewModel.navigateTo(dest) }
                            )
                        }
                    }
                }
            }
        }
    }

    if (isAboutVisible) {
        AboutDialog(onDismiss = { viewModel.setAboutDialogVisible(false) })
    }
}
