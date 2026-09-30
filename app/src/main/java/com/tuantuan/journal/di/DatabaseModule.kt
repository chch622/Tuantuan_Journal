package com.tuantuan.journal.di

import android.content.Context
import androidx.room.Room
import com.tuantuan.journal.data.local.db.TuantuanDatabase
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
            .build()

    @Provides
    fun provideChildDao(db: TuantuanDatabase) = db.childDao()

    @Provides
    fun provideDiaryEntryDao(db: TuantuanDatabase) = db.diaryEntryDao()

    @Provides
    fun provideMediaItemDao(db: TuantuanDatabase) = db.mediaItemDao()

    @Provides
    fun provideTagDao(db: TuantuanDatabase) = db.tagDao()
}