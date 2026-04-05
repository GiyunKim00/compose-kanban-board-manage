package woowacourse.kanban.domain.card

import woowacourse.kanban.domain.common.FailureReason

enum class CardTaskState(
    val isDeletable: Boolean = false,
    val isManagerRequired: Boolean = true,
) {
    TODO(
        isDeletable = true,
        isManagerRequired = false,
    ) {
        override fun move(card: Card, targetState: CardTaskState): CardMoveResult {
            return when (targetState) {
                TODO, IN_PROGRESS -> CardMoveResult.Success(card.updateWithNewState(targetState))
                else -> CardMoveResult.Failure(FailureReason.INVALID_TRANSITION)
            }
        }

        override fun update(originalCard: Card, targetCard: Card): CardUpdateResult {
            return when (targetCard.taskState) {
                TODO, IN_PROGRESS -> validateTargetCard(targetCard)
                else -> CardUpdateResult.Failure(FailureReason.INVALID_TRANSITION)
            }
        }
    },
    IN_PROGRESS(
        isDeletable = true,
    ) {
        override fun move(card: Card, targetState: CardTaskState): CardMoveResult {
            return when (targetState) {
                TODO, IN_PROGRESS, REVIEW -> CardMoveResult.Success(card.updateWithNewState(targetState))
                else -> CardMoveResult.Failure(FailureReason.INVALID_TRANSITION)
            }
        }

        override fun update(originalCard: Card, targetCard: Card): CardUpdateResult {
            return when (targetCard.taskState) {
                TODO, IN_PROGRESS, REVIEW -> validateTargetCard(targetCard)
                else -> CardUpdateResult.Failure(FailureReason.INVALID_TRANSITION)
            }
        }
    },
    REVIEW {
        override fun move(card: Card, targetState: CardTaskState): CardMoveResult {
            return when (targetState) {
                IN_PROGRESS, REVIEW, DONE -> CardMoveResult.Success(card.updateWithNewState(targetState))
                else -> CardMoveResult.Failure(FailureReason.INVALID_TRANSITION)
            }
        }

        override fun update(originalCard: Card, targetCard: Card): CardUpdateResult {
            return when (targetCard.taskState) {
                IN_PROGRESS, REVIEW, DONE -> validateTargetCard(targetCard)
                else -> CardUpdateResult.Failure(FailureReason.INVALID_TRANSITION)
            }
        }
    },
    DONE {
        override fun move(card: Card, targetState: CardTaskState): CardMoveResult {
            return when (targetState) {
                TODO, DONE -> CardMoveResult.Success(card.updateWithNewState(targetState))
                else -> CardMoveResult.Failure(FailureReason.INVALID_TRANSITION)
            }
        }

        override fun update(originalCard: Card, targetCard: Card): CardUpdateResult {
            return when (targetCard.taskState) {
                TODO, DONE -> validateTargetCard(targetCard)
                else -> CardUpdateResult.Failure(FailureReason.INVALID_TRANSITION)
            }
        }
    };

    abstract fun move(card: Card, targetState: CardTaskState): CardMoveResult

    abstract fun update(originalCard: Card, targetCard: Card): CardUpdateResult

    protected fun validateTargetCard(targetCard: Card): CardUpdateResult {
        if (targetCard.taskState.isManagerRequired && targetCard.managerState == null) {
            return CardUpdateResult.Failure(FailureReason.MANAGER_REQUIRED)
        }
        return CardUpdateResult.Success(targetCard)
    }
}

sealed class CardMoveResult {
    data class Success(val card: Card) : CardMoveResult()
    data class Failure(val reason: FailureReason) : CardMoveResult()
}

sealed class CardUpdateResult {
    data class Success(val card: Card) : CardUpdateResult()
    data class Failure(val reason: FailureReason) : CardUpdateResult()
}

enum class CardManagerState {
    DINO,
    FAMES,
}