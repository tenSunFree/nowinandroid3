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

package com.samples.nowinandroid_demo.baselineprofile

import androidx.benchmark.macro.ExperimentalBaselineProfilesApi
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.uiautomator.By
import com.samples.nowinandroid_demo.PACKAGE_NAME
import com.samples.nowinandroid_demo.foryou.forYouScrollFeedDownUp
import com.samples.nowinandroid_demo.foryou.forYouSelectAuthors
import com.samples.nowinandroid_demo.foryou.forYouWaitForContent
import com.samples.nowinandroid_demo.interests.interestsScrollPeopleDownUp
import com.samples.nowinandroid_demo.interests.interestsScrollTopicsDownUp
import com.samples.nowinandroid_demo.saved.savedScrollFeedDownUp
import org.junit.Rule
import org.junit.Test

/**
 * Generates a baseline profile which can be copied to `app/src/main/baseline-prof.txt`.
 */
@ExperimentalBaselineProfilesApi
class BaselineProfileGenerator {
    @get:Rule val baselineProfileRule = BaselineProfileRule()

    @Test
    fun generate() =
        baselineProfileRule.collectBaselineProfile(PACKAGE_NAME) {
            // This block defines the app's critical user journey. Here we are interested in
            // optimizing for app startup. But you can also navigate and scroll
            // through your most important UI.

            pressHome()
            startActivityAndWait()

            // Scroll the feed critical user journey
            forYouWaitForContent()
            forYouSelectAuthors()
            forYouScrollFeedDownUp()

            // Navigate to saved screen
            device.findObject(By.text("Saved")).click()
            device.waitForIdle()

            savedScrollFeedDownUp()

            // Navigate to interests screen
            device.findObject(By.text("Interests")).click()
            device.waitForIdle()

            interestsScrollTopicsDownUp()

            // Navigate to people tab
            device.findObject(By.text("People")).click()
            device.waitForIdle()

            interestsScrollPeopleDownUp()
        }
}
