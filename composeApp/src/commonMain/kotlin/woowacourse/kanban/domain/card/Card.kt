package woowacourse.kanban.domain.card

import woowacourse.kanban.domain.card.Card.Companion.create
import java.util.UUID

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
    val managerState: CardManagerState,
    val taskState: CardTaskState,
) {
    companion object {
        private const val MAX_TAG_COUNT = 5
        private const val MAX_TAG_LENGTH = 5

        fun validateTitle(rawText: String): TitleValidationResult {
            return if (rawText.trim().isNotBlank()) {
                TitleValidationResult.Valid
            } else {
                TitleValidationResult.Blank
            }
        }

        fun parseTag(rawText: String): List<String> {
            if (rawText.isBlank()) return emptyList()
            return rawText.split(",").map { it.trim() }
        }

        fun validateTag(rawText: String): TagValidationResult {
            if (rawText.isBlank()) return TagValidationResult.Valid

            val parsedTags = parseTag(rawText)

            if (parsedTags.any { it.isBlank() }) {
                return TagValidationResult.InvalidBlankTag
            }

            if (parsedTags.size > MAX_TAG_COUNT) {
                return TagValidationResult.TooManyTags
            }

            if (parsedTags.any { it.length > MAX_TAG_LENGTH }) {
                return TagValidationResult.TooLongTag
            }

            return TagValidationResult.Valid
        }

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
            manager: CardManagerState,
            state: CardTaskState,
        ): Card {
            require(validateTitle(title) is TitleValidationResult.Valid) {
                "[Card] 제목은 필수 입력 항목입니다."
            }

            val normalizedTags = tags
                .map { it.trim() }
                .filter { it.isNotEmpty() }

            require(normalizedTags.size <= MAX_TAG_COUNT) {
                "[Card] 태그는 최대 ${MAX_TAG_COUNT}개까지 가능합니다."
            }
            require(normalizedTags.all { it.length <= MAX_TAG_LENGTH }) {
                "[Card] 태그는 최대 ${MAX_TAG_LENGTH}자까지 가능합니다."
            }

            return Card(
                id = UUID.randomUUID().toString(),
                title = title,
                content = content,
                tags = normalizedTags,
                managerState = manager,
                taskState = state,
            )
        }
    }

    fun updateWithNewState(
        newState: CardTaskState,
    ): Card {
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
}
