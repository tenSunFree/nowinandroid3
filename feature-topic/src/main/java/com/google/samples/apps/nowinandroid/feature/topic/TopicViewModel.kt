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

package com.samples.nowinandroid_demo.feature.topic

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samples.nowinandroid_demo.core.data.repository.AuthorsRepository
import com.samples.nowinandroid_demo.core.data.repository.NewsRepository
import com.samples.nowinandroid_demo.core.data.repository.UserDataRepository
import com.samples.nowinandroid_demo.core.model.data.Author
import com.samples.nowinandroid_demo.core.model.data.FollowableAuthor
import com.samples.nowinandroid_demo.core.model.data.NewsResource
import com.samples.nowinandroid_demo.core.result.Result
import com.samples.nowinandroid_demo.core.result.asResult
import com.samples.nowinandroid_demo.feature.topic.navigation.TopicDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class TopicViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userDataRepository: UserDataRepository,
    authorsRepository: AuthorsRepository,
    newsRepository: NewsRepository
) : ViewModel() {

    private val authorId: String = checkNotNull(
        savedStateHandle[TopicDestination.topicIdArg]
    )

    // Observe the followed authors, as they could change over time.
    private val followedAuthorIdsStream: Flow<Result<Set<String>>> =
        userDataRepository.userDataStream
            .map { it.followedAuthors }
            .asResult()

    // Observe author information
    private val author: Flow<Result<Author>> = authorsRepository.getAuthorStream(
        id = authorId
    ).asResult()

    // Observe the News for this author
    private val newsStream: Flow<Result<List<NewsResource>>> =
        newsRepository.getNewsResourcesStream(
            filterAuthorIds = setOf(element = authorId),
            filterTopicIds = emptySet()
        ).asResult()

    val uiState: StateFlow<AuthorScreenUiState> =
        combine(
            followedAuthorIdsStream,
            author,
            newsStream
        ) { followedAuthorsResult, authorResult, newsResult ->
            val author: AuthorUiState =
                if (authorResult is Result.Success && followedAuthorsResult is Result.Success) {
                    val followed = followedAuthorsResult.data.contains(authorId)
                    AuthorUiState.Success(
                        followableAuthor = FollowableAuthor(
                            author = authorResult.data,
                            isFollowed = followed
                        )
                    )
                } else if (
                    authorResult is Result.Loading || followedAuthorsResult is Result.Loading
                ) {
                    AuthorUiState.Loading
                } else {
                    AuthorUiState.Error
                }

            val news: NewsUiState = when (newsResult) {
                is Result.Success -> NewsUiState.Success(newsResult.data)
                is Result.Loading -> NewsUiState.Loading
                is Result.Error -> NewsUiState.Error
            }

            AuthorScreenUiState(author, news)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = AuthorScreenUiState(AuthorUiState.Loading, NewsUiState.Loading)
            )

    fun followAuthorToggle(followed: Boolean) {
        viewModelScope.launch {
            userDataRepository.toggleFollowedAuthorId(authorId, followed)
        }
    }
}

sealed interface AuthorUiState {
    data class Success(val followableAuthor: FollowableAuthor) : AuthorUiState
    object Error : AuthorUiState
    object Loading : AuthorUiState
}

sealed interface NewsUiState {
    data class Success(val news: List<NewsResource>) : NewsUiState
    object Error : NewsUiState
    object Loading : NewsUiState
}

data class AuthorScreenUiState(
    val authorState: AuthorUiState,
    val newsState: NewsUiState
)
