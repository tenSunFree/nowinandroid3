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

package com.samples.nowinandroid_demo.core.network.fake

import com.samples.nowinandroid_demo.core.model.data.NewsResourceType.Codelab
import com.samples.nowinandroid_demo.core.network.model.NetworkNewsResource
import com.samples.nowinandroid_demo.core.network.model.NetworkTopic
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.intellij.lang.annotations.Language

/* ktlint-disable max-line-length */

object FakeDataSource {

    @Language("JSON")
    val topicsData = """[]
    """.trimIndent()

    @Language("JSON")
    val data = """
[
    {
      "id": "59",
      "episodeId": "59",
      "title": "讓人一試成主顧的炭燒烤逢甲店，也是最近逢甲商圈新開不久的炭燒烤店，烤功了得，用料也很實在～",
      "content": "Android Studio Bumblebee (2021.1.1) is now stable. We’ve since patched it to address some launch issues — so make sure to upgrade! It improves functionality across the typical developer workflow: Build and Deploy, Profiling and Inspection, and Design.",
      "url": "https://android-developers.googleblog.com/2022/01/android-studio-bumblebee-202111-stable.html",
      "headerImageUrl3": "https://candylife.tw/wp-content/uploads/20181217225551_85.jpg",
      "publishDate": "2022-01-25T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "7"
      ],
      "authors": [
        "17"
      ]
    },
    {
      "id": "71",
      "episodeId": "71",
      "title": "曾獲得米其林一星推薦的《教父牛排》，從前菜的小麵包、沙拉，到主食的明星牛排，以及飯後甜點，《教父牛排》可以說是老饕們一生中必走訪過一次的星級餐廳。",
      "content": "We launched the Jetpack Watch Face library written from the ground up in Kotlin, including all functionality from the Wearable Support Library along with many new features such as: Watch face styling which persists across both the watch and phone (with no need for your own database or companion app); Support for a WYSIWYG watch face configuration UI on the phone; Smaller, separate libraries (that only include what you need); Battery improvements through promoting good battery usage patterns out of the box, such as automatically reducing the interactive frame rate when the battery is low; New screenshot APIs so users can see previews of their watch face changes in real time on both the watch and phone.\n\nIf you are still using the Wearable Support Library, we strongly encourage migrating to the new Jetpack libraries to take advantage of the new APIs and upcoming features and bug fixes.",
      "url": "https://android-developers.googleblog.com/2021/12/develop-watch-faces-with-stable-jetpack.html",
      "headerImageUrl3": "https://imgs.gvm.com.tw/upload/gallery/20191220/70102_11.jpg",
      "publishDate": "2021-12-01T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "21",
        "11",
        "8"
      ],
      "authors": [
        "22"
      ]
    },
    {
      "id": "80",
      "episodeId": "80",
      "title": "做為台灣老字號頂級鐵板燒品牌的《犇 鐵板燒》，在信義區推出了最高端鐵板燒旗艦店的《犇 極上》品牌，選用台式頂級食材，以犇1991經典日式鐵板燒料理。",
      "content": "",
      "url": "https://medium.com/androiddevelopers/jetnews-for-every-screen-4d8e7927752",
      "headerImageUrl3": "https://imgs.gvm.com.tw/upload/gallery/20191220/70102_10.jpg",
      "publishDate": "2022-01-18T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "3"
      ],
      "authors": [
        "22"
      ]
    },
    {
      "id": "90",
      "episodeId": "90",
      "title": "位於內湖捷運站、康寧國小附近的「FC Bistro」，一樓是單點的餐酒館，二樓則是預約制的客製化菜單私廚，店內裝潢典雅舒適，牆上還有掛滿各種美食比賽的獎牌，可見FC Bistro的主廚料理功夫不得了啊！",
      "content": "content90",
      "url": "https://medium.com/androiddevelopers/jetnews-for-every-screen-4d8e7927752",
      "headerImageUrl3": "https://www.masterpon.com/wp-content/uploads/%E6%9C%AA%E5%91%BD%E5%90%8D%E8%A8%AD%E8%A8%88-1-3.png",
      "publishDate": "2022-01-18T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "3"
      ],
      "authors": [
        "22"
      ]
    },
    {
      "id": "100",
      "episodeId": "100",
      "title": "擁有「火鍋界LV」美譽的《橘色涮涮屋》是老饕們必訪的口袋名單之一，除了主打火鍋、壽喜燒料理外，位於信義區新光三越A9的橘色涮涮屋更結合了美式餐酒館的概念，在等候區設立的酒吧，讓你在等候的期間也可以很時尚！",
      "content": "",
      "url": "https://medium.com/androiddevelopers/jetnews-for-every-screen-4d8e7927752",
      "headerImageUrl3": "https://imgs.gvm.com.tw/upload/gallery/20191220/70102_05.jpg",
      "publishDate": "2022-01-18T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "3"
      ],
      "authors": [
        "22"
      ]
    },
    {
      "id": "105",
      "episodeId": "105",
      "title": "台北莫爾頓牛排館的大包廂擁有信義區101大樓並延伸至整個大台北市區的無垠夜景，是最適合商務或和親朋好友聚餐的選擇。",
      "content": "",
      "url": "https://medium.com/androiddevelopers/jetnews-for-every-screen-4d8e7927752",
      "headerImageUrl3": "https://d1r3ekpbhdi0gp.cloudfront.net/uploads/venue/picture/208/10cabcf626458b6d.jpg",
      "publishDate": "2022-01-18T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "3"
      ],
      "authors": [
        "22"
      ]
    },
    {
      "id": "110",
      "episodeId": "110",
      "title": "被譽為高而不貴所聞名的紅花鐵板燒集團共有三個相關品牌：「紅花」為價格最高的旗艦店、「小紅花」價格約為旗艦店的一半、「漾紅花」最為平價，多在百貨的美食皆可享用。",
      "content": "",
      "url": "https://medium.com/androiddevelopers/jetnews-for-every-screen-4d8e7927752",
      "headerImageUrl3": "https://imgs.gvm.com.tw/upload/gallery/20191009/68751_05.jpg",
      "publishDate": "2022-01-18T00:00:00.000Z",
      "type": "Article 📚",
      "topics": [
        "3"
      ],
      "authors": [
        "22"
      ]
    }
  ]
    """.trimIndent()

    @Language("JSON")
    val data2 = """
[
    
  ]
    """.trimIndent()

    @Language("JSON")
    val authors = """[ ]
    """.trimIndent()
}

/* ktlint-enable max-line-length */
