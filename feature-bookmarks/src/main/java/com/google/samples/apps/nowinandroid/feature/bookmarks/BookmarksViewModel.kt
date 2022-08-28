package com.samples.nowinandroid_demo.feature.bookmarks

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samples.nowinandroid_demo.core.data.repository.NewsRepository
import com.samples.nowinandroid_demo.core.data.repository.UserDataRepository
import com.samples.nowinandroid_demo.core.model.data.NewsResource
import com.samples.nowinandroid_demo.core.model.data.SaveableNewsResource
import com.samples.nowinandroid_demo.core.ui.NewsFeedUiState
import com.samples.nowinandroid_demo.core.ui.NewsFeedUiState.Loading
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class BookmarksViewModel @Inject constructor(
    newsRepository: NewsRepository,
    private val userDataRepository: UserDataRepository
) : ViewModel() {
    private val savedNewsResourcesState: StateFlow<Set<String>> =
        userDataRepository.userDataStream
            .map { userData ->
                Log.d(
                    "", "BookmarksViewModel, savedNewsResourcesState, " +
                            "userData: $userData"
                )
                userData.bookmarkedNewsResources
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptySet()
            )

    val feedState: StateFlow<NewsFeedUiState> =
        newsRepository
            .getNewsResourcesStream()
            .mapToFeedState(savedNewsResourcesState)
            .map { it }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = Loading
            )

    init {
        viewModelScope.launch {
            userDataRepository.userDataStream.collect { data ->
                if (data.bookmarkedNewsResources.isEmpty()) {
                    updateNewsResourceSaved("59", true)
                    updateNewsResourceSaved("71", true)
                    updateNewsResourceSaved("80", true)
                    updateNewsResourceSaved("90", true)
                    updateNewsResourceSaved("100", true)
                    updateNewsResourceSaved("105", true)
                    updateNewsResourceSaved("110", true)
                }
            }
        }
    }

    private fun Flow<List<NewsResource>>.mapToFeedState(
        savedNewsResourcesState: Flow<Set<String>>
    ): Flow<NewsFeedUiState> {
        return filterNot {
            it.isEmpty()
        }
            .combine(savedNewsResourcesState) { newsResources, savedNewsResources ->
                newsResources
                    .filter { newsResource -> savedNewsResources.contains(newsResource.id) }
                    .map { SaveableNewsResource(it, true) }
            }
            .map<List<SaveableNewsResource>, NewsFeedUiState>(NewsFeedUiState::Success)
            .onStart { emit(Loading) }
    }

    fun updateNewsResourceSaved(newsResourceId: String, isChecked: Boolean) {
        viewModelScope.launch {
            userDataRepository.updateNewsResourceBookmark(newsResourceId, isChecked)
        }
    }


    fun removeFromSavedResources(newsResourceId: String) {
        viewModelScope.launch {
            userDataRepository.updateNewsResourceBookmark(newsResourceId, false)
        }
    }
}
