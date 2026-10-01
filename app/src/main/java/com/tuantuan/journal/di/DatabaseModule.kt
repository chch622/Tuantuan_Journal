package com.tuantuan.journal.di

import android.content.Context
import androidx.room.Room
import com.tuantuan.journal.data.local.db.TuantuanDatabase
import com.tuantuan.journal.data.local.db.dao.ChildDao
import com.tuantuan.journal.data.local.db.dao.DiaryEntryDao
import com.tuantuan.journal.data.local.db.dao.GrowthRecordDao
import com.tuantuan.journal.data.local.db.dao.MediaItemDao
import com.tuantuan.journal.data.local.db.dao.MilestoneDao
import com.tuantuan.journal.data.local.db.dao.TagDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 数据库依赖注入模块。
 *
 * 重要：禁止使用 fallbackToDestructiveMigration()（DATA_MODEL.md 第6节）。
 * 每次 schema 变更必须提供显式 Room Migration，确保用户数据安全。
 * schema JSON 已导出至 app/schemas/ 目录用于测试验证。
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TuantuanDatabase =
        Room.databaseBuilder(
            context,
            TuantuanDatabase::class.java,
            "tuantuan_journal.db"
        )
            .addMigrations(TuantuanDatabase.MIGRATION_1_2)
            .build()

    @Provides
    fun provideChildDao(db: TuantuanDatabase): ChildDao = db.childDao()

    @Provides
    fun provideDiaryEntryDao(db: TuantuanDatabase): DiaryEntryDao = db.diaryEntryDao()

    @Provides
    fun provideMediaItemDao(db: TuantuanDatabase): MediaItemDao = db.mediaItemDao()

    @Provides
    fun provideTagDao(db: TuantuanDatabase): TagDao = db.tagDao()

    @Provides
    fun provideGrowthRecordDao(db: TuantuanDatabase): GrowthRecordDao = db.growthRecordDao()

    @Provides
    fun provideMilestoneDao(db: TuantuanDatabase): MilestoneDao = db.milestoneDao()
}