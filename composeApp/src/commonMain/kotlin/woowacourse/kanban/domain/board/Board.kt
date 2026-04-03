package woowacourse.kanban.domain.board

import woowacourse.kanban.domain.card.Card
import woowacourse.kanban.domain.card.CardManagerState
import woowacourse.kanban.domain.card.CardTaskState
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

        val state = validateTransition(
            card = originalCard,
            targetState = targetState,
            targetManager = originalCard.managerState,
        )

        if (state != BoardManageState.SUCCESS) {
            return BoardManageResult(this, state)
        }

        val updatedBoard = updatedBoardWithNewCard(
            originalCard.updateWithNewState(targetState)
        )

        return BoardManageResult(updatedBoard, BoardManageState.SUCCESS)
    }

    fun deleteCard(cardId: String): BoardManageResult {
        val targetCard = cards.first { it.id == cardId }
        if (!validateDelete(targetCard)) {
            return BoardManageResult(this, BoardManageState.INVALID_DELETE)
        }

        val updatedBoard = copy(
            cards = cards.filterNot { it.id == cardId },
        )

        return BoardManageResult(updatedBoard, BoardManageState.SUCCESS)
    }

    fun updateCard(targetCard:Card): BoardManageResult {
        val originalCard = cards.first { it.id == targetCard.id }

        val state = validateTransition(
            card = originalCard,
            targetState = targetCard.taskState,
            targetManager = targetCard.managerState,
        )

        if (state != BoardManageState.SUCCESS) {
            return BoardManageResult(this, state)
        }

        val updatedBoard = updatedBoardWithNewCard(targetCard)
        return BoardManageResult(updatedBoard, BoardManageState.SUCCESS)
    }

    private fun updatedBoardWithNewCard(updatedCard: Card): Board =
        copy(
            cards = cards.map { card ->
                if (card.id == updatedCard.id) updatedCard else card
            },
        )
}

private fun validateTransition(
    card: Card,
    targetState: CardTaskState,
    targetManager: CardManagerState?,
): BoardManageState {
    val isValidTransition = when (card.taskState) {
        CardTaskState.TODO -> {
            targetState == CardTaskState.TODO || targetState == CardTaskState.IN_PROGRESS
        }

        CardTaskState.IN_PROGRESS -> {
            targetState == CardTaskState.TODO
                    || targetState == CardTaskState.IN_PROGRESS
                    || targetState == CardTaskState.REVIEW
        }

        CardTaskState.REVIEW -> {
            targetState == CardTaskState.IN_PROGRESS
                    || targetState == CardTaskState.REVIEW
                    || targetState == CardTaskState.DONE
        }

        CardTaskState.DONE -> {
            targetState == CardTaskState.TODO || targetState == CardTaskState.DONE
        }
    }

    if (!isValidTransition) return BoardManageState.INVALID_TRANSITION

    if (targetState != CardTaskState.TODO && targetManager == null)
        return BoardManageState.MANAGER_REQUIRED

    return BoardManageState.SUCCESS
}

private fun validateDelete(card: Card): Boolean =
    card.taskState == CardTaskState.TODO || card.taskState == CardTaskState.IN_PROGRESS
