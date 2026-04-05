package woowacourse.kanban.ui.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import woowacourse.kanban.domain.card.CardManagerState
import woowacourse.kanban.domain.card.CardTaskState
import woowacourse.kanban.domain.card.TagValidationResult
import woowacourse.kanban.domain.card.TitleValidationResult
import woowacourse.kanban.ui.board.common.toDisplayText
import woowacourse.kanban.ui.card.editor.CardEditorButton
import woowacourse.kanban.ui.card.editor.CardEditorButtonDefaultSetting
import woowacourse.kanban.ui.card.editor.CardEditorFormSection
import woowacourse.kanban.ui.card.editor.CardEditorMode
import woowacourse.kanban.ui.card.editor.CardEditorState
import woowacourse.kanban.ui.card.editor.displayInfo
import woowacourse.kanban.ui.card.editor.message
import woowacourse.kanban.ui.theme.KanbanCardColor.DefaultBackground
import woowacourse.kanban.ui.theme.KanbanCardColor.DefaultContent
import woowacourse.kanban.ui.theme.KanbanCardColor.SelectedBackground
import woowacourse.kanban.ui.theme.KanbanCardColor.SelectedContent
import woowacourse.kanban.ui.theme.Typography.CardCreationTitle

@Composable
fun CardEditorDialog(
    mode: CardEditorMode,
    cardEditorState: CardEditorState,
    onCardEditorStateChange: (CardEditorState) -> Unit,
    modifier: Modifier = Modifier,
    onSubmit: (CardEditorState) -> Unit,
    onDelete: (() -> Unit)? = null,
    onDismiss: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
    ) {
        CardEditorDialogContents(
            mode = mode,
            modifier = modifier,
            cardEditorState = cardEditorState,
            onCardEditorStateChange = onCardEditorStateChange,
            onSubmit = onSubmit,
            onDelete = onDelete,
            onDismiss = onDismiss,
        )
    }
}

@Composable
internal fun CardEditorDialogContents(
    mode: CardEditorMode,
    modifier: Modifier = Modifier,
    cardEditorState: CardEditorState,
    onCardEditorStateChange: (CardEditorState) -> Unit,
    onSubmit: (CardEditorState) -> Unit,
    onDelete: (() -> Unit)?,
    onDismiss: () -> Unit,
) {
    val display = mode.displayInfo()
    val titleValidationResult = cardEditorState.titleValidationResult
    val tagValidationResult = cardEditorState.tagValidationResult

    OutlinedCard(
        modifier = modifier.testTag("모달 열림"),
    ) {
        Column(
            modifier = Modifier.background(DefaultBackground),
        ) {
            CardHeaderSection(
                title = display.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 28.dp, horizontal = 24.dp),
                onClose = onDismiss,
            )

            HorizontalDivider(modifier = Modifier.fillMaxWidth())

            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                CardEditorFormSection(
                    title = "제목 *",
                    placeholder = "태스크 제목을 입력하세요",
                    contents = cardEditorState.title,
                    onTextChange = {
                        onCardEditorStateChange(cardEditorState.copy(title = it))
                    },
                    showAdditionalInfo = titleValidationResult !is TitleValidationResult.Valid,
                    testTag = "titleTextField",
                    infoText = titleValidationResult.message(),
                    isError = titleValidationResult !is TitleValidationResult.Valid,
                )

                CardEditorFormSection(
                    title = "설명",
                    placeholder = "태스크에 대한 자세한 설명을 입력하세요",
                    contents = cardEditorState.content,
                    onTextChange = {
                        onCardEditorStateChange(cardEditorState.copy(content = it))
                    },
                    testTag = "descriptionTextField",
                )

                CardEditorFormSection(
                    title = "태그",
                    placeholder = "태그를 쉼표로 구분하여 입력하세요 (예: 버그, 긴급)",
                    contents = cardEditorState.tagInput,
                    onTextChange = {
                        onCardEditorStateChange(cardEditorState.copy(tagInput = it))
                    },
                    showAdditionalInfo = true,
                    testTag = "tagTextField",
                    infoText = tagValidationResult.message(),
                    isError = tagValidationResult !is TagValidationResult.Valid,
                )

                CardStateSection(
                    selectedState = cardEditorState.taskState,
                    onStateChange = {
                        onCardEditorStateChange(cardEditorState.copy(taskState = it))
                    },
                )

                CardManagerSection(
                    selectedState = cardEditorState.taskState,
                    selectedManager = cardEditorState.managerState,
                    onManagerChange = {
                        onCardEditorStateChange(cardEditorState.copy(managerState = it))
                    },
                )

                HorizontalDivider(modifier = Modifier.fillMaxWidth())

                CardEditorButtonSection(
                    submitText = display.submitText,
                    showSubmitButton = display.showSubmitButton,
                    showDeleteButton = display.showDeleteButton,
                    submitEnabled = cardEditorState.isSubmitEnabled,
                    onCancelClick = onDismiss,
                    onSubmitClick = { onSubmit(cardEditorState) },
                    onDeleteClick = onDelete,
                )
            }
        }
    }
}

@Composable
private fun CardHeaderSection(
    title: String,
    modifier: Modifier,
    onClose: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.W600,
            lineHeight = 28.sp,
            letterSpacing = (-0.45).sp,
        )

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "닫기 아이콘",
            modifier = Modifier.clickable { onClose() },
        )
    }
}

@Composable
private fun CardStateSection(
    selectedState: CardTaskState,
    onStateChange: (CardTaskState) -> Unit,
) {
    Column {
        Text(
            text = "상태 *",
            style = CardCreationTitle,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CardTaskState.entries.forEach { state ->
                StateButton(
                    text = state.toDisplayText(),
                    isSelected = selectedState == state,
                    onClick = { onStateChange(state) },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                )
            }
        }
    }
}

@Composable
private fun StateButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val containerColor = if (isSelected) SelectedBackground else DefaultBackground
    val contentColor = if (isSelected) SelectedContent else DefaultContent
    val borderColor = if (isSelected) SelectedContent else SelectedBackground

    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(20),
        border = BorderStroke(1.dp, borderColor),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = containerColor,
            contentColor = contentColor,
        ),
        modifier = modifier.testTag(text),
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = (-0.3).sp,
            lineHeight = 24.sp,
        )
    }
}

@Composable
private fun CardManagerSection(
    selectedState: CardTaskState,
    selectedManager: CardManagerState?,
    onManagerChange: (CardManagerState?) -> Unit,
) {
    Column {
        Text(
            text = if (selectedState != CardTaskState.TODO) "담당자 *" else "담당자",
            style = CardCreationTitle,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            if (selectedState == CardTaskState.TODO) {
                ManagerButton(
                    text = "없음",
                    iconEnable = false,
                    isSelected = selectedManager == null,
                    onClick = { onManagerChange(null) },
                    modifier = Modifier
                        .width(72.dp)
                        .height(68.dp),
                )
            }
            CardManagerState.entries.forEach { manager ->
                ManagerButton(
                    text = manager.toDisplayText(),
                    iconEnable = true,
                    isSelected = selectedManager == manager,
                    onClick = { onManagerChange(manager) },
                    modifier = Modifier
                        .weight(1f)
                        .height(68.dp),
                )
            }
        }
    }
}

@Composable
private fun ManagerButton(
    text: String,
    iconEnable: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val containerColor = if (isSelected) SelectedBackground else DefaultBackground
    val contentColor = if (isSelected) SelectedContent else DefaultContent
    val borderColor = if (isSelected) SelectedContent else SelectedBackground

    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(20),
        border = BorderStroke(1.dp, borderColor),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = containerColor,
            contentColor = contentColor,
        ),
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (iconEnable) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "매니저 아이콘",
                    modifier = Modifier.size(24.dp),
                    tint = Color(0xFF838383),
                )
                Spacer(modifier = Modifier.width(12.dp))
            }
            Text(
                text = text,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = (-0.15).sp,
                lineHeight = 20.sp,
            )
        }
    }
}

@Composable
private fun CardEditorButtonSection(
    submitText: String,
    showSubmitButton: Boolean,
    showDeleteButton: Boolean,
    submitEnabled: Boolean,
    modifier: Modifier = Modifier,
    onCancelClick: () -> Unit,
    onSubmitClick: () -> Unit,
    onDeleteClick: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CardEditorButton(
            text = "취소",
            contentColor = CardEditorButtonDefaultSetting.CancelContentColor,
            containerColor = CardEditorButtonDefaultSetting.CancelContainerColor,
            elevation = CardEditorButtonDefaultSetting.CancelElevation,
            enabled = true,
            onClick = onCancelClick,
        )

        if (showDeleteButton) {
            Spacer(modifier = Modifier.width(12.dp))
            CardEditorButton(
                text = "삭제",
                contentColor = CardEditorButtonDefaultSetting.DeleteContentColor,
                containerColor = CardEditorButtonDefaultSetting.DeleteContainerColor,
                elevation = CardEditorButtonDefaultSetting.DeleteElevation,
                enabled = true,
                onClick = { onDeleteClick?.invoke() },
            )
        }

        if (showSubmitButton) {
            Spacer(modifier = Modifier.width(12.dp))
            CardEditorButton(
                text = submitText,
                contentColor = CardEditorButtonDefaultSetting.SubmitContentColor,
                containerColor = CardEditorButtonDefaultSetting.SubmitContainerColor,
                elevation = CardEditorButtonDefaultSetting.SubmitElevation,
                enabled = submitEnabled,
                onClick = onSubmitClick,
            )
        }
    }
}

@Preview(widthDp = 672, heightDp = 909)
@Composable
fun CardEditorDialogPreview() {
    CardEditorDialog(
        mode = CardEditorMode.ADD,
        cardEditorState = CardEditorState(),
        onCardEditorStateChange = {},
        onSubmit = {},
        onDismiss = {},
    )
}