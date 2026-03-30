package woowacourse.kanban.domain.project

import woowacourse.kanban.domain.board.Board

/**
 * Project Data Class입니다.
 * @param boards : 프로젝트가 보유한 보드 리스트입니다.
 * @param selectedBoardIndex 현재 프로젝트 내에서 보여지는 보드입니다.
 * @param title 프로젝트 탭에 출력되는 제목입니다.
 * @param description 프로젝트 탭에 출력되는 설명입니다.
 */
data class Project(
    val boards: List<Board> = emptyList(),
    val selectedBoardIndex: Int = 0,
    val title: String = "",
    val description: String = "",
) {
    /**
     * BoardList의 Index를 활용하여 프로젝트 탭에서 선택한 보드로 변경합니다.
     * 추후 보드 생성/수정/삭제 기능 요구사항이 추가될 시 id를 활용하도록 리팩토링할 수 있습니다.
     * @param newBoardIndex 변경할 보드의 인덱스입니다.
     * @return 변경된 Project 객체입니다.
     */
    fun switchBoard(newBoardIndex: Int): Project = copy(selectedBoardIndex = newBoardIndex)

    /**
     * BoardList에 새로운 Board를 추가합니다. Board ID를 활용하여 수정사항이 생긴 Board를 교체합니다.
     * @param newBoard 추가할 Board 객체입니다.
     * @return 변경된 Project 객체입니다.
     */
    fun updateBoard(newBoard: Board): Project =
        copy(
            boards = boards.map { board ->
                if (board.id == newBoard.id) newBoard else board
            },
        )
}