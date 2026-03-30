package woowacourse.kanban.domain.card

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * [Card] Unit 테스트 클래스입니다.
 */
class CardTest {
    @Test
    fun `제목이 공백 문자만 있으면 생성할 수 없다`() {
        val invalidTitles = listOf("   ", "\t", "\n", " \n\t ")

        invalidTitles.forEach { title ->
            assertFailsWith<IllegalArgumentException> {
                Card.create(
                    title = title,
                    content = "",
                    tags = emptyList(),
                    manager = CardManagerState.DINO,
                    state = CardTaskState.TODO,
                )
            }
        }
    }

    @Test
    fun `내용이 있으면 hasContent 리턴 값은 true이다`() {
        val cardData = Card.create(
            title = "제목",
            content = "내용",
            tags = emptyList(),
            manager = CardManagerState.DINO,
            state = CardTaskState.TODO,
        )

        assertTrue(cardData.hasContent())
    }

    @Test
    fun `내용이 공백이면 hasContent 리턴 값은 false이다`() {
        val cardData = Card.create(
            title = "제목",
            content = "   ",
            tags = emptyList(),
            manager = CardManagerState.DINO,
            state = CardTaskState.TODO,
        )

        assertFalse(cardData.hasContent())
    }

    @Test
    fun `태그의 앞뒤 공백은 제거된다`() {
        val cardData = Card.create(
            title = "제목",
            content = "내용",
            tags = listOf(" 태그1 ", "  태그2  "),
            manager = CardManagerState.DINO,
            state = CardTaskState.TODO,
        )

        assertEquals(listOf("태그1", "태그2"), cardData.tags)
    }

    @Test
    fun `공백으로만 구성된 태그는 제거된다`() {
        val cardData = Card.create(
            title = "제목",
            content = "내용",
            tags = listOf("태그1", "   ", "", "  "),
            manager = CardManagerState.DINO,
            state = CardTaskState.TODO,
        )

        assertEquals(listOf("태그1"), cardData.tags)
    }

    @Test
    fun `태그가 5개를 초과하면 생성할 수 없다`() {
        assertFailsWith<IllegalArgumentException> {
            Card.create(
                title = "제목",
                content = "내용",
                tags = listOf("태그1", "태그2", "태그3", "태그4", "태그5", "태그6"),
                manager = CardManagerState.DINO,
                state = CardTaskState.TODO,
            )
        }
    }

    @Test
    fun `태그 내용이 5글자를 초과하면 5글자까지만 유지된다`() {
        assertFailsWith<IllegalArgumentException> {
            Card.create(
                title = "제목",
                content = "내용",
                tags = listOf("우아한테크코스", "안드로이드8기", "칸반보드리팩터링"),
                manager = CardManagerState.DINO,
                state = CardTaskState.TODO,
            )
        }
    }

    @Test
    fun `공백 태그가 포함되면 생성할 수 없다`() {
        assertFailsWith<IllegalArgumentException> {
            Card.create(
                title = "제목",
                content = "내용",
                tags = listOf("태그1", "   ", "", "  "),
                manager = CardManagerState.DINO,
                state = CardTaskState.TODO,
            )
        }
    }

    @Test
    fun `태그가 있으면 hasTag 리턴 값은 true이다`() {
        val cardData = Card.create(
            title = "제목",
            content = "내용",
            tags = listOf("태그1"),
            manager = CardManagerState.DINO,
            state = CardTaskState.TODO,
        )

        assertTrue(cardData.hasTag())
    }

    @Test
    fun `빈 태그가 포함되면 InvalidBlankTag를 반환한다`() {
        val result = CardValidator.validateTags(",...")

        assertEquals(TagValidationResult.InvalidBlankTag, result)
    }

    @Test
    fun `태그 개수가 초과되면 TooManyTags를 반환한다`() {
        val result = CardValidator.validateTags("태그1,태그2,태그3,태그4,태그5,태그6")

        assertEquals(TagValidationResult.TooManyTags, result)
    }

    @Test
    fun `태그 길이가 초과되면 TooLongTag를 반환한다`() {
        val result = CardValidator.validateTags("123456")

        assertEquals(TagValidationResult.TooLongTag, result)
    }

    @Test
    fun `쉼표를 기준으로 태그 문자열을 분리한다`() {
        assertEquals(
            listOf("태그1", "태그2", "태그3"),
            CardValidator.parseTags("태그1,태그2,태그3"),
        )
    }

    @Test
    fun `태그 문자열의 앞뒤 공백을 제거한 후 쉼표를 기준으로 분리한다`() {
        assertEquals(
            listOf("태그1", "태그2"),
            CardValidator.parseTags("태그1,태그2   "),
        )
    }

    @Test
    fun `카드 상태를 변경하면 상태가 변경된 새 카드가 반환된다`() {
        val card = Card.create(
            title = "제목",
            content = "내용",
            tags = listOf("태그"),
            manager = CardManagerState.DINO,
            state = CardTaskState.TODO,
        )

        val updatedCard = card.updateWithNewState(CardTaskState.DONE)

        assertEquals(updatedCard.taskState, CardTaskState.DONE)
        assertEquals(updatedCard.id, card.id)
        assertEquals(updatedCard.title, card.title)
        assertEquals(updatedCard.content, card.content)
        assertEquals(updatedCard.tags, card.tags)
        assertEquals(updatedCard.managerState, card.managerState)
    }
}