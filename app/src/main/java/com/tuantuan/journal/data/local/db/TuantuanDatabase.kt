package com.tuantuan.journal.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.tuantuan.journal.data.local.db.dao.ChildDao
import com.tuantuan.journal.data.local.db.dao.DiaryEntryDao
import com.tuantuan.journal.data.local.db.dao.GrowthRecordDao
import com.tuantuan.journal.data.local.db.dao.MediaItemDao
import com.tuantuan.journal.data.local.db.dao.MilestoneDao
import com.tuantuan.journal.data.local.db.dao.TagDao
import com.tuantuan.journal.data.local.db.entity.ChildEntity
import com.tuantuan.journal.data.local.db.entity.DiaryEntryEntity
import com.tuantuan.journal.data.local.db.entity.EntryTagEntity
import com.tuantuan.journal.data.local.db.entity.GrowthRecordEntity
import com.tuantuan.journal.data.local.db.entity.MediaItemEntity
import com.tuantuan.journal.data.local.db.entity.MilestoneEntity
import com.tuantuan.journal.data.local.db.entity.TagEntity

@Database(
    entities = [
        ChildEntity::class,
        DiaryEntryEntity::class,
        MediaItemEntity::class,
        TagEntity::class,
        EntryTagEntity::class,
        GrowthRecordEntity::class,
        MilestoneEntity::class
    ],
    version = 2,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class TuantuanDatabase : RoomDatabase() {
    abstract fun childDao(): ChildDao
    abstract fun diaryEntryDao(): DiaryEntryDao
    abstract fun mediaItemDao(): MediaItemDao
    abstract fun tagDao(): TagDao
    abstract fun growthRecordDao(): GrowthRecordDao
    abstract fun milestoneDao(): MilestoneDao

    companion object {
        /**
         * Migration v1 → v2：添加成长记录和里程碑表。
         *
         * 遵循 DATA_MODEL.md 第6节 — 禁止 fallbackToDestructiveMigration，
         * 每次 schema 变更必须提供显式 Migration。
         */
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // 创建 growth_records 表
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS `growth_records` (
                        `id` TEXT NOT NULL,
                        `childId` TEXT NOT NULL,
                        `recordType` TEXT NOT NULL,
                        `value` REAL NOT NULL,
                        `unit` TEXT NOT NULL,
                        `measureDate` TEXT NOT NULL,
                        `notes` TEXT,
                        `isDeleted` INTEGER NOT NULL DEFAULT 0,
                        `createdAt` INTEGER NOT NULL,
                        `updatedAt` INTEGER NOT NULL,
                        PRIMARY KEY(`id`),
                        FOREIGN KEY(`childId`) REFERENCES `children`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
                    )
                """.trimIndent())

                // 创建 growth_records 索引
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_growth_records_childId` ON `growth_records` (`childId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_growth_records_recordType` ON `growth_records` (`recordType`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_growth_records_measureDate` ON `growth_records` (`measureDate`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_growth_records_isDeleted` ON `growth_records` (`isDeleted`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_growth_records_childId_recordType` ON `growth_records` (`childId`, `recordType`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_growth_records_childId_isDeleted` ON `growth_records` (`childId`, `isDeleted`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_growth_records_childId_recordType_isDeleted` ON `growth_records` (`childId`, `recordType`, `isDeleted`)")

                // 创建 milestones 表
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS `milestones` (
                        `id` TEXT NOT NULL,
                        `childId` TEXT NOT NULL,
                        `category` TEXT NOT NULL,
                        `title` TEXT NOT NULL,
                        `description` TEXT,
                        `achievedDate` TEXT,
                        `isExpected` INTEGER NOT NULL DEFAULT 0,
                        `expectedAgeMonths` INTEGER,
                        `notes` TEXT,
                        `isDeleted` INTEGER NOT NULL DEFAULT 0,
                        `createdAt` INTEGER NOT NULL,
                        `updatedAt` INTEGER NOT NULL,
                        PRIMARY KEY(`id`),
                        FOREIGN KEY(`childId`) REFERENCES `children`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
                    )
                """.trimIndent())

                // 创建 milestones 索引
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_milestones_childId` ON `milestones` (`childId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_milestones_category` ON `milestones` (`category`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_milestones_achievedDate` ON `milestones` (`achievedDate`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_milestones_isDeleted` ON `milestones` (`isDeleted`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_milestones_childId_isDeleted` ON `milestones` (`childId`, `isDeleted`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_milestones_childId_category_isDeleted` ON `milestones` (`childId`, `category`, `isDeleted`)")
            }
        }
    }
}