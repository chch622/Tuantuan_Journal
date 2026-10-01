package com.tuantuan.journal.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.tuantuan.journal.R
import com.tuantuan.journal.ui.theme.TuantuanSpacing
import java.time.LocalDate
import java.time.Period

/**
 * 年龄显示组件 — HOME_PAGE.md §4 / DESIGN_SYSTEM.md §6.3
 *
 * 显示格式："X岁X个月X天"
 * 数字使用 DisplayMedium + Primary 色（视觉重量高）
 * 单位使用 BodyMedium + OnSurfaceVariant 色
 *
 * @param birthDate 出生日期
 * @param modifier Modifier
 */
@Composable
fun AgeDisplay(
    birthDate: LocalDate,
    modifier: Modifier = Modifier
) {
    val period = Period.between(birthDate, LocalDate.now())
    val years = period.years
    val months = period.months
    val days = period.days

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (years > 0) {
            Text(
                text = years.toString(),
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(TuantuanSpacing.XS))
            Text(
                text = stringResource(R.string.age_years),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(TuantuanSpacing.SM))
        }

        if (months > 0 || years > 0) {
            Text(
                text = months.toString(),
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(TuantuanSpacing.XS))
            Text(
                text = stringResource(R.string.age_months),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(TuantuanSpacing.SM))
        }

        Text(
            text = days.toString(),
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(TuantuanSpacing.XS))
        Text(
            text = stringResource(R.string.age_days),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}