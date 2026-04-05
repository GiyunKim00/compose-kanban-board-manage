package woowacourse.kanban.ui.card.editor

import woowacourse.kanban.domain.card.Card
import woowacourse.kanban.domain.card.CardManagerState
import woowacourse.kanban.domain.card.CardTaskState
import woowacourse.kanban.domain.card.CardValidator
import woowacourse.kanban.domain.card.TagValidationResult
import woowacourse.kanban.domain.card.TitleValidationResult

data class CardEditorState(
    val title: String = "",
    val content: String = "",
    val tagInput: String = "",
    val taskState: CardTaskState = CardTaskState.TODO,
    val managerState: CardManagerState? = null,
) {
    val titleValidationResult: TitleValidationResult = CardValidator.validateTitle(title)

    val tagValidationResult: TagValidationResult = CardValidator.validateTags(tagInput)

    val tags: List<String> = CardValidator.parseTags(tagInput)
    val isManagerRequired: Boolean = taskState != CardTaskState.TODO

    val isManagerValid: Boolean = !isManagerRequired || managerState != null

    val isSubmitEnabled: Boolean = titleValidationResult.isValid &&
            tagValidationResult.isValid &&
            isManagerValid
}

fun CardEditorState.createCard(): Card =
    Card.create(
        title = title,
        content = content,
        tags = tags,
        manager = managerState,
        state = taskState,
    )

fun CardEditorState.editCard(id: String): Card =
    Card.update(
        id = id,
        title = title,
        content = content,
        tags = tags,
        manager = managerState,
        state = taskState,
    )