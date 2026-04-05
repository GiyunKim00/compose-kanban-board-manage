package woowacourse.kanban.ui.board

import woowacourse.kanban.domain.common.FailureReason

fun FailureReason.message(): String = when (this) {
    FailureReason.MANAGER_REQUIRED -> "담당자를 지정해야 상태를 옮길 수 있습니다."
    FailureReason.INVALID_TRANSITION -> "해당 상태로 옮길 수 없습니다."
    FailureReason.INVALID_DELETE -> "해당 상태에서는 태스크 삭제가 불가합니다."
}