package com.freddie.core.designsystem.layout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.freddie.core.designsystem.brand.ChirpSuccessIcon
import com.freddie.core.designsystem.buttons.ChirpButton
import com.freddie.core.designsystem.buttons.ChirpButtonStyle
import com.freddie.core.designsystem.theme.ChirpTheme
import com.freddie.core.designsystem.theme.extended

@Composable
fun ChirpSimpleSuccessLayout(
    title: String,
    description: String,
    icon: @Composable () -> Unit,
    primaryButton: @Composable () -> Unit,
    // nullable 슬롯 패턴: 기본값을 빈 람다({})가 아닌 null로 두면 "슬롯 미제공"을 구분할 수 있어
    // 호출부가 생략했을 때 슬롯 주변의 Spacer 같은 부수 UI까지 조건부로 함께 제거할 수 있다.
    // https://developer.android.com/develop/ui/compose/layouts/basics#slot-based-layouts
    secondaryButton: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        icon()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                // Modifier.offset: padding과 달리 측정·배치된 크기(차지하는 공간)는 그대로 두고
                // 그리는 위치만 이동한다 — 아이콘 내부 여백 위로 텍스트 블록을 겹쳐 올리는 용도.
                // https://developer.android.com/develop/ui/compose/modifiers#offset
                .offset(y = -(25).dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.extended.textPrimary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.extended.textSecondary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))

            primaryButton()

            if (secondaryButton != null) {
                Spacer(modifier = Modifier.height(8.dp))
                secondaryButton()
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
@Preview
fun ChirpSimpleSuccessLayoutPreview() {
    ChirpTheme(darkTheme = true) {
        ChirpSimpleSuccessLayout(
            title = "Hello world!",
            description = "Test description",
            icon = {
                ChirpSuccessIcon()
            },
            primaryButton = {
                ChirpButton(
                    text = "Log In",
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                )
            },
            secondaryButton = {
                ChirpButton(
                    text = "Resend verification email",
                    onClick = {},
                    style = ChirpButtonStyle.SECONDARY,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        )
    }
}