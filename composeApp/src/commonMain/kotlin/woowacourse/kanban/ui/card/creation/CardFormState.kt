package woowacourse.kanban.ui.card.creation

import woowacourse.kanban.domain.card.Card
import woowacourse.kanban.domain.card.CardManagerState
import woowacourse.kanban.domain.card.CardTaskState
import woowacourse.kanban.domain.card.TagValidationResult
import woowacourse.kanban.domain.card.TitleValidationResult

data class CardFormState(
    val title: String = "",
    val content: String = "",
    val tagInput: String = "",
    val taskState: CardTaskState = CardTaskState.TODO,
    val managerState: CardManagerState = CardManagerState.DINO,
) {
    val titleValidationResult: TitleValidationResult = Card.validateTitle(title)

    val tagValidationResult: TagValidationResult = Card.validateTag(tagInput)

    val tags: List<String> = Card.parseTag(tagInput)

    val isCreateEnabled: Boolean = titleValidationResult is TitleValidationResult.Valid &&
                tagValidationResult is TagValidationResult.Valid
}