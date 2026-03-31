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
        mutableStateOf(sampleProject())
    }

    ProjectScreenContents(
        project = project,
        onBoardSelected = { boardIndex ->
            project = project.switchBoard(boardIndex)
        },
        onAddNewCard = { card ->
            project = project.updateBoard(
                project.boards[project.selectedBoardIndex].addCard(card),
            )
        },
        onBoardChange = { board ->
            project = project.updateBoard(board)
        },
    )
}

/**
 * 프로젝트 화면입니다. 프로젝트 탭과 보드 영역을 포함합니다.
 * @param modifier Modifier
 * @param project 프로젝트 데이터입니다.
 * @param onBoardSelected 보드를 선택합니다.
 * @param onAddNewCard 보드에 카드를 추가합니다.
 * @param onBoardChange 보드 데이터를 변경합니다.
 */
@Composable
internal fun ProjectScreenContents(
    project: Project,
    modifier: Modifier = Modifier,
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
            board = project.boards[project.selectedBoardIndex],
            onAddCard = onAddNewCard,
            onBoardChange = onBoardChange,
        )
    }
}

/* Preview */

private fun sampleProject(): Project {
    return Project(
        boards = listOf(
            Board(
                title = "Compose1",
                cards = listOf(
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
                    Card.create(
                        title = "제목4",
                        content = "",
                        tags = listOf("태그1", "태그2"),
                        manager = CardManagerState.FAMES,
                        state = CardTaskState.REVIEW,
                    ),
                ),
            ),
            Board(
                title = "Compose2",
                cards = listOf(
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
                title = "Compose3너무너무긴제목",
                cards = emptyList(),
            ),
        ),
        selectedBoardIndex = 0,
        title = "프로젝트 제목",
        description = "프로젝트 설명",
    )
}

@Preview(showBackground = true, widthDp = 1551, heightDp = 909)
@Composable
private fun ProjectScreenPreview() {
    ProjectScreen()
}
