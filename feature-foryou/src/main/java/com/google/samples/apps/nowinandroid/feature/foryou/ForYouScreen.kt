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

package com.samples.nowinandroid_demo.feature.foryou

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumedWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.trace
import androidx.core.view.doOnPreDraw
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.samples.nowinandroid_demo.core.designsystem.component.NiaFilledButton
import com.samples.nowinandroid_demo.core.designsystem.component.NiaGradientBackground
import com.samples.nowinandroid_demo.core.designsystem.component.NiaLoadingWheel
import com.samples.nowinandroid_demo.core.designsystem.component.NiaToggleButton
import com.samples.nowinandroid_demo.core.designsystem.component.NiaTopAppBar
import com.samples.nowinandroid_demo.core.designsystem.icon.NiaIcons
import com.samples.nowinandroid_demo.core.designsystem.theme.NiaTheme
import com.samples.nowinandroid_demo.core.model.data.FollowableAuthor
import com.samples.nowinandroid_demo.core.model.data.FollowableTopic
import com.samples.nowinandroid_demo.core.model.data.SaveableNewsResource
import com.samples.nowinandroid_demo.core.model.data.previewAuthors
import com.samples.nowinandroid_demo.core.model.data.previewNewsResources
import com.samples.nowinandroid_demo.core.model.data.previewTopics
import com.samples.nowinandroid_demo.core.ui.NewsFeed
import com.samples.nowinandroid_demo.core.ui.NewsFeedUiState
import com.samples.nowinandroid_demo.core.ui.TrackScrollJank
import com.samples.nowinandroid_demo.feature.foryou.R.string
import kotlin.math.floor

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun ForYouRoute(
    modifier: Modifier = Modifier,
    viewModel: ForYouViewModel = hiltViewModel()
) {
    Log.d("", "NavGraphBuilder.forYouGraph")
    val interestsSelectionState by viewModel.interestsSelectionState.collectAsStateWithLifecycle()
    val feedState by viewModel.feedState.collectAsStateWithLifecycle()
    ForYouScreen(
        modifier = modifier,
        interestsSelectionState = interestsSelectionState,
        feedState = feedState,
        onTopicCheckedChanged = viewModel::updateTopicSelection,
        onAuthorCheckedChanged = viewModel::updateAuthorSelection,
        saveFollowedTopics = viewModel::saveFollowedInterests,
        onNewsResourcesCheckedChanged = viewModel::updateNewsResourceSaved
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ForYouScreen(
    interestsSelectionState: ForYouInterestsSelectionUiState,
    feedState: NewsFeedUiState,
    onTopicCheckedChanged: (String, Boolean) -> Unit,
    onAuthorCheckedChanged: (String, Boolean) -> Unit,
    saveFollowedTopics: () -> Unit,
    onNewsResourcesCheckedChanged: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    NiaGradientBackground {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
            NiaTopAppBar(
                titleRes = string.for_you,
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    }
}

/**
 * An extension on [LazyListScope] defining the interests selection portion of the for you screen.
 * Depending on the [interestsSelectionState], this might emit no items.
 *
 * @param showLoadingUIIfLoading if true, show a visual indication of loading if the
 * [interestsSelectionState] is loading. This is controllable to permit du-duplicating loading
 * states.
 */
private fun LazyListScope.InterestsSelection(
    interestsSelectionState: ForYouInterestsSelectionUiState,
    showLoadingUIIfLoading: Boolean,
    onAuthorCheckedChanged: (String, Boolean) -> Unit,
    onTopicCheckedChanged: (String, Boolean) -> Unit,
    saveFollowedTopics: () -> Unit
) {
    when (interestsSelectionState) {
        ForYouInterestsSelectionUiState.Loading -> {
            if (showLoadingUIIfLoading) {
                item {
                    NiaLoadingWheel(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize()
                            .testTag("forYou:loading"),
                        contentDesc = stringResource(id = R.string.for_you_loading),
                    )
                }
            }
        }
        ForYouInterestsSelectionUiState.NoInterestsSelection -> Unit
        is ForYouInterestsSelectionUiState.WithInterestsSelection -> {
            item {
                Text(
                    text = stringResource(R.string.onboarding_guidance_title),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            item {
                Text(
                    text = stringResource(R.string.onboarding_guidance_subtitle),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            item {
                AuthorsCarousel(
                    authors = interestsSelectionState.authors,
                    onAuthorClick = onAuthorCheckedChanged,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
            }
            item {
                TopicSelection(
                    interestsSelectionState,
                    onTopicCheckedChanged,
                    Modifier.padding(bottom = 8.dp)
                )
            }
            item {
                // Done button
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    NiaFilledButton(
                        onClick = saveFollowedTopics,
                        enabled = interestsSelectionState.canSaveInterests,
                        modifier = Modifier
                            .padding(horizontal = 40.dp)
                            .width(364.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.done)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TopicSelection(
    interestsSelectionState: ForYouInterestsSelectionUiState.WithInterestsSelection,
    onTopicCheckedChanged: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) = trace("TopicSelection") {
    val lazyGridState = rememberLazyGridState()
    TrackScrollJank(scrollableState = lazyGridState, stateName = "forYou:TopicSelection")

    LazyHorizontalGrid(
        state = lazyGridState,
        rows = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(24.dp),
        modifier = modifier
            // LazyHorizontalGrid has to be constrained in height.
            // However, we can't set a fixed height because the horizontal grid contains
            // vertical text that can be rescaled.
            // When the fontScale is at most 1, we know that the horizontal grid will be at most
            // 240dp tall, so this is an upper bound for when the font scale is at most 1.
            // When the fontScale is greater than 1, the height required by the text inside the
            // horizontal grid will increase by at most the same factor, so 240sp is a valid
            // upper bound for how much space we need in that case.
            // The maximum of these two bounds is therefore a valid upper bound in all cases.
            .heightIn(max = max(240.dp, with(LocalDensity.current) { 240.sp.toDp() }))
            .fillMaxWidth()
    ) {
        items(interestsSelectionState.topics) {
            SingleTopicButton(
                name = it.topic.name,
                topicId = it.topic.id,
                imageUrl = it.topic.imageUrl,
                isSelected = it.isFollowed,
                onClick = onTopicCheckedChanged
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SingleTopicButton(
    name: String,
    topicId: String,
    imageUrl: String,
    isSelected: Boolean,
    onClick: (String, Boolean) -> Unit
) = trace("SingleTopicButton") {
    Surface(
        modifier = Modifier
            .width(312.dp)
            .heightIn(min = 56.dp),
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        color = MaterialTheme.colorScheme.surface,
        selected = isSelected,
        onClick = {
            onClick(topicId, !isSelected)
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 12.dp, end = 8.dp)
        ) {
            TopicIcon(
                imageUrl = imageUrl
            )
            Text(
                text = name,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .weight(1f),
                color = MaterialTheme.colorScheme.onSurface
            )
            NiaToggleButton(
                checked = isSelected,
                onCheckedChange = { checked -> onClick(topicId, checked) },
                icon = {
                    Icon(
                        imageVector = NiaIcons.Add,
                        contentDescription = name
                    )
                },
                checkedIcon = {
                    Icon(
                        imageVector = NiaIcons.Check,
                        contentDescription = name
                    )
                }
            )
        }
    }
}

@Composable
fun TopicIcon(
    modifier: Modifier = Modifier,
    imageUrl: String
) {
    AsyncImage(
        // TODO b/228077205, show loading image visual instead of static placeholder
        placeholder = painterResource(R.drawable.ic_icon_placeholder),
        model = imageUrl,
        contentDescription = null, // decorative
        modifier = modifier
            .padding(10.dp)
            .size(32.dp)
    )
}