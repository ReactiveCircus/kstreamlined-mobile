package io.github.reactivecircus.kstreamlined.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import io.github.reactivecircus.kstreamlined.android.designsystem.component.NavigationIsland
import io.github.reactivecircus.kstreamlined.android.designsystem.component.NavigationIslandDivider
import io.github.reactivecircus.kstreamlined.android.designsystem.component.NavigationIslandItem
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.icon.Bookmarks
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.icon.Kotlin
import io.github.reactivecircus.kstreamlined.android.feature.home.HomeScreen

@AndroidEntryPoint
class KSActivity : ComponentActivity() {

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            KSTheme {
                val darkTheme = isSystemInDarkTheme()
                val navigationBarColor = KSTheme.colorScheme.background.toArgb()
                LaunchedEffect(darkTheme) {
                    window.navigationBarColor = navigationBarColor
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    var selectedNavItem by rememberSaveable { mutableStateOf(NavItemKey.Home) }

                    val pagerState = rememberPagerState(pageCount = { NavItemKey.entries.size })
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize(),
                        beyondBoundsPageCount = NavItemKey.entries.size,
                        userScrollEnabled = false,
                    ) {
                        when (it) {
                            NavItemKey.Home.ordinal -> {
                                HomeScreen()
                            }
                            NavItemKey.Saved.ordinal -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(KSTheme.colorScheme.background)
                                )
                            }
                        }
                    }

                    LaunchedEffect(selectedNavItem) {
                        pagerState.animateScrollToPage(selectedNavItem.ordinal)
                    }

                    NavigationIsland(
                        modifier = Modifier
                            .navigationBarsPadding()
                            .padding(8.dp)
                            .align(Alignment.BottomCenter),
                    ) {
                        NavigationIslandItem(
                            selected = selectedNavItem == NavItemKey.Home,
                            icon = KSIcons.Kotlin,
                            contentDescription = "Home",
                            onClick = {
                                selectedNavItem = NavItemKey.Home
                            },
                        )
                        NavigationIslandDivider()
                        NavigationIslandItem(
                            selected = selectedNavItem == NavItemKey.Saved,
                            icon = KSIcons.Bookmarks,
                            contentDescription = "Saved",
                            onClick = {
                                selectedNavItem = NavItemKey.Saved
                            },
                        )
                    }
                }
            }
        }
    }
}

enum class NavItemKey {
    Home,
    Saved,
}
