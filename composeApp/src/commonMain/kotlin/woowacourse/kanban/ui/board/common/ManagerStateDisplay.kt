package woowacourse.kanban.ui.board.common

import woowacourse.kanban.domain.card.CardManagerState


fun CardManagerState.toDisplayText(): String {
    return when (this) {
        CardManagerState.DINO -> "DINO"
        CardManagerState.FAMES -> "FAMES"
    }
}