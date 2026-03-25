package woowacourse.kanban.ui.project

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import woowacourse.kanban.domain.board.Board
import woowacourse.kanban.domain.project.Project
import woowacourse.kanban.ui.board.BoardScreen

@Composable
fun ProjectScreen() {
    var project by remember {
        mutableStateOf(
            Project(
                boardList = listOf(
                    Board(boardTitle = "Compose1"),
                    Board(boardTitle = "Compose2"),
                    Board(boardTitle = "Compose3너무너무긴제목")
                ),
                selectedBoardIndex = 0,
                projectTitle = "프로젝트 제목",
                projectDescription = "프로젝트 설명",
            ),
        )
    }
    ProjectScreen(
        project = project,
        onBoardSelected = { boardIndex ->
            project = project.switchBoard(boardIndex)
        },
    )
}


@Composable
fun ProjectScreen(
    project: Project,
    modifier: Modifier = Modifier,
    onBoardSelected: (Int) -> Unit = {},
) {
    Row(
        modifier = modifier.fillMaxSize(),
    ) {
        ProjectTab(
            project = project,
            onBoardSelected = onBoardSelected,
        )
        BoardScreen()
    }

}


@Preview(showBackground = true, widthDp = 1551, heightDp = 909)
@Composable
private fun ProjectScreenPreview() {
    ProjectScreen()
}
