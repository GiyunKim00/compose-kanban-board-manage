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
}