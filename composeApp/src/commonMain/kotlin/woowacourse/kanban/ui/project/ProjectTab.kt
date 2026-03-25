package woowacourse.kanban.ui.project

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import woowacourse.kanban.board.ui.theme.BoardColor.ProjectTabHeaderColor
import woowacourse.kanban.board.ui.theme.KanbanCardColor.DefaultBackground
import woowacourse.kanban.domain.board.Board
import woowacourse.kanban.domain.project.Project

@Composable
fun ProjectTab(
    project: Project,
    modifier: Modifier = Modifier,
    onBoardSelected: (Int) -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .width(207.dp)
            .background(color = DefaultBackground),
    ) {
        ProjectTabHeader(
            modifier = Modifier.fillMaxWidth(),
            title = project.getTitle,
            description = project.getDescription,
        )
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = ProjectTabHeaderColor,
        )
        ProjectContents(
            modifier = Modifier.fillMaxWidth(),
            onBoardSelected = onBoardSelected,
            boards = project.boards,
        )
    }
}

@Composable
private fun ProjectTabHeader(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(24.dp),
    ) {
        Text(
            text = title,
            modifier = Modifier,
            fontWeight = FontWeight.W600,
            fontSize = 18.sp,
            lineHeight = 28.sp,
            letterSpacing = (-0.44).sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = description,
            modifier = Modifier,
            fontWeight = FontWeight.W400,
            fontSize = 14.sp,
            color = Color(0xFF6A7282),
            lineHeight = 20.sp,
            letterSpacing = (-0.15).sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun ProjectContents(
    boards: List<Board>,
    modifier: Modifier = Modifier,
    onBoardSelected: (Int) -> Unit = {},
) {
    var selectedIndex by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxHeight()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        boards.forEachIndexed { index, board ->
            ProjectTabButton(
                tabTitle = board.title,
                onClick = {
                    selectedIndex = index
                    onBoardSelected(index)
                },
                isSelected = index == selectedIndex,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}