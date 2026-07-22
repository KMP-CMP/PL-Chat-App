package com.freddie.core.designsystem.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.freddie.core.designsystem.theme.ChirpTheme
import com.freddie.core.designsystem.theme.extended

// 버튼 변형을 enum으로 제한하면 when이 모든 스타일을 처리하는지 컴파일 시점에 검사합니다.
// 색상과 테두리 정책을 한 컴포넌트에서 매핑해 호출부마다 디자인이 달라지는 문제를 줄입니다.
enum class ChirpButtonStyle {
    PRIMARY,
    DESTRUCTIVE_PRIMARY,
    SECONDARY,
    DESTRUCTIVE_SECONDARY,
    TEXT
}

@Composable
fun ChirpButton(
    text: String,
    modifier: Modifier = Modifier,
    style: ChirpButtonStyle = ChirpButtonStyle.PRIMARY,
    enable: Boolean = true,
    isLoading: Boolean = false,
    onClick: () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        enabled = enable,
        shape = RoundedCornerShape(8.dp),
        colors = style.colors(),
        border = style.border(enable)
    ) {
        // 로딩 인디케이터와 본문을 같은 Box에 겹치고 alpha만 전환해 상태가 바뀌어도 버튼 크기를 유지합니다.
        // alpha 0f는 그리기만 숨기므로 접근성 정보까지 제거해야 한다면 semantics를 별도로 처리해야 합니다.
        // https://developer.android.com/reference/kotlin/androidx/compose/ui/draw/alpha.modifier
        Box(
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(15.dp)
                    .alpha(if (isLoading) 1f else 0f),
                strokeWidth = 1.5.dp,
                color = Color.Black
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.alpha(
                    if (isLoading) 0f else 1f
                )
            ) {
                leadingIcon?.invoke()
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}

@Composable
private fun ChirpButtonStyle.colors(): ButtonColors =
    when (this) {
        ChirpButtonStyle.PRIMARY -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.extended.disabledFill,
            disabledContentColor = MaterialTheme.colorScheme.extended.textDisabled
        )
        ChirpButtonStyle.DESTRUCTIVE_PRIMARY -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.error,
            contentColor = MaterialTheme.colorScheme.onError,
            disabledContainerColor = MaterialTheme.colorScheme.extended.disabledFill,
            disabledContentColor = MaterialTheme.colorScheme.extended.textDisabled
        )
        ChirpButtonStyle.SECONDARY -> ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.extended.textSecondary,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = MaterialTheme.colorScheme.extended.textDisabled
        )
        ChirpButtonStyle.DESTRUCTIVE_SECONDARY -> ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.error,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = MaterialTheme.colorScheme.extended.textDisabled
        )
        ChirpButtonStyle.TEXT -> ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.tertiary,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = MaterialTheme.colorScheme.extended.textDisabled
        )
}

@Composable
private fun ChirpButtonStyle.border(enabled: Boolean): BorderStroke? {
    val defaultBorderStroke = BorderStroke(
        width = 1.dp,
        color = MaterialTheme.colorScheme.extended.disabledOutline
    )

    return when {
        this == ChirpButtonStyle.PRIMARY && !enabled -> defaultBorderStroke
        this == ChirpButtonStyle.SECONDARY -> defaultBorderStroke
        this == ChirpButtonStyle.DESTRUCTIVE_PRIMARY && !enabled -> defaultBorderStroke
        this == ChirpButtonStyle.DESTRUCTIVE_SECONDARY -> {
            val borderColor = if (enabled) {
                MaterialTheme.colorScheme.extended.destructiveSecondaryOutline
            } else {
                MaterialTheme.colorScheme.extended.disabledOutline
            }
            BorderStroke(width = 1.dp, color = borderColor)
        }

        else -> null
    }
}

// Preview도 ChirpTheme 아래에서 렌더링해 실제 화면과 같은 Material·확장 색상 값을 공급합니다.
// 테마별 색상 매핑과 CompositionLocal 누락을 앱 실행 전에 확인할 수 있습니다.
// https://developer.android.com/develop/ui/compose/designsystems/custom
@Preview
@Composable
private fun ChirpPrimaryButtonPreview() {
 ChirpTheme(
     darkTheme = true
 ) {
     ChirpButton(
         text = "Hello world!",
         onClick = {}
     )
  }
}

@Preview
@Composable
private fun ChirpSecondaryButtonPreview() {
    ChirpTheme(
        darkTheme = true
    ) {
        ChirpButton(
            text = "Hello world!",
            onClick = {},
            style = ChirpButtonStyle.SECONDARY
        )
    }
}

@Preview
@Composable
private fun ChirpDestructivePrimaryButtonPreview() {
    ChirpTheme(
        darkTheme = true
    ) {
        ChirpButton(
            text = "Hello world!",
            onClick = {},
            style = ChirpButtonStyle.DESTRUCTIVE_PRIMARY
        )
    }
}

@Preview
@Composable
private fun ChirpDestructiveSecondaryButtonPreview() {
    ChirpTheme(
        darkTheme = true
    ) {
        ChirpButton(
            text = "Hello world!",
            onClick = {},
            style = ChirpButtonStyle.DESTRUCTIVE_SECONDARY
        )
    }
}

@Preview
@Composable
private fun ChirpTextButtonPreview() {
    ChirpTheme(
        darkTheme = true
    ) {
        ChirpButton(
            text = "Hello world!",
            onClick = {},
            style = ChirpButtonStyle.TEXT
        )
    }
}
