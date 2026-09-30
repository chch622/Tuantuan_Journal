package com.tuantuan.journal.ui.screen.tag

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Label
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tuantuan.journal.domain.model.Tag
import com.tuantuan.journal.ui.component.TtEmptyState
import com.tuantuan.journal.ui.component.TtErrorState
import com.tuantuan.journal.ui.component.TtLoadingIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagManageScreen(
    onBackClick: () -> Unit,
    viewModel: TagViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var newTagName by remember { mutableStateOf("") }
    var newTagCategory by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("标签管理") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true }
            ) {
                Icon(Icons.Default.Add, contentDescription = "添加标签")
            }
        }
    ) { padding ->
        if (state.isLoading) {
            TtLoadingIndicator(
                modifier = Modifier.fillMaxSize().padding(padding)
            )
        } else if (state.tags.isEmpty()) {
            TtEmptyState(
                title = "还没有标签",
                description = "点击右下角 + 创建第一个标签",
                icon = Icons.Outlined.Label,
                actionLabel = "添加标签",
                onAction = { showAddDialog = true },
                modifier = Modifier.fillMaxSize().padding(padding)
            )
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.padding(padding)
            ) {
                items(state.tags, key = { it.id }) { tag ->
                    TagItem(tag = tag)
                }
            }
        }

        // 错误提示
        state.error?.let { error ->
            TtErrorState(
                error = error,
                onRetry = { viewModel.loadTags() },
                modifier = Modifier.padding(padding)
            )
        }

        // 添加标签对话框（简单实现）
        if (showAddDialog) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = {
                    showAddDialog = false
                    newTagName = ""
                    newTagCategory = ""
                },
                title = { Text("添加标签") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = newTagName,
                            onValueChange = { newTagName = it },
                            label = { Text("标签名称 *") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = newTagCategory,
                            onValueChange = { newTagCategory = it },
                            label = { Text("分类（可选）") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }
                },
                confirmButton = {
                    androidx.compose.material3.TextButton(
                        onClick = {
                            if (newTagName.isNotBlank()) {
                                viewModel.createTag(
                                    name = newTagName.trim(),
                                    category = newTagCategory.ifBlank { null }
                                )
                                newTagName = ""
                                newTagCategory = ""
                                showAddDialog = false
                            }
                        },
                        enabled = newTagName.isNotBlank()
                    ) {
                        Text("添加")
                    }
                },
                dismissButton = {
                    androidx.compose.material3.TextButton(
                        onClick = {
                            showAddDialog = false
                            newTagName = ""
                            newTagCategory = ""
                        }
                    ) {
                        Text("取消")
                    }
                }
            )
        }
    }
}

@Composable
private fun TagItem(tag: Tag) {
    ListItem(
        headlineContent = {
            Text(tag.name, style = MaterialTheme.typography.titleSmall)
        },
        supportingContent = {
            tag.category?.let { category ->
                Text(category, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        },
        trailingContent = {
            tag.color?.let { color ->
                Box(
                    Modifier.padding(end = 8.dp)
                ) {
                    Text(color, style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    )
}