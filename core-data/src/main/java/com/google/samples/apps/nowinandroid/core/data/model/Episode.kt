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

package com.samples.nowinandroid_demo.core.data.model

import com.samples.nowinandroid_demo.core.database.model.EpisodeEntity
import com.samples.nowinandroid_demo.core.network.model.NetworkEpisode
import com.samples.nowinandroid_demo.core.network.model.NetworkEpisodeExpanded

fun NetworkEpisode.asEntity() = EpisodeEntity(
    id = id,
    name = name,
    publishDate = publishDate,
    alternateVideo = alternateVideo,
    alternateAudio = alternateAudio,
)

fun NetworkEpisodeExpanded.asEntity() = EpisodeEntity(
    id = id,
    name = name,
    publishDate = publishDate,
    alternateVideo = alternateVideo,
    alternateAudio = alternateAudio,
)
