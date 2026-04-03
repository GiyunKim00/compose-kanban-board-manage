package woowacourse.kanban.domain.board

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.assertj.core.api.Assertions.assertThat
import woowacourse.kanban.domain.card.Card
import woowacourse.kanban.domain.card.CardManagerState
import woowacourse.kanban.domain.card.CardTaskState
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BoardTest {

    @Test
    fun `보드에 카드를 추가할 수 있다`() {
        val card = Card.create(
            title = "제목",
            content = "내용내용",
            tags = listOf("태그"),
            manager = CardManagerState.DINO,
            state = CardTaskState.TODO,
        )

        var board = Board().addCard(card)

        assertThat(board.totalTaskCount).isEqualTo(1)
        board = board.addCard(card)
        assertThat(board.totalTaskCount).isEqualTo(2)
    }

    @Test
    fun `state에 따라 Card가 분류된다`() {
        val cardList = listOf(
            Card.create(
                title = "제목1",
                content = "내용내용1",
                tags = listOf("태그1"),
                manager = CardManagerState.DINO,
                state = CardTaskState.TODO,
            ),
            Card.create(
                title = "제목2",
                content = "내용내용2",
                tags = listOf("태그2"),
                manager = CardManagerState.DINO,
                state = CardTaskState.IN_PROGRESS,
            ),
            Card.create(
                title = "제목3",
                content = "내용내용3",
                tags = listOf("태그3"),
                manager = CardManagerState.FAMES,
                state = CardTaskState.REVIEW,
            ),
            Card.create(
                title = "제목4",
                content = "내용내용4",
                tags = listOf("태그4"),
                manager = CardManagerState.FAMES,
                state = CardTaskState.DONE,
            ),
        )

        val board = Board(cards = cardList)

        assertThat(board.toDoTaskCount).isEqualTo(1)
        assertThat(board.inProgressTaskCount).isEqualTo(1)
        assertThat(board.reviewTaskCount).isEqualTo(1)
        assertThat(board.doneTaskCount).isEqualTo(1)
    }

    @Test
    fun `카드가 없으면 빈 보드이다`() {
        val board = Board()

        assertThat(board.totalTaskCount).isEqualTo(0)
        assertThat(board.doneTaskCount).isEqualTo(0)
        assertThat(board.inProgressTaskCount).isEqualTo(0)
        assertThat(board.reviewTaskCount).isEqualTo(0)
        assertThat(board.toDoTaskCount).isEqualTo(0)
    }

    @Test
    fun `카드가 없으면 완료율은 0%이다`() {
        val board = Board()

        assertThat(board.completionPercentage).isEqualTo(0)
    }

    @Test
    fun `전체 카드 3개 중 1개만 완료되었다면 완료율은 33%이다`() {
        val cardList = listOf(
            Card.create(
                title = "제목1",
                content = "내용내용1",
                tags = listOf("태그1"),
                manager = CardManagerState.DINO,
                state = CardTaskState.TODO,
            ),
            Card.create(
                title = "제목2",
                content = "내용내용2",
                tags = listOf("태그2"),
                manager = CardManagerState.DINO,
                state = CardTaskState.IN_PROGRESS,
            ),
            Card.create(
                title = "제목3",
                content = "내용내용3",
                tags = listOf("태그3"),
                manager = CardManagerState.FAMES,
                state = CardTaskState.DONE,
            ),
        )

        val board = Board(cards = cardList)
        assertThat(board.completionPercentage).isEqualTo(33)
    }

    @Test
    fun `카드 상태가 모두 완료되었다면 완료율은 100%이다`() {
        val cardList = listOf(
            Card.create(
                title = "제목1",
                content = "내용내용1",
                tags = listOf("태그1"),
                manager = CardManagerState.DINO,
                state = CardTaskState.DONE,
            ),
            Card.create(
                title = "제목2",
                content = "내용내용2",
                tags = listOf("태그2"),
                manager = CardManagerState.DINO,
                state = CardTaskState.DONE,
            ),
            Card.create(
                title = "제목3",
                content = "내용내용3",
                tags = listOf("태그3"),
                manager = CardManagerState.FAMES,
                state = CardTaskState.DONE,
            ),
        )

        val board = Board(cards = cardList)
        assertThat(board.completionPercentage).isEqualTo(100)
    }

    @Test
    fun `태스크를 옮기면 같은 태스크의 상태가 변경된다`() {
        val oldCard: Card = Card.create(
            title = "제목",
            content = "내용",
            tags = listOf("태그1", "태그2"),
            manager = CardManagerState.DINO,
            state = CardTaskState.TODO,
        )

        val board: Board = Board().addCard(oldCard)

        assertThat(board.toDoTaskCount).isEqualTo(1)
        assertThat(board.doneTaskCount).isEqualTo(0)

        val movedBoard = board.moveCard(
            cardId = oldCard.id,
            targetState = CardTaskState.IN_PROGRESS,
        ).board

        assertThat(movedBoard.toDoTaskCount).isEqualTo(0)
        assertThat(movedBoard.inProgressTaskCount).isEqualTo(1)

        val movedCard = movedBoard.cards.first()
        assertThat(movedCard.id).isEqualTo(oldCard.id)
        assertThat(movedCard.taskState).isEqualTo(CardTaskState.IN_PROGRESS)
        assertThat(movedCard.title).isEqualTo(oldCard.title)
        assertThat(movedCard.content).isEqualTo(oldCard.content)
        assertThat(movedCard.tags).containsExactlyElementsOf(oldCard.tags)
        assertThat(movedCard.managerState).isEqualTo(oldCard.managerState)
    }

    @Test
    fun `태스크를 옮기면 보드의 태스크 완료율이 변경된다`() {
        val oldCard: Card = Card.create(
            title = "제목",
            content = "내용",
            tags = listOf("태그1", "태그2"),
            manager = CardManagerState.DINO,
            state = CardTaskState.REVIEW,
        )

        val board: Board = Board().addCard(oldCard)

        assertThat(board.completionPercentage).isEqualTo(0)

        val movedBoard = board.moveCard(
            cardId = oldCard.id,
            targetState = CardTaskState.DONE,
        ).board

        assertThat(movedBoard.completionPercentage).isEqualTo(100)
    }

    @Test
    fun `To Do 상태 태스크는 삭제할 수 있다`() {
        val card = Card.create(
            title = "제목",
            content = "내용",
            tags = listOf("태그1", "태그2"),
            manager = null,
            state = CardTaskState.TODO,
        )
        val board = Board(cards = listOf(card))

        val result = board.deleteCard(card.id)

        assertThat(result.status).isEqualTo(BoardManageState.SUCCESS)
        assertThat(result.board.totalTaskCount).isEqualTo(0)
        assertThat(result.board.toDoTaskCount).isEqualTo(0)
    }

    @Test
    fun `In Progress 상태 태스크는 삭제할 수 있다`() {
        val card = Card.create(
            title = "제목",
            content = "내용",
            tags = listOf("태그1", "태그2"),
            manager = CardManagerState.FAMES,
            state = CardTaskState.IN_PROGRESS,
        )
        val board = Board(cards = listOf(card))

        val result = board.deleteCard(card.id)

        assertThat(result.status).isEqualTo(BoardManageState.SUCCESS)
        assertThat(result.board.totalTaskCount).isEqualTo(0)
        assertThat(result.board.inProgressTaskCount).isEqualTo(0)
    }

    @Test
    fun `Review 상태 태스크는 삭제할 수 없다`() {
        val card = Card.create(
            title = "제목",
            content = "내용",
            tags = listOf("태그1", "태그2"),
            manager = CardManagerState.FAMES,
            state = CardTaskState.REVIEW,
        )
        val board = Board(cards = listOf(card))

        val result = board.deleteCard(card.id)

        assertThat(result.status).isEqualTo(BoardManageState.INVALID_DELETE)
        assertThat(result.board).isEqualTo(board)
    }

    @Test
    fun `Done 상태 태스크는 삭제할 수 없다`() {
        val card = Card.create(
            title = "제목",
            content = "내용",
            tags = listOf("태그1", "태그2"),
            manager = CardManagerState.FAMES,
            state = CardTaskState.DONE,
        )
        val board = Board(cards = listOf(card))

        val result = board.deleteCard(card.id)

        assertThat(result.status).isEqualTo(BoardManageState.INVALID_DELETE)
        assertThat(result.board).isEqualTo(board)
    }

    @Test
    fun `담당자 없는 To Do 상태 태스크는 In Progress 상태로 전이할 수 없다`() {
        val card = Card.create(
            title = "제목",
            content = "내용",
            tags = listOf("태그1", "태그2"),
            manager = null,
            state = CardTaskState.TODO,
        )
        val board = Board(cards = listOf(card))

        val result = board.moveCard(card.id, CardTaskState.IN_PROGRESS)

        assertThat(result.status).isEqualTo(BoardManageState.MANAGER_REQUIRED)
        assertThat(result.board).isEqualTo(board)
    }

    @Test
    fun `담당자 있는 To Do 상태 태스크는 In Progress 상태로 전이할 수 있다`() {
        val card = Card.create(
            title = "제목",
            content = "내용",
            tags = listOf("태그1", "태그2"),
            manager = CardManagerState.DINO,
            state = CardTaskState.TODO,
        )
        val board = Board(cards = listOf(card))

        val result = board.moveCard(card.id, CardTaskState.IN_PROGRESS)

        assertThat(result.status).isEqualTo(BoardManageState.SUCCESS)
        assertThat(result.board.toDoTaskCount).isEqualTo(0)
        assertThat(result.board.inProgressTaskCount).isEqualTo(1)
        assertThat(result.board.cards.first().taskState).isEqualTo(CardTaskState.IN_PROGRESS)
    }

    @Test
    fun `담당자 있는 To Do 상태 태스크는 Review 상태로 전이할 수 없다`() {
        val card = Card.create(
            title = "제목",
            content = "내용",
            tags = listOf("태그1", "태그2"),
            manager = CardManagerState.DINO,
            state = CardTaskState.TODO,
        )
        val board = Board(cards = listOf(card))

        val result = board.moveCard(card.id, CardTaskState.REVIEW)

        assertThat(result.status).isEqualTo(BoardManageState.INVALID_TRANSITION)
        assertThat(result.board).isEqualTo(board)
    }

    @Test
    fun `담당자 있는 To Do 상태 태스크는 Done 상태로 전이할 수 없다`() {
        val card = Card.create(
            title = "제목",
            content = "내용",
            tags = listOf("태그1", "태그2"),
            manager = CardManagerState.DINO,
            state = CardTaskState.TODO,
        )
        val board = Board(cards = listOf(card))

        val result = board.moveCard(card.id, CardTaskState.DONE)

        assertThat(result.status).isEqualTo(BoardManageState.INVALID_TRANSITION)
        assertThat(result.board).isEqualTo(board)
    }

    @Test
    fun `In Progress 상태 태스크는 Review 상태로 전이할 수 있다`() {
        val card = Card.create(
            title = "제목",
            content = "내용",
            tags = listOf("태그1", "태그2"),
            manager = CardManagerState.DINO,
            state = CardTaskState.IN_PROGRESS,
        )
        val board = Board(cards = listOf(card))

        val result = board.moveCard(card.id, CardTaskState.REVIEW)

        assertThat(result.status).isEqualTo(BoardManageState.SUCCESS)
        assertThat(result.board.inProgressTaskCount).isEqualTo(0)
        assertThat(result.board.reviewTaskCount).isEqualTo(1)
    }

    @Test
    fun `In Progress 상태 태스크는 Done 상태로 전이할 수 없다`() {
        val card = Card.create(
            title = "제목",
            content = "내용",
            tags = listOf("태그1", "태그2"),
            manager = CardManagerState.DINO,
            state = CardTaskState.IN_PROGRESS,
        )
        val board = Board(cards = listOf(card))

        val result = board.moveCard(card.id, CardTaskState.DONE)

        assertThat(result.status).isEqualTo(BoardManageState.INVALID_TRANSITION)
        assertThat(result.board).isEqualTo(board)
    }

    @Test
    fun `Review 상태 태스크는 Done 상태로 전이할 수 있다`() {
        val card = Card.create(
            title = "제목",
            content = "내용",
            tags = listOf("태그1", "태그2"),
            manager = CardManagerState.DINO,
            state = CardTaskState.REVIEW,
        )
        val board = Board(cards = listOf(card))

        val result = board.moveCard(card.id, CardTaskState.DONE)

        assertThat(result.status).isEqualTo(BoardManageState.SUCCESS)
        assertThat(result.board.reviewTaskCount).isEqualTo(0)
        assertThat(result.board.doneTaskCount).isEqualTo(1)
    }

    @Test
    fun `Review 상태 태스크는 To Do 상태로 전이할 수 없다`() {
        val card = Card.create(
            title = "제목",
            content = "내용",
            tags = listOf("태그1", "태그2"),
            manager = CardManagerState.DINO,
            state = CardTaskState.REVIEW,
        )
        val board = Board(cards = listOf(card))

        val result = board.moveCard(card.id, CardTaskState.TODO)

        assertThat(result.status).isEqualTo(BoardManageState.INVALID_TRANSITION)
        assertThat(result.board).isEqualTo(board)
    }

    @Test
    fun `Done 상태 태스크는 To Do 상태로 전이할 수 있다`() {
        val card = Card.create(
            title = "제목",
            content = "내용",
            tags = listOf("태그1", "태그2"),
            manager = CardManagerState.DINO,
            state = CardTaskState.DONE,
        )
        val board = Board(cards = listOf(card))

        val result = board.moveCard(card.id, CardTaskState.TODO)

        assertThat(result.status).isEqualTo(BoardManageState.SUCCESS)
        assertThat(result.board.doneTaskCount).isEqualTo(0)
        assertThat(result.board.toDoTaskCount).isEqualTo(1)
    }

    @Test
    fun `Done 상태 태스크는 In Progress 상태로 전이할 수 없다`() {
        val card = Card.create(
            title = "제목",
            content = "내용",
            tags = listOf("태그1", "태그2"),
            manager = CardManagerState.DINO,
            state = CardTaskState.DONE,
        )
        val board = Board(cards = listOf(card))

        val result = board.moveCard(card.id, CardTaskState.IN_PROGRESS)

        assertThat(result.status).isEqualTo(BoardManageState.INVALID_TRANSITION)
        assertThat(result.board).isEqualTo(board)
    }

    @Test
    fun `Done 상태 태스크는 Review 상태로 전이할 수 없다`() {
        val card = Card.create(
            title = "제목",
            content = "내용",
            tags = listOf("태그1", "태그2"),
            manager = CardManagerState.DINO,
            state = CardTaskState.DONE,
        )
        val board = Board(cards = listOf(card))

        val result = board.moveCard(card.id, CardTaskState.REVIEW)

        assertThat(result.status).isEqualTo(BoardManageState.INVALID_TRANSITION)
        assertThat(result.board).isEqualTo(board)
    }

    @Test
    fun `태스크 수정 시 Board의 상태별 태스크 개수가 변경된다`() {
        val card = Card.create(
            title = "제목",
            content = "내용",
            tags = listOf("태그1", "태그2"),
            manager = CardManagerState.DINO,
            state = CardTaskState.IN_PROGRESS,
        )
        val board = Board(cards = listOf(card))

        val updatedCard = Card.update(
            id = card.id,
            title = card.title,
            content = card.content,
            tags = card.tags,
            manager = card.managerState,
            state = CardTaskState.REVIEW,
        )

        val result = board.updateCard(updatedCard)

        assertThat(result.status).isEqualTo(BoardManageState.SUCCESS)
        assertThat(result.board.inProgressTaskCount).isEqualTo(0)
        assertThat(result.board.reviewTaskCount).isEqualTo(1)
    }

    @Test
    fun `태스크 삭제 시 Board의 상태별 태스크 개수가 변경된다`() {
        val todoCard = Card.create(
            title = "todo",
            content = "내용1",
            tags = listOf("태그1", "태그2"),
            manager = null,
            state = CardTaskState.TODO,
        )
        val doneCard = Card.create(
            title = "done",
            content = "내용2",
            tags = listOf("태그1", "태그2"),
            manager = CardManagerState.DINO,
            state = CardTaskState.DONE,
        )
        val board = Board(cards = listOf(todoCard, doneCard))

        val result = board.deleteCard(todoCard.id)

        assertThat(result.status).isEqualTo(BoardManageState.SUCCESS)
        assertThat(result.board.totalTaskCount).isEqualTo(1)
        assertThat(result.board.toDoTaskCount).isEqualTo(0)
        assertThat(result.board.doneTaskCount).isEqualTo(1)
    }
}