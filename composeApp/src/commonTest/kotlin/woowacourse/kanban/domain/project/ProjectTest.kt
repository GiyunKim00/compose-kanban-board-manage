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
        val board: Board = Board(title = boardTitle)
        val project: Project = Project(
            boards = listOf(board),
            selectedBoardIndex = 0,
            title = "프로젝트 제목",
            description = "프로젝트 설명",
        )

        assertThat(project.boards[project.selectedBoardIndex].title).isEqualTo(boardTitle)
    }

    @Test
    fun `프로젝트 내 보드 간 전환이 가능하다`() {
        val boardTitles = listOf("abcd", "efgh")
        val oldBoard: Board = Board(title = boardTitles[0])
        val newBoard: Board = Board(title = boardTitles[1])

        var project: Project = Project(
            boards = listOf(oldBoard, newBoard),
            selectedBoardIndex = 0,
            title = "프로젝트 제목",
            description = "프로젝트 설명",
        )

        project = project.switchBoard(1)
        assertThat(project.boards[project.selectedBoardIndex].title).isEqualTo(boardTitles[1])
    }
}