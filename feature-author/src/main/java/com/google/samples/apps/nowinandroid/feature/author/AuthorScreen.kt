/*
 * Copyright 2021 The Android Open Source Project
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

package com.samples.nowinandroid_demo.feature.author

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.samples.nowinandroid_demo.core.designsystem.component.*
import com.samples.nowinandroid_demo.core.designsystem.theme.NiaTheme
import com.samples.nowinandroid_demo.core.model.data.Author
import com.samples.nowinandroid_demo.core.model.data.FollowableAuthor
import com.samples.nowinandroid_demo.core.model.data.previewAuthors
import com.samples.nowinandroid_demo.core.model.data.previewNewsResources
import com.samples.nowinandroid_demo.core.ui.newsResourceCardItems

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun AuthorRoute(
    modifier: Modifier = Modifier,
    viewModel: AuthorViewModel = hiltViewModel(),
) {
    val uiState: AuthorScreenUiState by viewModel.uiState.collectAsStateWithLifecycle()

    AuthorScreen(
        authorState = uiState.authorState,
        newsState = uiState.newsState,
        modifier = modifier,
        onFollowClick = viewModel::followAuthorToggle,
    )
}

@VisibleForTesting
@Composable
internal fun AuthorScreen(
    authorState: AuthorUiState,
    newsState: NewsUiState,
    onFollowClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    NiaGradientBackground {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
            NiaTopAppBar(
                titleRes = R.string.author,
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    }
}

private fun LazyListScope.authorBody(
    author: Author,
    news: NewsUiState
) {
    item {
        AuthorHeader(author)
    }

    authorCards(news)
}

@Composable
private fun AuthorHeader(author: Author) {
    Column(
        modifier = Modifier.padding(horizontal = 24.dp)
    ) {
        AsyncImage(
            modifier = Modifier
                .padding(bottom = 12.dp)
                .size(216.dp)
                .align(Alignment.CenterHorizontally)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            model = author.imageUrl,
            contentDescription = "Author profile picture",
        )
        Text(author.name, style = MaterialTheme.typography.displayMedium)
        if (author.bio.isNotEmpty()) {
            Text(
                text = author.bio,
                modifier = Modifier.padding(top = 24.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

private fun LazyListScope.authorCards(news: NewsUiState) {
    when (news) {
        is NewsUiState.Success -> {
            newsResourceCardItems(
                items = news.news,
                newsResourceMapper = { it },
                isBookmarkedMapper = { /* TODO */ false },
                onToggleBookmark = { /* TODO */ },
                itemModifier = Modifier.padding(24.dp)
            )
        }
        is NewsUiState.Loading -> item {
            NiaLoadingWheel(contentDesc = "Loading news") // TODO
        }
        else -> item {
            Text("Error") // TODO
        }
    }
}

@Composable
private fun AuthorToolbar(
    uiState: FollowableAuthor,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onFollowClick: (Boolean) -> Unit = {},
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp)
    ) {
        IconButton(onClick = { onBackClick() }) {
            Icon(
                imageVector = Filled.ArrowBack,
                contentDescription = stringResource(id = R.string.back)
            )
        }
        val selected = uiState.isFollowed
        NiaFilterChip(
            modifier = Modifier.padding(horizontal = 16.dp),
            selected = selected,
            onSelectedChange = onFollowClick,
        ) {
            if (selected) {
                Text(stringResource(id = R.string.author_following))
            } else {
                Text(stringResource(id = R.string.author_not_following))
            }
        }
    }
}