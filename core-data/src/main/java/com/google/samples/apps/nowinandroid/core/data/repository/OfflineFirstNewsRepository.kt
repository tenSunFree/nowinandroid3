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

package com.samples.nowinandroid_demo.core.data.repository

import android.util.Log
import com.samples.nowinandroid_demo.core.data.Synchronizer
import com.samples.nowinandroid_demo.core.data.changeListSync
import com.samples.nowinandroid_demo.core.data.model.asEntity
import com.samples.nowinandroid_demo.core.data.model.authorCrossReferences
import com.samples.nowinandroid_demo.core.data.model.authorEntityShells
import com.samples.nowinandroid_demo.core.data.model.episodeEntityShell
import com.samples.nowinandroid_demo.core.data.model.topicCrossReferences
import com.samples.nowinandroid_demo.core.data.model.topicEntityShells
import com.samples.nowinandroid_demo.core.database.dao.AuthorDao
import com.samples.nowinandroid_demo.core.database.dao.EpisodeDao
import com.samples.nowinandroid_demo.core.database.dao.NewsResourceDao
import com.samples.nowinandroid_demo.core.database.dao.TopicDao
import com.samples.nowinandroid_demo.core.database.model.AuthorEntity
import com.samples.nowinandroid_demo.core.database.model.EpisodeEntity
import com.samples.nowinandroid_demo.core.database.model.PopulatedNewsResource
import com.samples.nowinandroid_demo.core.database.model.TopicEntity
import com.samples.nowinandroid_demo.core.database.model.asExternalModel
import com.samples.nowinandroid_demo.core.datastore.ChangeListVersions
import com.samples.nowinandroid_demo.core.model.data.NewsResource
import com.samples.nowinandroid_demo.core.network.NiaNetworkDataSource
import com.samples.nowinandroid_demo.core.network.model.NetworkNewsResource
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Disk storage backed implementation of the [NewsRepository].
 * Reads are exclusively from local storage to support offline access.
 */
class OfflineFirstNewsRepository @Inject constructor(
    private val newsResourceDao: NewsResourceDao,
    private val episodeDao: EpisodeDao,
    private val authorDao: AuthorDao,
    private val topicDao: TopicDao,
    private val network: NiaNetworkDataSource,
) : NewsRepository {

    override fun getNewsResourcesStream(): Flow<List<NewsResource>> {
        Log.d("", "OfflineFirstNewsRepository, getNewsResourcesStream")
        return newsResourceDao.getNewsResourcesStream()
            .map {
                Log.d("", "OfflineFirstNewsRepository, getNewsResourcesStream2, " +
                    "it: $it")
                it.map(PopulatedNewsResource::asExternalModel) }
    }


    override fun getNewsResourcesStream(
        filterAuthorIds: Set<String>,
        filterTopicIds: Set<String>
    ): Flow<List<NewsResource>> = newsResourceDao.getNewsResourcesStream(
        filterAuthorIds = filterAuthorIds,
        filterTopicIds = filterTopicIds
    )
        .map { it.map(PopulatedNewsResource::asExternalModel) }

    override suspend fun syncWith(synchronizer: Synchronizer) =
        synchronizer.changeListSync(
            versionReader = ChangeListVersions::newsResourceVersion,
            changeListFetcher = { currentVersion ->
                network.getNewsResourceChangeList(after = currentVersion)
            },
            versionUpdater = { latestVersion ->
                copy(newsResourceVersion = latestVersion)
            },
            modelDeleter = newsResourceDao::deleteNewsResources,
            modelUpdater = { changedIds ->
                Log.d("", "OfflineFirstNewsRepository, syncWith, changedIds: $changedIds")
                val networkNewsResources = network.getNewsResources()

                // Order of invocation matters to satisfy id and foreign key constraints!

                // topicDao.insertOrIgnoreTopics(
                //     topicEntities = networkNewsResources
                //         .map(NetworkNewsResource::topicEntityShells)
                //         .flatten()
                //         .distinctBy(TopicEntity::id)
                // )
                // authorDao.insertOrIgnoreAuthors(
                //     authorEntities = networkNewsResources
                //         .map(NetworkNewsResource::authorEntityShells)
                //         .flatten()
                //         .distinctBy(AuthorEntity::id)
                // )
                episodeDao.insertOrIgnoreEpisodes(
                    episodeEntities = networkNewsResources
                        .map(NetworkNewsResource::episodeEntityShell)
                        .distinctBy(EpisodeEntity::id)
                )
                newsResourceDao.upsertNewsResources(
                    newsResourceEntities = networkNewsResources
                        .map(NetworkNewsResource::asEntity)
                )
                // newsResourceDao.insertOrIgnoreTopicCrossRefEntities(
                //     newsResourceTopicCrossReferences = networkNewsResources
                //         .map(NetworkNewsResource::topicCrossReferences)
                //         .distinct()
                //         .flatten()
                // )
                // newsResourceDao.insertOrIgnoreAuthorCrossRefEntities(
                //     newsResourceAuthorCrossReferences = networkNewsResources
                //         .map(NetworkNewsResource::authorCrossReferences)
                //         .distinct()
                //         .flatten()
                // )
            }
        )
}
