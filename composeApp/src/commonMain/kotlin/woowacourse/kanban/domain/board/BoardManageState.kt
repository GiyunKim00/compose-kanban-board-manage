package woowacourse.kanban.domain.board

enum class BoardManageState {
    SUCCESS,
    INVALID_DELETE,
    MANAGER_REQUIRED,
    INVALID_TRANSITION,
}

data class BoardManageResult(
    val board: Board,
    val status: BoardManageState
) {
    val isSuccess: Boolean = status == BoardManageState.SUCCESS
}