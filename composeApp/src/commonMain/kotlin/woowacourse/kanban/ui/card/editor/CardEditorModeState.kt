package woowacourse.kanban.ui.card.editor

data class CardEditorUiState(
    val title: String,
    val submitText: String,
    val showSubmitButton: Boolean,
    val showDeleteButton: Boolean,
)

fun CardEditorMode.displayInfo(): CardEditorUiState {
    return when (this) {
        CardEditorMode.ADD -> CardEditorUiState(
            title = "새 태스크 생성",
            submitText = "생성",
            showSubmitButton = true,
            showDeleteButton = false,
        )

        CardEditorMode.EDIT -> CardEditorUiState(
            title = "태스크 수정",
            submitText = "수정",
            showSubmitButton = true,
            showDeleteButton = true,
        )
    }
}