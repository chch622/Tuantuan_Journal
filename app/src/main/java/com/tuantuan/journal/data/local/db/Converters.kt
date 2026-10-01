package com.tuantuan.journal.data.local.db

import androidx.room.TypeConverter
import com.tuantuan.journal.domain.model.Gender
import com.tuantuan.journal.domain.model.GrowthType
import com.tuantuan.journal.domain.model.MediaType
import com.tuantuan.journal.domain.model.MilestoneCategory
import com.tuantuan.journal.domain.model.Mood
import com.tuantuan.journal.domain.model.Weather
import java.time.Instant
import java.time.LocalDate

class Converters {

    // Instant converters
    @TypeConverter
    fun fromInstant(value: Instant?): Long? = value?.toEpochMilli()

    @TypeConverter
    fun toInstant(value: Long?): Instant? = value?.let { Instant.ofEpochMilli(it) }

    // LocalDate converters
    @TypeConverter
    fun fromLocalDate(value: LocalDate?): String? = value?.toString()

    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? = value?.let { LocalDate.parse(it) }

    // Gender converter
    @TypeConverter
    fun fromGender(value: Gender?): String? = value?.name

    @TypeConverter
    fun toGender(value: String?): Gender? = value?.let { Gender.valueOf(it) }

    // Mood converter
    @TypeConverter
    fun fromMood(value: Mood?): String? = value?.name

    @TypeConverter
    fun toMood(value: String?): Mood? = value?.let { Mood.valueOf(it) }

    // Weather converter
    @TypeConverter
    fun fromWeather(value: Weather?): String? = value?.name

    @TypeConverter
    fun toWeather(value: String?): Weather? = value?.let { Weather.valueOf(it) }

    // MediaType converter
    @TypeConverter
    fun fromMediaType(value: MediaType?): String? = value?.name

    @TypeConverter
    fun toMediaType(value: String?): MediaType? = value?.let { MediaType.valueOf(it) }

    // GrowthType converter
    @TypeConverter
    fun fromGrowthType(value: GrowthType?): String? = value?.name

    @TypeConverter
    fun toGrowthType(value: String?): GrowthType? = value?.let { GrowthType.valueOf(it) }

    // MilestoneCategory converter
    @TypeConverter
    fun fromMilestoneCategory(value: MilestoneCategory?): String? = value?.name

    @TypeConverter
    fun toMilestoneCategory(value: String?): MilestoneCategory? = value?.let { MilestoneCategory.valueOf(it) }
}