package woowacourse.kanban.domain.card

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class CardTaskState {
    TODO,
    IN_PROGRESS,
    DONE
}

enum class CardManagerState{
    DINO,
    FAMES,
}