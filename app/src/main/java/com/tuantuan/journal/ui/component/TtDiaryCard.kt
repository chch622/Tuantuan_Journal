package com.tuantuan.journal.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.tuantuan.journal.domain.model.DiaryEntry
import com.tuantuan.journal.R
import com.tuantuan.journal.ui.theme.TuantuanRoundedCorner
import com.tuantuan.journal.ui.theme.TuantuanSpacing
import androidx.compose.ui.unit.dp
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * 日记卡片组件 — HOME_PAGE.md §6
 *
 * 大图卡片，优先展示：
 * 1. 主照片（如有）
 * 2. 日期
 * 3. 标题
 * 4. 一句摘要
 *
 * 设计规格：
 * - Card 组件，Large 圆角
 * - Surface 色，Level1 阴影
 * - 卡片间距 SM (8dp)
 *
 * @param entry 日记条目
 * @param onClick 点击回调
 * @param modifier Modifier
 */
@Composable
fun TtDiaryCard(
    entry: DiaryEntry,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = com.tuantuan.journal.ui.theme.TuantuanElevation.Level1
        )
    ) {
        Column(
            modifier = Modifier.padding(TuantuanSpacing.Base)
        ) {
            // 照片占位（有照片时显示照片，无照片时显示图标占位）
            val hasPhoto = entry.mediaItems.isNotEmpty()
            if (hasPhoto) {
                // TODO: Phase 3 - 加载实际照片缩略图
                // 目前使用占位图标
                CardPhotoPlaceholder()
            }

            // 日期
            val dateFormatter = DateTimeFormatter.ofPattern(stringResource(R.string.date_format_dot))
            val dateStr = entry.eventDateTime
                .atZone(ZoneId.systemDefault())
                .format(dateFormatter)
            Text(
                text = dateStr,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(TuantuanSpacing.XS))

            // 标题（如有）
            entry.title?.let { title ->
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(TuantuanSpacing.XS))
            }

            // 摘要
            Text(
                text = entry.content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun CardPhotoPlaceholder() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(MaterialTheme.shapes.large),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(TuantuanSpacing.Base),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Image,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
    Spacer(modifier = Modifier.height(TuantuanSpacing.SM))
}