package woowacourse.kanban.domain.project

import com.sun.beans.introspect.PropertyInfo
import woowacourse.kanban.domain.board.Board

class Project(
    private val boardList: List<Board> = emptyList(),
    private val selectedBoardIndex: Int,
    private val projectTitle: String,
    private val projectDescription: String,
) {

    val getTitle = projectTitle
    val getDescription = projectDescription
    val boards: List<Board> = boardList
    val selectedBoard: Board = boardList[selectedBoardIndex]

    fun switchBoard(newBoardIndex: Int): Project = Project(
        boardList = boardList,
        selectedBoardIndex = newBoardIndex,
        projectTitle = projectTitle,
        projectDescription = projectDescription,
    )

}