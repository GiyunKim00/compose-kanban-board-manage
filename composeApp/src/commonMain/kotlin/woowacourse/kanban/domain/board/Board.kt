package woowacourse.kanban.domain.board

import woowacourse.kanban.domain.card.Card
import woowacourse.kanban.domain.card.CardMoveResult
import woowacourse.kanban.domain.card.CardTaskState
import woowacourse.kanban.domain.card.CardUpdateResult
import woowacourse.kanban.domain.common.FailureReason
import woowacourse.kanban.domain.common.generateId

/**
 * Board Data Class입니다.
 * @param id 보드 ID입니다.
 * @param title 보드 제목입니다.
 * @param cards 카드 리스트입니다.
 **/
data class Board(
    val id: String = generateId(),
    val title: String = "",
    val cards: List<Card> = emptyList(),
) {
    val totalTaskCount: Int = cards.size
    val doneTaskCount: Int = cards.count { it.taskState == CardTaskState.DONE }
    val reviewTaskCount: Int = cards.count { it.taskState == CardTaskState.REVIEW }
    val inProgressTaskCount: Int = cards.count { it.taskState == CardTaskState.IN_PROGRESS }
    val toDoTaskCount: Int = cards.count { it.taskState == CardTaskState.TODO }
    val completionRatio = if (totalTaskCount == 0) 0f
    else doneTaskCount.toFloat() / totalTaskCount
    val completionPercentage = (completionRatio * 100).toInt()

    fun cardsByState(state: CardTaskState): List<Card> = cards.filter { it.taskState == state }

    fun addCard(card: Card): Board = copy(cards = cards + card)

    fun moveCard(cardId: String, targetState: CardTaskState): BoardManageResult {
        val originalCard = cards.first { it.id == cardId }

        return when (val result = originalCard.move(targetState)) {
            is CardMoveResult.Success -> {
                val updatedBoard = updatedBoardWithNewCard(result.card)
                BoardManageResult.Success(updatedBoard)
            }

            is CardMoveResult.Failure -> {
                BoardManageResult.Failure(result.reason)
            }
        }
    }

    fun deleteCard(cardId: String): BoardManageResult {
        val targetCard = cards.first { it.id == cardId }

        if (!targetCard.canDelete()) {
            return BoardManageResult.Failure(FailureReason.INVALID_DELETE)
        }

        val updatedBoard = copy(
            cards = cards.filterNot { it.id == cardId },
        )

        return BoardManageResult.Success(updatedBoard)
    }

    fun updateCard(targetCard: Card): BoardManageResult {
        val originalCard = cards.first { it.id == targetCard.id }

        return when (val result = originalCard.validateUpdate(targetCard)) {
            is CardUpdateResult.Success -> {
                val updatedBoard = updatedBoardWithNewCard(result.card)
                BoardManageResult.Success(updatedBoard)
            }

            is CardUpdateResult.Failure -> {
                BoardManageResult.Failure(result.reason)
            }
        }
    }

    private fun updatedBoardWithNewCard(updatedCard: Card): Board =
        copy(
            cards = cards.map { card ->
                if (card.id == updatedCard.id) updatedCard else card
            },
        )
}