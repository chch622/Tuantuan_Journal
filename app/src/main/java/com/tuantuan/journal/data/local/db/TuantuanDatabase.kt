package com.tuantuan.journal.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tuantuan.journal.data.local.db.dao.ChildDao
import com.tuantuan.journal.data.local.db.dao.DiaryEntryDao
import com.tuantuan.journal.data.local.db.dao.MediaItemDao
import com.tuantuan.journal.data.local.db.dao.TagDao
import com.tuantuan.journal.data.local.db.entity.ChildEntity
import com.tuantuan.journal.data.local.db.entity.DiaryEntryEntity
import com.tuantuan.journal.data.local.db.entity.EntryTagEntity
import com.tuantuan.journal.data.local.db.entity.MediaItemEntity
import com.tuantuan.journal.data.local.db.entity.TagEntity

@Database(
    entities = [
        ChildEntity::class,
        DiaryEntryEntity::class,
        MediaItemEntity::class,
        TagEntity::class,
        EntryTagEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class TuantuanDatabase : RoomDatabase() {
    abstract fun childDao(): ChildDao
    abstract fun diaryEntryDao(): DiaryEntryDao
    abstract fun mediaItemDao(): MediaItemDao
    abstract fun tagDao(): TagDao
}