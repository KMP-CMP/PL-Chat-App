package com.freddie.core.designsystem.textfield

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicSecureTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import chirp.core.designsystem.generated.resources.Res
import chirp.core.designsystem.generated.resources.eye_icon
import chirp.core.designsystem.generated.resources.eye_off_icon
import chirp.core.designsystem.generated.resources.hide_password
import chirp.core.designsystem.generated.resources.show_password
import com.freddie.core.designsystem.theme.ChirpTheme
import com.freddie.core.designsystem.theme.extended
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

/**
 * BasicSecureTextField: 비밀번호 입력 전용 컴포저블. VisualTransformation 없이
 * TextObfuscationMode(Hidden/Visible/RevealLastTyped)로 마스킹을 제어한다.
 * https://developer.android.com/develop/ui/compose/text/user-input
 */
@Composable
fun ChirpPasswordTextField(
    state: TextFieldState,
    isPasswordVisible: Boolean,
    onToggleVisibilityClick: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    title: String? = null,
    supportingText: String? = null,
    isError: Boolean = false,
    enabled: Boolean = true,
    onFocusChanged: (Boolean) -> Unit = {},
) {
    ChirpTextFieldLayout(
        title = title,
        isError = isError,
        supportingText = supportingText,
        enabled = enabled,
        onFocusChanged = onFocusChanged,
        modifier = modifier
    ) { styleModifier, interactionSource ->
        BasicSecureTextField(
            state = state,
            modifier = styleModifier,
            enabled = enabled,
            textObfuscationMode = if(isPasswordVisible) {
                TextObfuscationMode.Visible
            } else TextObfuscationMode.Hidden,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = if(enabled) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.extended.textPlaceholder
                }
            ),
            interactionSource = interactionSource,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
            decorator = { innerBox ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f),
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

                    // CMP 리소스 시스템: composeResources/의 리소스가 Res 클래스로 생성되어
                    // 모든 타깃에서 vectorResource/stringResource로 접근한다 (androidx R 클래스 대체).
                    // https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-multiplatform-resources.html
                    Icon(
                        imageVector = if(isPasswordVisible) {
                            vectorResource(Res.drawable.eye_off_icon)
                        } else {
                            vectorResource(Res.drawable.eye_icon)
                        },
                        contentDescription = if(isPasswordVisible) {
                            stringResource(Res.string.hide_password)
                        } else {
                            stringResource(Res.string.show_password)
                        },
                        tint = MaterialTheme.colorScheme.extended.textDisabled,
                        modifier = Modifier
                            .size(24.dp)
                            // rememberRipple 대체(Compose 1.7+): material3의 ripple()로 Indication을 지정.
                            // bounded = false는 아이콘 경계 밖까지 퍼지는 원형 리플을 만든다.
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(
                                    bounded = false,
                                    radius = 24.dp
                                ),
                                onClick = onToggleVisibilityClick
                            )
                    )
                }
            }
        )
    }
}

@Composable
@Preview(
    showBackground = true
)
fun ChirpPasswordTextFieldEmptyPreview() {
    ChirpTheme {
        ChirpPasswordTextField(
            state = rememberTextFieldState(),
            isPasswordVisible = true,
            onToggleVisibilityClick = {},
            modifier = Modifier
                .width(300.dp),
            placeholder = "Password",
            title = "Password",
            supportingText = "Use 9+ characters, at least one digit and one uppercase letter",
        )
    }
}

@Composable
@Preview(
    showBackground = true
)
fun ChirpPasswordTextFieldFilledPreview() {
    ChirpTheme {
        ChirpPasswordTextField(
            state = rememberTextFieldState("password123"),
            isPasswordVisible = false,
            onToggleVisibilityClick = {},
            modifier = Modifier
                .width(300.dp),
            placeholder = "Password",
            title = "Password",
            supportingText = "Use 9+ characters, at least one digit and one uppercase letter",
        )
    }
}

@Composable
@Preview(
    showBackground = true
)
fun ChirpPasswordTextFieldErrorPreview() {
    ChirpTheme {
        ChirpPasswordTextField(
            state = rememberTextFieldState("password123"),
            isPasswordVisible = true,
            onToggleVisibilityClick = {},
            modifier = Modifier
                .width(300.dp),
            placeholder = "Password",
            title = "Password",
            supportingText = "Doesn't contain an uppercase character",
            isError = true,
        )
    }
}