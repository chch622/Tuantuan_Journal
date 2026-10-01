package com.tuantuan.journal.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.tuantuan.journal.R
import com.tuantuan.journal.ui.theme.TuantuanSpacing

/**
 * 今日卡片组件 — HOME_PAGE.md §5
 *
 * 没有记录时："今天还没有留下回忆。" + [记录今天] Button
 * 有记录时："今天已经留下 X 个瞬间。" + [查看今天] TextButton
 *
 * 设计规格：
 * - Surface 背景，Large 圆角
 * - 温柔的文案，不催促
 *
 * @param todayEntryCount 今日日记数
 * @param onRecordToday 点击"记录今天"回调
 * @param onViewToday 点击"查看今天"回调
 * @param modifier Modifier
 */
@Composable
fun TtTodayCard(
    todayEntryCount: Int,
    onRecordToday: () -> Unit,
    onViewToday: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(
            modifier = Modifier.padding(TuantuanSpacing.Base),
            verticalArrangement = Arrangement.Center
        ) {
            if (todayEntryCount == 0) {
                Text(
                    text = stringResource(R.string.today_no_memory),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(TuantuanSpacing.MD))
                Button(onClick = onRecordToday) {
                    Text(stringResource(R.string.today_record))
                }
            } else {
                Text(
                    text = stringResource(R.string.today_has_memory, todayEntryCount),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(TuantuanSpacing.MD))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onViewToday) {
                        Text(stringResource(R.string.today_view))
                    }
                }
            }
        }
    }
}