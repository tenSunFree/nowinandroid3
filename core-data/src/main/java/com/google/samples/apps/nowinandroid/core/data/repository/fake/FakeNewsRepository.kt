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

package com.samples.nowinandroid_demo.core.data.repository.fake

import com.samples.nowinandroid_demo.core.data.Synchronizer
import com.samples.nowinandroid_demo.core.data.model.asEntity
import com.samples.nowinandroid_demo.core.data.repository.NewsRepository
import com.samples.nowinandroid_demo.core.database.model.NewsResourceEntity
import com.samples.nowinandroid_demo.core.database.model.asExternalModel
import com.samples.nowinandroid_demo.core.model.data.NewsResource
import com.samples.nowinandroid_demo.core.network.Dispatcher
import com.samples.nowinandroid_demo.core.network.NiaDispatchers.IO
import com.samples.nowinandroid_demo.core.network.fake.FakeDataSource
import com.samples.nowinandroid_demo.core.network.model.NetworkNewsResource
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

/**
 * Fake implementation of the [NewsRepository] that retrieves the news resources from a JSON String.
 *
 * This allows us to run the app with fake data, without needing an internet connection or working
 * backend.
 */
class FakeNewsRepository @Inject constructor(
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    private val networkJson: Json
) : NewsRepository {

    override fun getNewsResourcesStream(): Flow<List<NewsResource>> =
        flow {
            emit(
                networkJson.decodeFromString<List<NetworkNewsResource>>(FakeDataSource.data2)
                    .map(NetworkNewsResource::asEntity)
                    .map(NewsResourceEntity::asExternalModel)
            )
        }
            .flowOn(ioDispatcher)

    override fun getNewsResourcesStream(
        filterAuthorIds: Set<String>,
        filterTopicIds: Set<String>,
    ): Flow<List<NewsResource>> =
        flow {
            emit(
                networkJson.decodeFromString<List<NetworkNewsResource>>(FakeDataSource.data2)
                    .filter {
                        it.authors.intersect(filterAuthorIds).isNotEmpty() ||
                            it.topics.intersect(filterTopicIds).isNotEmpty()
                    }
                    .map(NetworkNewsResource::asEntity)
                    .map(NewsResourceEntity::asExternalModel)
            )
        }
            .flowOn(ioDispatcher)

    override suspend fun syncWith(synchronizer: Synchronizer) = true
}
