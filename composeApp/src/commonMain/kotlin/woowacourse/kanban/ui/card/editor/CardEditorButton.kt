package woowacourse.kanban.ui.card.editor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CardEditorButton(
    text: String,
    contentColor: Color,
    containerColor: Color,
    elevation: Dp,
    enabled: Boolean,
    onClick: () -> Unit = {},
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = elevation,
            pressedElevation = elevation,
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
        ),
        shape = RoundedCornerShape(20),
    ) {
        Text(
            text = text,
            color = contentColor,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            letterSpacing = (-0.3).sp,
            lineHeight = 24.sp,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EnabledCardEditorButtonPreview() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        CardEditorButton(
            text = "생성",
            contentColor = CardEditorButtonDefaultSetting.SubmitContentColor,
            containerColor = CardEditorButtonDefaultSetting.SubmitContainerColor,
            elevation = CardEditorButtonDefaultSetting.SubmitElevation,
            enabled = true,
        )

        CardEditorButton(
            text = "취소",
            contentColor = CardEditorButtonDefaultSetting.CancelContentColor,
            containerColor = CardEditorButtonDefaultSetting.CancelContainerColor,
            elevation = CardEditorButtonDefaultSetting.CancelElevation,
            enabled = true,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DisabledCardEditorButtonPreview() {
    CardEditorButton(
        text = "생성",
        contentColor = CardEditorButtonDefaultSetting.SubmitContentColor,
        containerColor = CardEditorButtonDefaultSetting.SubmitContainerColor,
        elevation = CardEditorButtonDefaultSetting.SubmitElevation,
        enabled = false,
    )
}