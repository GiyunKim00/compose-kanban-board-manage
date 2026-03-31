package woowacourse.kanban.ui.card.creation

import woowacourse.kanban.domain.card.CardManagerState
import woowacourse.kanban.domain.card.CardTaskState
import woowacourse.kanban.domain.card.CardValidator
import woowacourse.kanban.domain.card.TagValidationResult
import woowacourse.kanban.domain.card.TitleValidationResult

data class CardFormState(
    val title: String = "",
    val content: String = "",
    val tagInput: String = "",
    val taskState: CardTaskState = CardTaskState.TODO,
    val managerState: CardManagerState? = null,
) {
    val titleValidationResult: TitleValidationResult = CardValidator.validateTitle(title)

    val tagValidationResult: TagValidationResult = CardValidator.validateTags(tagInput)

    val tags: List<String> = CardValidator.parseTags(tagInput)
    val isCreateEnabled: Boolean = titleValidationResult.isValid && tagValidationResult.isValid
    val isManagerRequired: Boolean = taskState != CardTaskState.TODO
}