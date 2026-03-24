package woowacourse.kanban.domain.project

import woowacourse.kanban.domain.board.Board

class Project(
    private val boardList: List<Board> = emptyList(),
    private val selectedBoardIndex: Int
) {
    val boards: List<Board> = boardList
    val selectedBoard: Board = boardList[selectedBoardIndex]

}