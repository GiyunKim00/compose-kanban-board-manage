package woowacourse.kanban.domain.board

import woowacourse.kanban.domain.card.Card
import woowacourse.kanban.domain.card.CardTaskState

class Board(
    private val cardList: List<Card> = emptyList(),
) {
    val cards: List<Card> = cardList
    val totalTaskCount: Int = cardList.size
    val doneTaskCount: Int = cardList.count { it.taskState == CardTaskState.DONE }
    val inProgressTaskCount: Int = cardList.count { it.taskState == CardTaskState.IN_PROGRESS }
    val toDoTaskCount: Int = cardList.count { it.taskState == CardTaskState.TODO }
    val completionRatio = if (totalTaskCount == 0) 0f
    else doneTaskCount.toFloat() / totalTaskCount
    val completionPercentage = (completionRatio * 100).toInt()

    fun cardsByState(state: CardTaskState): List<Card> = cards.filter { it.taskState == state  }
    operator fun plus(card: Card): Board = Board(cardList + card)
}