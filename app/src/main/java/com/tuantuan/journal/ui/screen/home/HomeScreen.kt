package com.tuantuan.journal.ui.screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.outlined.ChildCare
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tuantuan.journal.domain.model.Child
import com.tuantuan.journal.ui.component.TtEmptyState
import com.tuantuan.journal.ui.component.TtLoadingIndicator
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onChildClick: (String) -> Unit,
    onAddChildClick: () -> Unit,
    onTagManageClick: () -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
            title = { Text("团团日记") },
            actions = {
                IconButton(onClick = onTagManageClick) {
                    Icon(Icons.Default.Tag, contentDescription = "标签管理")
                }
            }
        )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddChildClick) {
                Icon(Icons.Default.Add, contentDescription = "添加儿童")
            }
        }
    ) { padding ->
        if (state.isLoading) {
            TtLoadingIndicator(
                modifier = Modifier.fillMaxSize().padding(padding)
            )
        } else if (state.children.isEmpty()) {
            TtEmptyState(
                title = "还没有儿童档案",
                description = "记录下团团的第一个瞬间吧",
                icon = Icons.Outlined.ChildCare,
                actionLabel = "添加儿童",
                onAction = onAddChildClick,
                modifier = Modifier.fillMaxSize().padding(padding)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.children, key = { it.id }) { child ->
                    ChildCard(
                        child = child,
                        onClick = { onChildClick(child.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ChildCard(child: Child, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 头像占位
            Box(
                modifier = Modifier.size(48.dp).clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = child.nickname,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                val age = runCatching {
                    val period = Period.between(child.birthDate, LocalDate.now())
                    "${period.years}岁${period.months}个月"
                }.getOrDefault("")
                if (age.isNotEmpty()) {
                    Text(
                        text = age,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}