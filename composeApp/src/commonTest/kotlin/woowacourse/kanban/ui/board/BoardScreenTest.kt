package woowacourse.kanban.ui.board

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import woowacourse.kanban.domain.board.Board
import woowacourse.kanban.domain.card.Card
import woowacourse.kanban.domain.card.CardManagerState
import woowacourse.kanban.domain.card.CardTaskState
import woowacourse.kanban.ui.card.editor.CardEditorMode
import woowacourse.kanban.ui.card.editor.CardEditorState
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class BoardScreenTest {

    @Test
    fun `보드에 보드 제목, 완료율, 태스크 생성 버튼, 프로그레스 바, ToDo, In Progress, Done Column이 노출된다`() = runComposeUiTest {
        // Given
        setContent {
            BoardScreenContents(
                board = Board(
                    title = "Compose Desktop 칸반 보드",
                    cards = emptyList(),
                ),
                showCardEditorDialog = false,
                mode = CardEditorMode.ADD,
                selectedCard = null,
                cardEditorState = CardEditorState(),
                onShowCardEditorDialogChange = {},
                onModeChange = {},
                onSelectedCardChange = {},
                onCardEditorStateChange = {},
                onBoardChange = {},
                snackbarHostState = SnackbarHostState(),
            )
        }

        onNodeWithTag("보드 제목").assertExists()
        onNodeWithText("Compose Desktop 칸반 보드").assertExists()
        onNodeWithText("완료율: 0% (0/0)").assertExists()
        onNodeWithTag("새 태스크 생성 버튼").assertExists()
        onNodeWithTag("프로그레스 바").assertExists()
        onNodeWithText("To Do").assertExists()
        onNodeWithText("In Progress").assertExists()
        onNodeWithText("Done").assertExists()
    }

    @Test
    fun `새 태스크 생성 버튼을 누르면 카드 생성 모달이 나타난다`() = runComposeUiTest {
        setContent {
            var board by remember { mutableStateOf(Board()) }
            var showCardEditorDialog by remember { mutableStateOf(false) }

            BoardScreenContents(
                board = board,
                showCardEditorDialog = showCardEditorDialog,
                mode = CardEditorMode.ADD,
                selectedCard = null,
                cardEditorState = CardEditorState(),
                onShowCardEditorDialogChange = { showCardEditorDialog = it },
                onModeChange = {},
                onSelectedCardChange = {},
                onCardEditorStateChange = {},
                onBoardChange = {},
                snackbarHostState = SnackbarHostState(),
            )
        }

        onNodeWithTag("새 태스크 생성 버튼").performClick()
        onNodeWithTag("모달 열림").assertExists()
    }

    @Test
    fun `카드 목록이 표시된다`() = runComposeUiTest {
        val board = Board(
            cards = listOf(
                Card.create(
                    title = "테스트 카드",
                    content = "테스트 내용",
                    tags = listOf("태그"),
                    manager = CardManagerState.DINO,
                    state = CardTaskState.TODO,
                ),
            ),
        )

        setContent {
            BoardScreenContents(
                board = board,
                showCardEditorDialog = false,
                mode = CardEditorMode.ADD,
                selectedCard = null,
                cardEditorState = CardEditorState(),
                onShowCardEditorDialogChange = {},
                onModeChange = {},
                onSelectedCardChange = {},
                onCardEditorStateChange = {},
                onBoardChange = {},
                snackbarHostState = SnackbarHostState(),
            )
        }

        onNodeWithText("테스트 카드").assertExists()
    }

    @Test
    fun `태스크 생성 후 완료율 텍스트가 변경된다`() = runComposeUiTest {
        setContent {
            var board by remember { mutableStateOf(Board()) }
            var showCardEditorDialog by remember { mutableStateOf(false) }
            var cardEditorState by remember { mutableStateOf(CardEditorState()) }

            BoardScreenContents(
                board = board,
                showCardEditorDialog = showCardEditorDialog,
                mode = CardEditorMode.ADD,
                selectedCard = null,
                cardEditorState = cardEditorState,
                onShowCardEditorDialogChange = { showCardEditorDialog = it },
                onModeChange = {},
                onSelectedCardChange = {},
                onCardEditorStateChange = { cardEditorState = it },
                onBoardChange = {
                    board = it
                    showCardEditorDialog = false
                },
                snackbarHostState = SnackbarHostState(),
            )
        }

        onNodeWithTag("새 태스크 생성 버튼").performClick()
        onNodeWithTag("titleTextField").performTextInput("완료 카드")
        onNodeWithTag("descriptionTextField").performTextInput("설명")
        onNodeWithTag("tagTextField").performTextInput("태그")

        onNodeWithTag("Done").performClick()
        onNodeWithText("DINO").performClick()
        onNodeWithText("생성").performClick()

        onNodeWithText("완료 카드").assertExists()
        onNodeWithTag("완료율").assertTextContains("완료율: 100% (1/1)")
    }

    @Test
    fun `태스크 생성 후 Snackbar가 노출된다`() = runComposeUiTest {
        setContent {
            var board by remember { mutableStateOf(Board()) }
            var showCardEditorDialog by remember { mutableStateOf(false) }
            var cardEditorState by remember { mutableStateOf(CardEditorState()) }
            var snackbarHostState by remember {mutableStateOf(SnackbarHostState())}

            BoardScreenContents(
                board = board,
                showCardEditorDialog = showCardEditorDialog,
                mode = CardEditorMode.ADD,
                selectedCard = null,
                cardEditorState = cardEditorState,
                onShowCardEditorDialogChange = { showCardEditorDialog = it },
                onModeChange = {},
                onSelectedCardChange = {},
                onCardEditorStateChange = { cardEditorState = it },
                onBoardChange = {
                    board = it
                    showCardEditorDialog = false
                },
                snackbarHostState = snackbarHostState,
            )
        }

        onNodeWithTag("새 태스크 생성 버튼").performClick()
        onNodeWithTag("titleTextField").performTextInput("새 카드")
        onNodeWithTag("descriptionTextField").performTextInput("설명")
        onNodeWithTag("tagTextField").performTextInput("태그")
        onNodeWithTag("Done").performClick()
        onNodeWithText("DINO").performClick()
        onNodeWithText("생성").performClick()

        onNodeWithText("새로운 태스크가 추가되었습니다.").assertExists()
    }
}