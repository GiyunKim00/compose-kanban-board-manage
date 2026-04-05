package woowacourse.kanban.domain.board

import woowacourse.kanban.domain.common.FailureReason

sealed class BoardManageResult {
    data class Success(val board: Board) : BoardManageResult()
    data class Failure(val reason: FailureReason) : BoardManageResult()
}