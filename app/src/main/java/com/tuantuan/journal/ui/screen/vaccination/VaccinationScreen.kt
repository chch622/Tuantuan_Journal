package com.tuantuan.journal.ui.screen.vaccination

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Vaccines
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tuantuan.journal.R
import com.tuantuan.journal.domain.model.Vaccination
import com.tuantuan.journal.ui.component.TtEmptyState
import com.tuantuan.journal.ui.component.TtLoadingIndicator
import com.tuantuan.journal.ui.theme.TuantuanSpacing
import java.time.format.DateTimeFormatter

/**
 * 疫苗记录页面
 *
 * 信息架构：
 * 1. 逾期提醒（如有逾期疫苗）
 * 2. 待接种列表
 * 3. 已接种列表
 * 4. FAB 添加疫苗记录
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaccinationScreen(
    childId: String,
    viewModel: VaccinationViewModel = hiltViewModel()
) {
    LaunchedEffect(childId) {
        viewModel.loadVaccinations(childId)
    }
    val state by viewModel.state.collectAsState()
    val formState by viewModel.formState.collectAsState()

    if (state.isLoading) {
        TtLoadingIndicator(modifier = Modifier.fillMaxSize())
        return
    }

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.resetForm()
                    showBottomSheet = true
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.vaccination_add))
            }
        }
    ) { innerPadding ->
        if (!state.hasVaccinations) {
            TtEmptyState(
                title = stringResource(R.string.vaccination_empty_title),
                description = stringResource(R.string.vaccination_empty_desc),
                icon = Icons.Outlined.Vaccines,
                actionLabel = stringResource(R.string.vaccination_empty_action),
                onAction = {
                    viewModel.resetForm()
                    showBottomSheet = true
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = TuantuanSpacing.Base),
                contentPadding = PaddingValues(vertical = TuantuanSpacing.XXL),
                verticalArrangement = Arrangement.spacedBy(TuantuanSpacing.SM)
            ) {
                // 逾期提醒
                if (state.overdueVaccinations.isNotEmpty()) {
                    item(key = "overdue_header") {
                        OverdueReminderSection(
                            overdueCount = state.overdueCount,
                            vaccinations = state.overdueVaccinations,
                            onMarkAdministered = { id -> viewModel.markAdministered(id) }
                        )
                    }
                }

                // 待接种列表
                if (state.pendingVaccinations.isNotEmpty()) {
                    item(key = "pending_header") {
                        SectionHeader(
                            title = stringResource(R.string.vaccination_pending_title),
                            count = state.pendingVaccinations.size
                        )
                    }

                    items(
                        items = state.pendingVaccinations,
                        key = { "pending_${it.id}" }
                    ) { vaccination ->
                        VaccinationCard(
                            vaccination = vaccination,
                            isAdministered = false,
                            onMarkAdministered = { viewModel.markAdministered(vaccination.id) }
                        )
                    }
                }

                // 已接种列表
                if (state.administeredVaccinations.isNotEmpty()) {
                    item(key = "administered_header") {
                        Spacer(modifier = Modifier.height(TuantuanSpacing.SM))
                        SectionHeader(
                            title = stringResource(R.string.vaccination_administered_title),
                            count = state.administeredVaccinations.size
                        )
                    }

                    items(
                        items = state.administeredVaccinations,
                        key = { "administered_${it.id}" }
                    ) { vaccination ->
                        VaccinationCard(
                            vaccination = vaccination,
                            isAdministered = true,
                            onMarkAdministered = null
                        )
                    }
                }
            }
        }
    }

    // 添加疫苗记录 BottomSheet
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState
        ) {
            AddVaccinationSheetContent(
                formState = formState,
                onFormUpdate = { viewModel.updateFormState(it) },
                onSave = {
                    val childId = state.selectedChildId
                    if (childId != null) {
                        viewModel.saveVaccination(childId)
                    }
                }
            )
        }
    }
}

/** 区段标题 */
@Composable
private fun SectionHeader(title: String, count: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(TuantuanSpacing.XS)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "($count)",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/** 逾期提醒区段 */
@Composable
private fun OverdueReminderSection(
    overdueCount: Int,
    vaccinations: List<Vaccination>,
    onMarkAdministered: (String) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.errorContainer
    ) {
        Column(
            modifier = Modifier.padding(TuantuanSpacing.Base)
        ) {
            Text(
                text = stringResource(R.string.vaccination_overdue_hint),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            Spacer(modifier = Modifier.height(TuantuanSpacing.XS))
            vaccinations.take(3).forEach { vaccination ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = vaccination.vaccineName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                    TextButton(onClick = { onMarkAdministered(vaccination.id) }) {
                        Text(text = stringResource(R.string.vaccination_mark_administered))
                    }
                }
            }
        }
    }
}

/** 单条疫苗记录卡片 */
@Composable
private fun VaccinationCard(
    vaccination: Vaccination,
    isAdministered: Boolean,
    onMarkAdministered: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = if (isAdministered) MaterialTheme.colorScheme.surfaceVariant
        else MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier.padding(TuantuanSpacing.Base),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(TuantuanSpacing.SM)
        ) {
            // 内容
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(TuantuanSpacing.XS),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = vaccination.vaccineName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (vaccination.doseNumber != null) {
                        Text(
                            text = stringResource(R.string.vaccination_dose_format, vaccination.doseNumber),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // 日期
                val dateText = if (isAdministered && vaccination.administeredDate != null) {
                    dateFormatter.format(vaccination.administeredDate)
                } else if (vaccination.scheduledDate != null) {
                    stringResource(R.string.vaccination_pending) + " " + dateFormatter.format(vaccination.scheduledDate)
                } else {
                    ""
                }
                if (dateText.isNotEmpty()) {
                    Text(
                        text = dateText,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // 地点
                if (vaccination.location != null) {
                    Text(
                        text = vaccination.location,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // 状态标签或操作按钮
            if (isAdministered) {
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = stringResource(R.string.vaccination_administered),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = TuantuanSpacing.SM, vertical = TuantuanSpacing.XS)
                    )
                }
            } else if (onMarkAdministered != null) {
                TextButton(onClick = onMarkAdministered) {
                    Text(text = stringResource(R.string.vaccination_mark_administered))
                }
            }
        }
    }
}

/** 添加疫苗记录 BottomSheet 内容 */
@Composable
private fun AddVaccinationSheetContent(
    formState: VaccinationFormState,
    onFormUpdate: ((VaccinationFormState) -> VaccinationFormState) -> Unit,
    onSave: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(TuantuanSpacing.LG),
        verticalArrangement = Arrangement.spacedBy(TuantuanSpacing.MD)
    ) {
        Text(
            text = stringResource(R.string.vaccination_add),
            style = MaterialTheme.typography.titleMedium
        )

        OutlinedTextField(
            value = formState.vaccineName,
            onValueChange = { value -> onFormUpdate { it.copy(vaccineName = value) } },
            label = { Text(stringResource(R.string.vaccination_vaccine_name)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = formState.doseNumber,
            onValueChange = { value -> onFormUpdate { it.copy(doseNumber = value) } },
            label = { Text(stringResource(R.string.vaccination_dose_number)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(
            value = formState.scheduledDate,
            onValueChange = { value -> onFormUpdate { it.copy(scheduledDate = value) } },
            label = { Text(stringResource(R.string.vaccination_scheduled_date)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            placeholder = { Text("yyyy-MM-dd") }
        )

        OutlinedTextField(
            value = formState.administeredDate,
            onValueChange = { value -> onFormUpdate { it.copy(administeredDate = value) } },
            label = { Text(stringResource(R.string.vaccination_administered_date)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            placeholder = { Text("yyyy-MM-dd（可选）") }
        )

        OutlinedTextField(
            value = formState.location,
            onValueChange = { value -> onFormUpdate { it.copy(location = value) } },
            label = { Text(stringResource(R.string.vaccination_location)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = formState.notes,
            onValueChange = { value -> onFormUpdate { it.copy(notes = value) } },
            label = { Text(stringResource(R.string.vaccination_notes)) },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3
        )

        TextButton(
            onClick = onSave,
            enabled = formState.isValid && !formState.isSaving,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.vaccination_save))
        }
    }
}