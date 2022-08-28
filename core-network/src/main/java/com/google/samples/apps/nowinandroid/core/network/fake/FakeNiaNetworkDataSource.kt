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

package com.samples.nowinandroid_demo.core.network.fake

import android.util.Log
import com.samples.nowinandroid_demo.core.network.Dispatcher
import com.samples.nowinandroid_demo.core.network.NiaDispatchers.IO
import com.samples.nowinandroid_demo.core.network.NiaNetworkDataSource
import com.samples.nowinandroid_demo.core.network.model.NetworkAuthor
import com.samples.nowinandroid_demo.core.network.model.NetworkChangeList
import com.samples.nowinandroid_demo.core.network.model.NetworkNewsResource
import com.samples.nowinandroid_demo.core.network.model.NetworkTopic
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

/**
 * [NiaNetworkDataSource] implementation that provides static news resources to aid development
 */
class FakeNiaNetworkDataSource @Inject constructor(
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    private val networkJson: Json
) : NiaNetworkDataSource {
    override suspend fun getTopics(ids: List<String>?): List<NetworkTopic> =
        withContext(ioDispatcher) {
            networkJson.decodeFromString(FakeDataSource.topicsData)
        }

    override suspend fun getNewsResources(): List<NetworkNewsResource> {
        return  withContext(ioDispatcher) {
            Log.d("", "FakeNiaNetworkDataSource, getNewsResources")
            networkJson.decodeFromString(FakeDataSource.data)
        }
    }


    override suspend fun getAuthors(ids: List<String>?): List<NetworkAuthor> =
        withContext(ioDispatcher) {
            networkJson.decodeFromString(FakeDataSource.authors)
        }

    override suspend fun getTopicChangeList(after: Int?): List<NetworkChangeList> =
        getTopics().mapToChangeList(NetworkTopic::id)

    override suspend fun getAuthorChangeList(after: Int?): List<NetworkChangeList> =
        getAuthors().mapToChangeList(NetworkAuthor::id)

    override suspend fun getNewsResourceChangeList(after: Int?): List<NetworkChangeList> {
        Log.d("", "FakeNiaNetworkDataSource, getNewsResourceChangeList, after: $after")
        Log.d("", "FakeNiaNetworkDataSource, getNewsResourceChangeList, id: ${NetworkNewsResource::id}")
        return getNewsResources().mapToChangeList(NetworkNewsResource::id)
    }

}

/**
 * Converts a list of [T] to change list of all the items in it where [idGetter] defines the
 * [NetworkChangeList.id]
 */
private fun <T> List<T>.mapToChangeList(
    idGetter: (T) -> String
) = mapIndexed { index, item ->
    NetworkChangeList(
        id = idGetter(item),
        changeListVersion = index,
        isDelete = false,
    )
}
