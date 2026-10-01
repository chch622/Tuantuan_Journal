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

/**
 * 成长概览卡片组件 — HOME_PAGE.md §7 / DESIGN_SYSTEM.md §6.3
 *
 * Phase 1 占位：显示"暂无成长数据"提示
 * Phase 2 完善：显示身高/体重最近一次数据
 *
 * 设计规格：
 * - Surface 背景，Large 圆角
 * - 身高/体重双列布局
 *
 * @param modifier Modifier
 */
@Composable
fun TtGrowthCard(
    modifier: Modifier = Modifier
) {
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
                text = stringResource(R.string.growth_overview),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(TuantuanSpacing.SM))
            Text(
                text = stringResource(R.string.growth_no_data),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(TuantuanSpacing.XS))
            Text(
                text = stringResource(R.string.growth_hint),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}