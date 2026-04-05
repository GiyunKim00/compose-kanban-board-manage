package woowacourse.kanban.domain.common

enum class FailureReason {
    INVALID_DELETE,
    INVALID_TRANSITION,
    MANAGER_REQUIRED,
}