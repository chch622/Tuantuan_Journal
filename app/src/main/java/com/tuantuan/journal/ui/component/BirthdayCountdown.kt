package com.tuantuan.journal.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.tuantuan.journal.R
import com.tuantuan.journal.ui.theme.TuantuanSpacing
import java.time.LocalDate
import java.time.temporal.ChronoUnit

/**
 * 生日倒计时组件 — HOME_PAGE.md §8
 *
 * 显示格式："🎂 距离团团的生日还有\n128 天"
 * 倒计时数字使用 HeadlineLarge
 * 说明文字使用 BodyMedium
 * 体风格温柔，不过度装饰
 *
 * @param nickname 儿童昵称
 * @param birthDate 出生日期
 * @param modifier Modifier
 */
@Composable
fun BirthdayCountdown(
    nickname: String,
    birthDate: LocalDate,
    modifier: Modifier = Modifier
) {
    val today = LocalDate.now()
    val nextBirthday = runCatching {
        val thisYearBirthday = birthDate.withYear(today.year)
        if (thisYearBirthday.isAfter(today) || thisYearBirthday.isEqual(today)) {
            thisYearBirthday
        } else {
            birthDate.withYear(today.year + 1)
        }
    }.getOrNull()

    val daysUntil = nextBirthday?.let {
        ChronoUnit.DAYS.between(today, it).toInt()
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(
            modifier = Modifier.padding(TuantuanSpacing.Base),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.birthday_countdown, nickname),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(TuantuanSpacing.SM))
            if (daysUntil != null) {
                Text(
                    text = stringResource(R.string.birthday_days, daysUntil),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                Text(
                    text = "—",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}