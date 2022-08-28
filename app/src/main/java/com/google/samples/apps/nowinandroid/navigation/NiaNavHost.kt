package com.samples.nowinandroid_demo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.samples.nowinandroid_demo.feature.author.navigation.authorGraph
import com.samples.nowinandroid_demo.feature.bookmarks.navigation.BookmarksDestination
import com.samples.nowinandroid_demo.feature.bookmarks.navigation.bookmarksGraph
import com.samples.nowinandroid_demo.feature.foryou.navigation.forYouGraph
import com.samples.nowinandroid_demo.feature.interests.navigation.interestsGraph
import com.samples.nowinandroid_demo.feature.topic.navigation.topicGraph

@Composable
fun NiaNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = BookmarksDestination.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        bookmarksGraph()
        forYouGraph()
        interestsGraph()
        authorGraph()
        topicGraph()
    }
}
