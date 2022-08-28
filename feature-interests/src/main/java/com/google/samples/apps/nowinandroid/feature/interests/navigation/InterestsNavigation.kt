package com.samples.nowinandroid_demo.feature.interests.navigation

import android.util.Log
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.samples.nowinandroid_demo.core.navigation.NiaNavigationDestination
import com.samples.nowinandroid_demo.feature.interests.InterestsRoute

object InterestsDestination : NiaNavigationDestination {
    override val route = "interests_route"
    override val destination = "interests_destination"
}

fun NavGraphBuilder.interestsGraph() {
    navigation(
        route = InterestsDestination.route,
        startDestination = InterestsDestination.destination
    ) {
        Log.d("", "NavGraphBuilder.interestsGraph")
        composable(route = InterestsDestination.destination) {
            InterestsRoute()
        }
    }
}
