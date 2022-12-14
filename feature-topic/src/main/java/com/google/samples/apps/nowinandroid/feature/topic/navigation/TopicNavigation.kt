/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.samples.nowinandroid_demo.feature.topic.navigation

import android.net.Uri
import android.util.Log
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.samples.nowinandroid_demo.core.navigation.NiaNavigationDestination
import com.samples.nowinandroid_demo.feature.topic.TopicRoute

object TopicDestination : NiaNavigationDestination {
    const val topicIdArg = "topicId"
    override val route = "topic_route/{$topicIdArg}"
    override val destination = "topic_destination"

    /**
     * Creates destination route for an topicId that could include special characters
     */
    fun createNavigationRoute(topicIdArg: String): String {
        val encodedId = Uri.encode(topicIdArg)
        return "topic_route/$encodedId"
    }

    /**
     * Returns the topicId from a [NavBackStackEntry] after an topic destination navigation call
     */
    fun fromNavArgs(entry: NavBackStackEntry): String {
        val encodedId = entry.arguments?.getString(topicIdArg)!!
        return Uri.decode(encodedId)
    }
}

fun NavGraphBuilder.topicGraph() {
    Log.d("", "NavGraphBuilder.bookmarksGraph")
    composable(route = TopicDestination.route) {
        TopicRoute()
    }
}
