package com.tuantuan.journal.ui.screen.child

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import com.tuantuan.journal.ui.component.DeleteConfirmDialog
import com.tuantuan.journal.ui.component.TtErrorState
import com.tuantuan.journal.ui.component.TtLoadingIndicator
import androidx.compose.material3.Button

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tuantuan.journal.R
import com.tuantuan.journal.domain.model.Child
import com.tuantuan.journal.domain.model.Gender
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChildDetailScreen(
    childId: String,
    onEditClick: () -> Unit,
    onDiaryClick: (String) -> Unit,
    onAddDiaryClick: () -> Unit,
    onViewDiariesClick: () -> Unit = {},
    onBackClick: () -> Unit,
    onDeleteClick: (() -> Unit)? = null,
    viewModel: ChildViewModel = hiltViewModel()
) {
    LaunchedEffect(childId) { viewModel.loadChild(childId) }
    val state by viewModel.detailState.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.child?.nickname ?: stringResource(R.string.child_detail)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                },
                actions = {
                    IconButton(onClick = onEditClick) {
                        Icon(Icons.Default.Edit, contentDescription = stringResource(R.string.edit))
                    }
                    onDeleteClick?.let {
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.delete))
                        }
                    }
                }
            )
        }
    ) { padding ->
        if (state.isLoading) {
            TtLoadingIndicator(
                modifier = Modifier.fillMaxSize().padding(padding)
            )
        } else if (state.child == null) {
            TtErrorState(
                message = stringResource(R.string.child_not_found),
                modifier = Modifier.fillMaxSize().padding(padding)
            )
        } else {
            val child = state.child!!
            Column(
                Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 头像和基本信息
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(72.dp).clip(CircleShape), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Person, null, Modifier.size(48.dp), MaterialTheme.colorScheme.primary)
                    }
                    Spacer(Modifier.width(16.dp))
                    Column {
                        Text(child.name, style = MaterialTheme.typography.headlineMedium)
                        if (child.nickname != child.name) {
                            Text(
                                stringResource(R.string.child_nickname, child.nickname),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                // 详细信息
                CardSection(stringResource(R.string.child_basic_info)) {
                    InfoRow(stringResource(R.string.child_birth_date), child.birthDate.format(DateTimeFormatter.ofPattern(stringResource(R.string.date_format_full))))
                    val age = runCatching {
                        Period.between(child.birthDate, LocalDate.now()).let {
                            stringResource(R.string.child_age_format, it.years, it.months)
                        }
                    }.getOrDefault("")
                    if (age.isNotEmpty()) InfoRow(stringResource(R.string.child_age), age)
                    child.gender?.let { gender ->
                        InfoRow(stringResource(R.string.child_gender), when (gender) {
                            Gender.MALE -> stringResource(R.string.child_gender_male)
                            Gender.FEMALE -> stringResource(R.string.child_gender_female)
                            else -> stringResource(R.string.child_gender_other)
                        })
                    }
                }

                child.birthWeight?.let {
                    CardSection(stringResource(R.string.child_birth_info)) {
                        InfoRow(stringResource(R.string.child_birth_weight), "${it}kg")
                        child.birthHeight?.let { h -> InfoRow(stringResource(R.string.child_birth_height), "${h}cm") }
                        child.bloodType?.let { b -> InfoRow(stringResource(R.string.child_blood_type), b) }
                        child.birthPlace?.let { p -> InfoRow(stringResource(R.string.child_birth_place), p) }
                    }
                }

                child.notes?.let {
                    CardSection(stringResource(R.string.child_notes)) {
                        Text(it, style = MaterialTheme.typography.bodyMedium)
                    }
                }

                // 操作按钮
                Button(
                    onClick = onAddDiaryClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.child_write_diary))
                }

                OutlinedButton(
                    onClick = onViewDiariesClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.child_view_all_diaries))
                }
            }
        }
    }

    // 删除确认对话框
    if (showDeleteDialog) {
        DeleteConfirmDialog(
            itemName = stringResource(R.string.child_delete_confirm_item),
            isPermanent = true,
            onConfirm = {
                showDeleteDialog = false
                viewModel.deleteChild(childId)
                onDeleteClick?.invoke()
            },
            onDismiss = { showDeleteDialog = false }
        )
    }
}

@Composable
private fun CardSection(title: String, content: @Composable () -> Unit) {
    Column {
        Text(title, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(8.dp))
        content
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(label, Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}