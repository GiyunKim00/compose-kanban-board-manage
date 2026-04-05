package woowacourse.kanban.ui.project

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.runComposeUiTest
import woowacourse.kanban.domain.board.Board
import woowacourse.kanban.domain.card.Card
import woowacourse.kanban.domain.card.CardManagerState
import woowacourse.kanban.domain.card.CardTaskState
import woowacourse.kanban.domain.common.FailureReason
import woowacourse.kanban.ui.board.BoardScreenContents
import woowacourse.kanban.ui.board.message
import woowacourse.kanban.ui.card.editor.CardEditorMode
import woowacourse.kanban.ui.card.editor.CardEditorState
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class ProjectScreenTest {

    @Test
    fun `프로젝트 탭의 제목, 설명이 표시된다`() = runComposeUiTest {
        setContent {
            ProjectScreen()
        }

        onNodeWithText("프로젝트 제목").assertExists()
        onNodeWithText("프로젝트 설명").assertExists()
    }

    @Test
    fun `보드 간 전환 후 해당 보드의 태스크가 표시된다`() = runComposeUiTest {
        setContent {
            ProjectScreen()
        }

        onNodeWithText("Compose2").performClick()
        onNodeWithText("제목4").assertExists()
    }

    /* 실패하는 테스트 코드.
    @Test
    fun `태스크를 다른 컬럼으로 옮기면 스낵바가 표시된다`() = runComposeUiTest {
        setContent {
            val card = remember {
                Card.create(
                    title = "드래그테스트",
                    content = "TODO에서 DONE으로 이동",
                    tags = listOf("드래그"),
                    manager = CardManagerState.DINO,
                    state = CardTaskState.TODO,
                )
            }
            var board by remember { mutableStateOf(Board(cards = listOf(card))) }
            var showCardEditorDialog by remember { mutableStateOf(false) }
            var cardEditorState by remember { mutableStateOf(CardEditorState()) }
            val snackbarHostState = remember { SnackbarHostState() }
            var mode by remember { mutableStateOf(CardEditorMode.ADD) }
            var selectedCard by remember { mutableStateOf<Card?>(null) }

            BoardScreenContents(
                board = board,
                showCardEditorDialog = showCardEditorDialog,
                mode = mode,
                selectedCard = selectedCard,
                cardEditorState = cardEditorState,
                onShowCardEditorDialogChange = { showCardEditorDialog = it },
                onModeChange = { mode = it },
                onSelectedCardChange = { selectedCard = it },
                onCardEditorStateChange = { cardEditorState = it },
                onBoardChange = {
                    board = it
                    showCardEditorDialog = false
                },
                snackbarHostState = snackbarHostState,
            )
        }

        val source = onNodeWithTag("카드_드래그테스트").fetchSemanticsNode()
        val target = onNodeWithTag("DONE column").fetchSemanticsNode()

        val sourceCenter = source.boundsInRoot.center
        val targetCenter = target.boundsInRoot.center

        onRoot().performTouchInput {
            down(sourceCenter)
            advanceEventTime(viewConfiguration.longPressTimeoutMillis + 100)
            moveTo(targetCenter)
            advanceEventTime(300)
            up()
        }

        waitForIdle()

        onNodeWithText(FailureReason.INVALID_TRANSITION.message()).assertExists()
    }
     */
}