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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import woowacourse.kanban.domain.card.Card
import woowacourse.kanban.domain.card.CardManagerState
import woowacourse.kanban.domain.card.CardTaskState
import woowacourse.kanban.domain.card.TagValidationResult
import woowacourse.kanban.domain.card.TitleValidationResult
import woowacourse.kanban.ui.board.common.toDisplayText
import woowacourse.kanban.ui.card.creation.CardCreationPanelFormSection
import woowacourse.kanban.ui.card.creation.CardFormState
import woowacourse.kanban.ui.card.creation.PanelButton
import woowacourse.kanban.ui.card.creation.PanelButtonDefaultSetting
import woowacourse.kanban.ui.card.creation.message
import woowacourse.kanban.ui.theme.KanbanCardColor.DefaultBackground
import woowacourse.kanban.ui.theme.KanbanCardColor.DefaultContent
import woowacourse.kanban.ui.theme.KanbanCardColor.SelectedBackground
import woowacourse.kanban.ui.theme.KanbanCardColor.SelectedContent
import woowacourse.kanban.ui.theme.Typography.CardCreationTitle

@Composable
fun CardCreationScreen(
    modifier: Modifier = Modifier,
    onAddItem: (Card) -> Unit,
    onDismiss: () -> Unit,
) {
    var cardFormState by remember { mutableStateOf(CardFormState()) }

    Dialog(
        onDismissRequest = onDismiss,
    ) {
        CardCreationScreenContents(
            modifier = modifier,
            cardFormState = cardFormState,
            onCardFormStateChange = { cardFormState = it },
            onAddItem = onAddItem,
            onDismiss = onDismiss,
        )
    }
}

@Composable
internal fun CardCreationScreenContents(
    modifier: Modifier = Modifier,
    cardFormState: CardFormState,
    onCardFormStateChange: (CardFormState) -> Unit,
    onAddItem: (Card) -> Unit,
    onDismiss: () -> Unit,
) {
    val titleValidationResult = cardFormState.titleValidationResult
    val tagValidationResult = cardFormState.tagValidationResult

    OutlinedCard(
        modifier = modifier.testTag("생성 모달 열림"),
    ) {
        Column(
            modifier = Modifier.background(DefaultBackground).width(672.dp),
        ) {
            CardCreationPanelHeaderSection(
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
                CardCreationPanelFormSection(
                    title = "제목 *",
                    placeholder = "태스크 제목을 입력하세요",
                    contents = cardFormState.title,
                    onTextChange = {
                        onCardFormStateChange(cardFormState.copy(title = it))
                    },
                    showAdditionalInfo = titleValidationResult !is TitleValidationResult.Valid,
                    testTag = "titleTextField",
                    infoText = titleValidationResult.message(),
                    isError = titleValidationResult !is TitleValidationResult.Valid,
                )

                CardCreationPanelFormSection(
                    title = "설명",
                    placeholder = "태스크에 대한 자세한 설명을 입력하세요",
                    contents = cardFormState.content,
                    onTextChange = {
                        onCardFormStateChange(cardFormState.copy(content = it))
                    },
                    testTag = "descriptionTextField",
                )

                CardCreationPanelFormSection(
                    title = "태그",
                    placeholder = "태그를 쉼표로 구분하여 입력하세요 (예: 버그, 긴급)",
                    contents = cardFormState.tagInput,
                    onTextChange = {
                        onCardFormStateChange(cardFormState.copy(tagInput = it))
                    },
                    showAdditionalInfo = true,
                    testTag = "tagTextField",
                    infoText = tagValidationResult.message(),
                    isError = tagValidationResult !is TagValidationResult.Valid,
                )

                CardCreationPanelStateSection(
                    selectedState = cardFormState.taskState,
                    onStateChange = {
                        onCardFormStateChange(cardFormState.copy(taskState = it))
                    },
                )

                CardCreationPanelManagerSection(
                    selectedManager = cardFormState.managerState,
                    onManagerChange = {
                        onCardFormStateChange(cardFormState.copy(managerState = it))
                    },
                )

                HorizontalDivider(modifier = Modifier.fillMaxWidth())

                PanelButtonSection(
                    createEnabled = cardFormState.isCreateEnabled,
                    onCancelClick = onDismiss,
                    onCreateClick = {
                        onAddItem(
                            Card.create(
                                title = cardFormState.title,
                                content = cardFormState.content,
                                tags = cardFormState.tags,
                                manager = cardFormState.managerState,
                                state = cardFormState.taskState,
                            ),
                        )
                    },
                )
            }
        }
    }
}

@Composable
private fun CardCreationPanelHeaderSection(
    modifier: Modifier,
    onClose: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "새 태스크 생성",
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
private fun CardCreationPanelStateSection(
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
                        .width(200.dp)
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
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = (-0.3).sp,
            lineHeight = 24.sp,
        )
    }
}

@Composable
private fun CardCreationPanelManagerSection(
    selectedManager: CardManagerState?,
    onManagerChange: (CardManagerState) -> Unit,
) {
    Column {
        Text(
            text = "담당자 *",
            style = CardCreationTitle,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            CardManagerState.entries.forEach { manager ->
                ManagerButton(
                    text = manager.toDisplayText(),
                    isSelected = selectedManager == manager,
                    onClick = { onManagerChange(manager) },
                    modifier = Modifier
                        .width(200.dp)
                        .height(68.dp),
                )
            }
        }
    }
}

@Composable
private fun ManagerButton(
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
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "매니저 아이콘",
                modifier = Modifier.size(24.dp),
                tint = Color(0xFF838383),
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = (-0.15).sp,
                lineHeight = 20.sp,
            )
        }
    }
}

@Composable
private fun PanelButtonSection(
    createEnabled: Boolean,
    modifier: Modifier = Modifier,
    onCancelClick: () -> Unit,
    onCreateClick: () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PanelButton(
            text = "취소",
            contentColor = PanelButtonDefaultSetting.CancelContentColor,
            containerColor = PanelButtonDefaultSetting.CancelContainerColor,
            elevation = PanelButtonDefaultSetting.CancelElevation,
            enabled = true,
            onClick = onCancelClick,
        )
        Spacer(modifier = Modifier.width(12.dp))
        PanelButton(
            text = "생성",
            contentColor = PanelButtonDefaultSetting.CreateContentColor,
            containerColor = PanelButtonDefaultSetting.CreateContainerColor,
            elevation = PanelButtonDefaultSetting.CreateElevation,
            enabled = createEnabled,
            onClick = onCreateClick,
        )
    }
}

@Preview(widthDp = 672, heightDp = 909)
@Composable
fun CardCreationScreenPreview() {
    CardCreationScreen(
        onAddItem = {},
        onDismiss = {},
    )
}