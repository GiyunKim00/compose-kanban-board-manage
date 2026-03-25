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
import woowacourse.kanban.domain.card.Card
import woowacourse.kanban.domain.card.CardManagerState
import woowacourse.kanban.domain.card.CardTaskState
import woowacourse.kanban.domain.project.Project
import woowacourse.kanban.ui.board.BoardScreen

@Composable
fun ProjectScreen() {
    var project by remember {
        mutableStateOf(
            sampleProject()
        )
    }

    ProjectScreen(
        project = project,
        onBoardSelected = { boardIndex ->
            project = project.switchBoard(boardIndex)
        },
        onAddNewCard = { card ->
            project = project.updateBoard(project.selectedBoard + card)
        },
        onBoardChange = { board ->
            project = project.updateBoard(board)
        },
    )
}

@Composable
fun ProjectScreen(
    modifier: Modifier = Modifier,
    project: Project = sampleProject(),
    onBoardSelected: (Int) -> Unit = {},
    onAddNewCard: (Card) -> Unit = {},
    onBoardChange: (Board) -> Unit = {},
) {
    Row(
        modifier = modifier.fillMaxSize(),
    ) {
        ProjectTab(
            project = project,
            onBoardSelected = onBoardSelected,
        )
        BoardScreen(
            board = project.selectedBoard,
            onAddCard = onAddNewCard,
            onBoardChange = onBoardChange,
        )
    }
}

/* Preview */

private fun sampleProject(): Project {
    return Project(
        boardList = listOf(
            Board(
                boardTitle = "Compose1",
                cardList = listOf(
                    Card.create(
                        title = "제목1",
                        content = "",
                        tags = listOf("태그1", "태그2", "태그3"),
                        manager = CardManagerState.DINO,
                        state = CardTaskState.DONE,
                    ),
                    Card.create(
                        title = "제목2",
                        content = "",
                        tags = listOf("태그1", "태그2"),
                        manager = CardManagerState.DINO,
                        state = CardTaskState.IN_PROGRESS,
                    ),
                    Card.create(
                        title = "제목3",
                        content = "",
                        tags = listOf("태그1", "태그2"),
                        manager = CardManagerState.DINO,
                        state = CardTaskState.TODO,
                    ),
                ),
            ),
            Board(
                boardTitle = "Compose2",
                cardList = listOf(
                    Card.create(
                        title = "제목4",
                        content = "",
                        tags = listOf("태그1", "태그2"),
                        manager = CardManagerState.DINO,
                        state = CardTaskState.DONE,
                    ),
                ),
            ),
            Board(
                boardTitle = "Compose3너무너무긴제목",
                cardList = emptyList(),
            ),
        ),
        selectedBoardIndex = 0,
        projectTitle = "프로젝트 제목",
        projectDescription = "프로젝트 설명",
    )
}

@Preview(showBackground = true, widthDp = 1551, heightDp = 909)
@Composable
private fun ProjectScreenPreview() {
    ProjectScreen()
}
