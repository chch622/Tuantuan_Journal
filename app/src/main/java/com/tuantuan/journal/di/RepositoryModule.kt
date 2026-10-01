package com.tuantuan.journal.di

import com.tuantuan.journal.data.local.file.MediaFileManager
import com.tuantuan.journal.data.local.file.MediaFileService
import com.tuantuan.journal.data.repository.ChildRepositoryImpl
import com.tuantuan.journal.data.repository.DiaryRepositoryImpl
import com.tuantuan.journal.data.repository.GrowthRepositoryImpl
import com.tuantuan.journal.data.repository.MediaRepositoryImpl
import com.tuantuan.journal.data.repository.MilestoneRepositoryImpl
import com.tuantuan.journal.data.repository.TagRepositoryImpl
import com.tuantuan.journal.domain.repository.ChildRepository
import com.tuantuan.journal.domain.repository.DiaryRepository
import com.tuantuan.journal.domain.repository.GrowthRepository
import com.tuantuan.journal.domain.repository.MediaRepository
import com.tuantuan.journal.domain.repository.MilestoneRepository
import com.tuantuan.journal.domain.repository.TagRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindChildRepository(impl: ChildRepositoryImpl): ChildRepository

    @Binds
    @Singleton
    abstract fun bindDiaryRepository(impl: DiaryRepositoryImpl): DiaryRepository

    @Binds
    @Singleton
    abstract fun bindMediaRepository(impl: MediaRepositoryImpl): MediaRepository

    @Binds
    @Singleton
    abstract fun bindTagRepository(impl: TagRepositoryImpl): TagRepository

    @Binds
    @Singleton
    abstract fun bindGrowthRepository(impl: GrowthRepositoryImpl): GrowthRepository

    @Binds
    @Singleton
    abstract fun bindMilestoneRepository(impl: MilestoneRepositoryImpl): MilestoneRepository

    @Binds
    @Singleton
    abstract fun bindMediaFileService(impl: MediaFileManager): MediaFileService
}