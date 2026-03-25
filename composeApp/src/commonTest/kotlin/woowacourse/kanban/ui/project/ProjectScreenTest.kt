package woowacourse.kanban.ui.project

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
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
}