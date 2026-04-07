package woowacourse.kanban.domain.card

import woowacourse.kanban.domain.common.FailureReason
import woowacourse.kanban.domain.common.generateId

/**
 * Card 도메인 모델입니다.
 * 카드 생성 규칙을 적용합니다.
 * 생성은 [create] 팩토리 메서드로 수행합니다.
 */
class Card private constructor(
    val id: String,
    val title: String,
    val content: String,
    val tags: List<String>,
    val managerState: CardManagerState?,
    val taskState: CardTaskState,
) {
    companion object {
        /**
         * [Card] 객체 생성 팩토리 메서드입니다.
         * @param title 필수 | 제목
         * @param content 본문
         * @param tags 태그
         * @param manager 필수 | 계정명
         * @param state 필수 | 업무 상태
         * @throws IllegalArgumentException 기능 요구사항을 충족하지 않을 경우 예외를 던집니다.
         */
        fun create(
            title: String,
            content: String,
            tags: List<String>,
            manager: CardManagerState?,
            state: CardTaskState,
        ): Card {
            return createDefaultCard(
                id = generateId(),
                title = title,
                content = content,
                tags = tags,
                manager = manager,
                state = state,
            )
        }

        fun update(
            id: String,
            title: String,
            content: String,
            tags: List<String>,
            manager: CardManagerState?,
            state: CardTaskState,
        ): Card {
            return createDefaultCard(
                id = id,
                title = title,
                content = content,
                tags = tags,
                manager = manager,
                state = state,
            )
        }

        private fun createDefaultCard(
            id: String,
            title: String,
            content: String,
            tags: List<String>,
            manager: CardManagerState?,
            state: CardTaskState,
        ): Card {
            require(CardValidator.validateTitle(title).isValid) {
                "[Card] 제목은 필수 입력 항목입니다."
            }

            require(CardValidator.validateTags(tags).isValid) {
                "[Card] 태그 형식이 올바르지 않습니다."
            }

            val normalizedTitle = title.trim()
            val normalizedTags = CardValidator.normalizeTags(tags)

            return Card(
                id = id,
                title = normalizedTitle,
                content = content,
                tags = normalizedTags,
                managerState = manager,
                taskState = state,
            )
        }
    }

    fun move(targetState: CardTaskState): CardMoveResult {
        if (targetState.isManagerRequired && !hasManager()) {
            return CardMoveResult.Failure(FailureReason.MANAGER_REQUIRED)
        }

        return taskState.move(this, targetState)
    }

    fun validateUpdate(targetCard: Card): CardUpdateResult {
        return taskState.update(this, targetCard)
    }

    fun canDelete(): Boolean = taskState.isDeletable
    fun updateWithNewState(newState: CardTaskState): Card {
        return Card(
            id = id,
            title = title,
            content = content,
            tags = tags,
            managerState = managerState,
            taskState = newState,
        )
    }

    /**
     * 카드 내용 존재 여부를 리턴합니다.
     * @return 내용이 공백이 아니면 true 리턴.
     */
    fun hasContent(): Boolean = content.isNotBlank()

    /**
     * 태그 존재 여부를 리턴합니다
     * @return 태그가 있다면 true 리턴.
     */
    fun hasTag(): Boolean = tags.isNotEmpty()

    fun hasManager(): Boolean = managerState != null
}
