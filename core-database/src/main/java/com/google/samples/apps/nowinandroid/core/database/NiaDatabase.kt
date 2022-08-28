package com.samples.nowinandroid_demo.core.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.samples.nowinandroid_demo.core.database.dao.AuthorDao
import com.samples.nowinandroid_demo.core.database.dao.EpisodeDao
import com.samples.nowinandroid_demo.core.database.dao.NewsResourceDao
import com.samples.nowinandroid_demo.core.database.dao.TopicDao
import com.samples.nowinandroid_demo.core.database.model.AuthorEntity
import com.samples.nowinandroid_demo.core.database.model.EpisodeAuthorCrossRef
import com.samples.nowinandroid_demo.core.database.model.EpisodeEntity
import com.samples.nowinandroid_demo.core.database.model.NewsResourceAuthorCrossRef
import com.samples.nowinandroid_demo.core.database.model.NewsResourceEntity
import com.samples.nowinandroid_demo.core.database.model.NewsResourceTopicCrossRef
import com.samples.nowinandroid_demo.core.database.model.TopicEntity
import com.samples.nowinandroid_demo.core.database.util.InstantConverter
import com.samples.nowinandroid_demo.core.database.util.NewsResourceTypeConverter

@Database(
    entities = [
        AuthorEntity::class,
        EpisodeAuthorCrossRef::class,
        EpisodeEntity::class,
        NewsResourceAuthorCrossRef::class,
        NewsResourceEntity::class,
        NewsResourceTopicCrossRef::class,
        TopicEntity::class,
    ],
    version = 9,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3, spec = DatabaseMigrations.Schema2to3::class),
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5),
        AutoMigration(from = 5, to = 6),
        AutoMigration(from = 6, to = 7),
        AutoMigration(from = 7, to = 8),
        AutoMigration(from = 8, to = 9),
    ],
    exportSchema = true,
)
@TypeConverters(
    InstantConverter::class,
    NewsResourceTypeConverter::class,
)
abstract class NiaDatabase : RoomDatabase() {
    abstract fun topicDao(): TopicDao
    abstract fun authorDao(): AuthorDao
    abstract fun episodeDao(): EpisodeDao
    abstract fun newsResourceDao(): NewsResourceDao
}
