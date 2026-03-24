package woowacourse.kanban.domain.project

import androidx.compose.ui.test.ExperimentalTestApi
import org.assertj.core.api.Assertions.assertThat
import woowacourse.kanban.domain.board.Board
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class ProjectTest {

    @Test
    fun `프로젝트가 1개 이상의 보드를 가진다`() {
        val boardTitle: String = "abcd"
        val board: Board = Board(boardTitle = boardTitle)
        val project: Project = Project(
            boardList = listOf(board),
            selectedBoardIndex = 0,
        )

        assertThat(project.selectedBoard.title).isEqualTo(boardTitle)
    }

    @Test
    fun `프로젝트 내 보드 간 전환이 가능하다`() {
        val boardTitles = listOf("abcd", "efgh")
        val oldBoard: Board = Board(boardTitle = boardTitles[0])
        val newBoard: Board = Board(boardTitle = boardTitles[1])

        var project: Project = Project(
            boardList = listOf(oldBoard, newBoard),
            selectedBoardIndex = 0,
        )

        project = project.switchBoard(1)
        assertThat(project.selectedBoard.title).isEqualTo(boardTitles[1])
    }

    @Test
    fun `태스크를 옮기면 태스크 상태가 변경된다`() {

    }

    @Test
    fun `태스크를 옮기면 태스크 완료율이 변경된다`() {

    }

    @Test
    fun `태스크를 옮기면 Progress bar가 변경된다`() {

    }
}