package com.freddie.core.designsystem.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.freddie.core.designsystem.theme.ChirpTheme
import com.freddie.core.designsystem.theme.extended

/**
 * TextFieldState 기반 BasicTextField(Foundation 1.7+): value/onValueChange 콜백 없이
 * 상태 객체가 텍스트·선택 영역을 직접 소유해 비동기 갱신 시 글자 유실이 없다.
 * https://developer.android.com/develop/ui/compose/text/user-input
 */
@Composable
fun ChirpTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    title: String? = null,
    supportingText: String? = null,
    isError: Boolean = false,
    singleLine: Boolean = false,
    enable: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    onFucusChanged: (Boolean) -> Unit = {}
) {
    // InteractionSource를 직접 생성해 텍스트필드에 넘기면 포커스 여부를 State로 구독할 수 있다.
    // Modifier.onFocusChanged 없이 포커스에 따른 배경/테두리 스타일링이 가능.
    // https://developer.android.com/develop/ui/compose/touch-input/handling-interactions
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val isFocus by interactionSource.collectIsFocusedAsState()

    LaunchedEffect(isFocus) {
        onFucusChanged(isFocus)
    }

    Column(
        modifier = modifier
    ) {
        if(title != null) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.extended.textSecondary
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        BasicTextField(
            state = state,
            enabled = enable,
            // 구버전의 singleLine/maxLines 파라미터 대신 TextFieldLineLimits(SingleLine/MultiLine)로 줄 수를 제약한다.
            lineLimits = if (singleLine) TextFieldLineLimits.SingleLine else TextFieldLineLimits.Default,
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = if (enable) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.extended.textPlaceholder
                }
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
            interactionSource = interactionSource,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = when {
                        isFocus -> MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
                        enable -> MaterialTheme.colorScheme.surface
                        else -> MaterialTheme.colorScheme.extended.secondaryFill
                    },
                    shape = RoundedCornerShape(8.dp)
                )
                .border(
                    width = 1.dp,
                    color = when {
                        isError -> MaterialTheme.colorScheme.error
                        isFocus -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.outline
                    },
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(12.dp),
            // decorator: BasicTextField에는 placeholder 슬롯이 없어 텍스트가 비었을 때 직접 그린다.
            // innerBox()가 실제 입력 영역이므로 반드시 호출해야 한다.
            decorator = { innerBox ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if(state.text.isEmpty() && placeholder != null) {
                        Text(
                            text = placeholder,
                            color = MaterialTheme.colorScheme.extended.textPlaceholder,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    innerBox()
                }
            }
        )

        if (supportingText != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = supportingText,
                color = if (isError) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.extended.textTertiary
                },
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
@Preview(
    showBackground = true
)
fun ChirpTextFieldEmptyPreview() {
    ChirpTheme {
        ChirpTextField(
            state = rememberTextFieldState(),
            modifier = Modifier
                .width(300.dp),
            placeholder = "test@test.com",
            title = "email",
            supportingText = "Please enter your email",
        )
    }
}

@Composable
@Preview(
    showBackground = true
)
fun ChirpTextFieldFilledPreview() {
    ChirpTheme {
        ChirpTextField(
            state = rememberTextFieldState(
                initialText = "test@test.com"
            ),
            modifier = Modifier
                .width(300.dp),
            placeholder = "test@test.com",
            title = "email",
            supportingText = "Please enter your email",
        )
    }
}

@Composable
@Preview(
    showBackground = true
)
fun ChirpTextFieldDisabledPreview() {
    ChirpTheme {
        ChirpTextField(
            state = rememberTextFieldState(
                initialText = "test@test.com"
            ),
            modifier = Modifier
                .width(300.dp),
            placeholder = "test@test.com",
            title = "email",
            supportingText = "Please enter your email",
            enable = false
        )
    }
}

@Composable
@Preview(
    showBackground = true
)
fun ChirpTextFieldErrorPreview() {
    ChirpTheme {
        ChirpTextField(
            state = rememberTextFieldState(
                initialText = "test@test.com"
            ),
            modifier = Modifier
                .width(300.dp),
            placeholder = "test@test.com",
            title = "email",
            supportingText = "This email is Invalid",
            isError = true
        )
    }
}