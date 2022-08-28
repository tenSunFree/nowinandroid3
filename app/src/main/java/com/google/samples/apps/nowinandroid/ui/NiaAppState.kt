package com.samples.nowinandroid_demo.ui

import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.tracing.trace
import com.samples.nowinandroid_demo.core.designsystem.icon.Icon.DrawableResourceIcon
import com.samples.nowinandroid_demo.core.designsystem.icon.Icon.ImageVectorIcon
import com.samples.nowinandroid_demo.core.designsystem.icon.NiaIcons
import com.samples.nowinandroid_demo.core.navigation.NiaNavigationDestination
import com.samples.nowinandroid_demo.core.ui.JankMetricDisposableEffect
import com.samples.nowinandroid_demo.feature.author.navigation.AuthorDestination
import com.samples.nowinandroid_demo.feature.bookmarks.R as bookmarksR
import com.samples.nowinandroid_demo.feature.bookmarks.navigation.BookmarksDestination
import com.samples.nowinandroid_demo.feature.foryou.R as forYouR
import com.samples.nowinandroid_demo.feature.foryou.navigation.ForYouDestination
import com.samples.nowinandroid_demo.feature.interests.R as interestsR
import com.samples.nowinandroid_demo.feature.topic.R as topicR
import com.samples.nowinandroid_demo.feature.author.R as authorR
import com.samples.nowinandroid_demo.feature.interests.navigation.InterestsDestination
import com.samples.nowinandroid_demo.feature.topic.navigation.TopicDestination
import com.samples.nowinandroid_demo.navigation.TopLevelDestination

@Composable
fun rememberNiaAppState(
    windowSizeClass: WindowSizeClass,
    navController: NavHostController = rememberNavController()
): NiaAppState {
    NavigationTrackingSideEffect(navController)
    return remember(navController, windowSizeClass) {
        NiaAppState(navController, windowSizeClass)
    }
}

@Stable
class NiaAppState(
    val navController: NavHostController,
    val windowSizeClass: WindowSizeClass
) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val shouldShowBottomBar: Boolean
        get() = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact ||
                windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact

    val shouldShowNavRail: Boolean
        get() = !shouldShowBottomBar

    /**
     * Top level destinations to be used in the BottomBar and NavRail
     */
    val topLevelDestinations: List<TopLevelDestination> = listOf(
        TopLevelDestination(
            route = BookmarksDestination.route,
            destination = BookmarksDestination.destination,
            selectedIcon = DrawableResourceIcon(NiaIcons.Bookmarks),
            unselectedIcon = DrawableResourceIcon(NiaIcons.BookmarksBorder),
            iconTextId = bookmarksR.string.saved
        ),
        TopLevelDestination(
            route = ForYouDestination.route,
            destination = ForYouDestination.destination,
            selectedIcon = DrawableResourceIcon(NiaIcons.Upcoming),
            unselectedIcon = DrawableResourceIcon(NiaIcons.UpcomingBorder),
            iconTextId = forYouR.string.for_you
        ),
        TopLevelDestination(
            route = InterestsDestination.route,
            destination = InterestsDestination.destination,
            selectedIcon = ImageVectorIcon(NiaIcons.Grid3x3),
            unselectedIcon = ImageVectorIcon(NiaIcons.Grid3x3),
            iconTextId = interestsR.string.interests
        ),
        TopLevelDestination(
            route = AuthorDestination.route,
            destination = AuthorDestination.destination,
            selectedIcon = ImageVectorIcon(NiaIcons.Grid3x3),
            unselectedIcon = ImageVectorIcon(NiaIcons.Grid3x3),
            iconTextId = authorR.string.author
        ),
        TopLevelDestination(
            route = TopicDestination.route,
            destination = TopicDestination.destination,
            selectedIcon = ImageVectorIcon(NiaIcons.Grid3x3),
            unselectedIcon = ImageVectorIcon(NiaIcons.Grid3x3),
            iconTextId = topicR.string.topic
        )
    )

    /**
     * UI logic for navigating to a particular destination in the app. The NavigationOptions to
     * navigate with are based on the type of destination, which could be a top level destination or
     * just a regular destination.
     *
     * Top level destinations have only one copy of the destination of the back stack, and save and
     * restore state whenever you navigate to and from it.
     * Regular destinations can have multiple copies in the back stack and state isn't saved nor
     * restored.
     *
     * @param destination: The [NiaNavigationDestination] the app needs to navigate to.
     * @param route: Optional route to navigate to in case the destination contains arguments.
     */
    fun navigate(destination: NiaNavigationDestination, route: String? = null) {
        trace("Navigation: $destination") {
            if (destination is TopLevelDestination) {
                navController.navigate(route ?: destination.route) {
                    // Pop up to the start destination of the graph to
                    // avoid building up a large stack of destinations
                    // on the back stack as users select items
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    // Avoid multiple copies of the same destination when
                    // reselecting the same item
                    launchSingleTop = true
                    // Restore state when reselecting a previously selected item
                    restoreState = true
                }
            } else {
                navController.navigate(route ?: destination.route)
            }
        }
    }

    fun onBackClick() {
        navController.popBackStack()
    }
}

/**
 * Stores information about navigation events to be used with JankStats
 */
@Composable
private fun NavigationTrackingSideEffect(navController: NavHostController) {
    JankMetricDisposableEffect(navController) { metricsHolder ->
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            metricsHolder.state?.addState("Navigation", destination.route.toString())
        }

        navController.addOnDestinationChangedListener(listener)

        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }
}
